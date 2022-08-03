use std::io::{Read, Result};
use ahash::AHasher;
use std::hash::Hasher;

pub const BUFFER: usize = 1024;

pub fn default_hasher<R: Read>(mut reader: R) -> Result<u64> {
    let mut hasher = AHasher::new_with_keys(1,2);
    let mut buffer = [0; BUFFER * 3];
    
    loop {
        let count = read_buffer(&mut buffer, &mut reader)?;
        
        if count == 0 {
            break;
        }

        hasher.write(&buffer[..count]);
    }
    
    Ok(hasher.finish())
}

pub fn hash_one_part<R: Read>(mut reader: R) -> Result<u64> {
    let mut hasher = AHasher::new_with_keys(1,2);
    let mut buffer = [0; BUFFER];
    
    let count = read_buffer(&mut buffer, &mut reader)?;
    hasher.write(&buffer[..count]);
    Ok(hasher.finish())
}

fn read_buffer<R: Read>(buf: &mut [u8], reader: &mut R) -> Result<usize> {
    reader.read(buf)
}
