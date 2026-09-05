use ahash::AHasher;
use std::{
    fs::File,
    hash::Hasher,
    io,
    marker::PhantomData,
    mem::MaybeUninit,
    os::{fd::AsRawFd, unix::fs::MetadataExt},
    path::Path,
};
use xxhash_rust::xxh64::Xxh64;

use io_uring::{opcode, types::Fd, IoUring};

const BUF_SIZE: usize = 1024 * 3; // 3 KB

pub struct Image {
    pub path: Box<Path>,
}

impl Image {
    pub fn new(image: &ImageReference) -> Self {
        Self {
            path: Box::from(image.path),
        }
    }
}

pub struct ImageReference<'a> {
    pub path: &'a Path,
}

impl<'a> ImageReference<'a> {
    pub fn new(path: &'a Path) -> Self {
        Self { path: path }
    }

    pub fn get_file_size(&self) -> u64 {
        self.path
            .metadata()
            .expect("File exists and should have metadata")
            .size()
    }
}

pub struct ImageReader<H: Hasher + Default> {
    ring: IoUring,
    phantom: PhantomData<H>,
}

#[inline]
pub fn xxh_reader() -> io::Result<ImageReader<Xxh64>> {
    ImageReader::new()
}

#[inline]
pub fn ahash_reader() -> io::Result<ImageReader<AHasher>> {
    ImageReader::new()
}

impl<H: std::hash::Hasher + Default> ImageReader<H> {
    fn new() -> io::Result<Self> {
        Ok(Self {
            ring: IoUring::new(8)?,
            phantom: PhantomData,
        })
    }

    fn read_buffer(
        &mut self,
        fd: Fd,
        buffer: &mut [MaybeUninit<u8>],
        offset: u64,
    ) -> io::Result<usize> {
        let read = opcode::Read::new(fd, buffer.as_mut_ptr() as *mut u8, buffer.len() as u32)
            .offset(offset)
            .build()
            .user_data(0);

        unsafe {
            self.ring
                .submission()
                .push(&read)
                .expect("submission queue full");
        }

        self.ring.submit_and_wait(1)?;

        let cqe = self
            .ring
            .completion()
            .next()
            .expect("io_uring returned no completion");

        let result = cqe.result();

        if result < 0 {
            return Err(io::Error::from_raw_os_error(-result));
        }

        Ok(result as usize)
    }

    pub fn read_file_part(&mut self, path: &Path) -> io::Result<u64> {
        let file = File::open(path)?;

        let fd = Fd(file.as_raw_fd());

        let mut buffer = [MaybeUninit::<u8>::uninit(); BUF_SIZE];

        let mut hasher = H::default();

        let bytes_read = self.read_buffer(fd, &mut buffer, 0)?;

        if bytes_read != 0 {
            // SAFETY:
            //
            // io_uring was told to write into the buffer, and the kernel
            // reported that `bytes_read` bytes were successfully read.
            // Therefore the first `bytes_read` bytes are initialized.
            let initialized =
                unsafe { std::slice::from_raw_parts(buffer.as_ptr() as *const u8, bytes_read) };

            hasher.write(initialized);
        }

        Ok(hasher.finish())
    }

    pub fn read_file(&mut self, path: &Path) -> io::Result<u64> {
        let file = File::open(path)?;
        let fd = Fd(file.as_raw_fd());

        // Allocate the buffer without zero-initializing it.
        //
        // This buffer is reused for every read.
        let mut buffer = [MaybeUninit::<u8>::uninit(); BUF_SIZE];

        let mut hasher = H::default();
        let mut offset = 0u64;

        loop {
            let bytes_read = self.read_buffer(fd, &mut buffer, offset)?;

            if bytes_read == 0 {
                break;
            }

            // SAFETY:
            //
            // The kernel has initialized exactly `bytes_read` bytes through
            // the io_uring read operation. We only expose those bytes as
            // initialized `u8`s.
            let initialized =
                unsafe { std::slice::from_raw_parts(buffer.as_ptr() as *const u8, bytes_read) };

            hasher.write(initialized);

            offset += bytes_read as u64;

            // If fewer bytes than the buffer size were returned, we've
            // reached EOF.
            if bytes_read < BUF_SIZE {
                break;
            }
        }

        Ok(hasher.finish())
    }
}
