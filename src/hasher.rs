use ahash::AHasher;
use std::hash::Hasher;
use std::io::{Read, Result};

pub const BUFFER: usize = 3024;

pub fn default_hasher<R: Read>(mut reader: R) -> Result<u64> {
    let mut hasher = AHasher::new_with_keys(1, 2);
    let mut buffer = [0; BUFFER * 2];

    loop {
        let count = reader.read(&mut buffer)?;

        if count == 0 {
            break;
        }

        hasher.write(&buffer[..count]);
    }

    Ok(hasher.finish())
}

pub fn hash_one_part<R: Read>(mut reader: R) -> Result<u64> {
    let mut hasher = AHasher::new_with_keys(1, 2);
    let mut buffer = [0; BUFFER];

    let count = reader.read(&mut buffer)?;
    hasher.write(&buffer[..count]);
    Ok(hasher.finish())
}
