use std::io::{Read, Result};
use std::collections::hash_map::DefaultHasher;
use std::hash::Hasher;

pub fn default_hasher<R: Read>(mut reader: R) -> Result<u64> {
    let mut hasher = DefaultHasher::new();
    let mut buffer = [0; 2024];
    
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
    let mut hasher = DefaultHasher::new();
    let mut buffer = [0; 2024];
    
    let count = read_buffer(&mut buffer, &mut reader)?;
    hasher.write(&buffer[..count]);
    Ok(hasher.finish())
}

fn read_buffer<R: Read>(buf: &mut [u8], reader: &mut R) -> Result<usize> {
    return reader.read(buf);
}