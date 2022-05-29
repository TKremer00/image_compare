use std::io::{Read, Result};
use md5::{compute, Context, Digest};

pub fn hash_reader<R: Read>(mut reader: R) -> Result<Digest> {
    let mut context = Context::new();
    let mut buffer = [0; 2024];

    loop {
        let count = read_buffer(&mut buffer, &mut reader)?;
        
        if count == 0 {
            break;
        }
        context.consume(&buffer[..count]);
    }
    Ok(context.compute())
}

pub fn hash_one_part<R: Read>(mut reader: R) -> Result<Digest> {
    let mut buffer = [0; 2024];

    let _ = read_buffer(&mut buffer, &mut reader)?;
    Ok(compute(buffer))
}

fn read_buffer<R: Read>(buf: &mut [u8], reader: &mut R) -> Result<usize> {
    return reader.read(buf);
}
