use crate::hasher::{hash_one_part, default_hasher, BUFFER};
use std::fs::File;
use std::io::{BufReader, Result};
use std::hash::{Hash, Hasher};
use std::cmp::PartialEq;
use std::path::Path;

#[derive(Debug)]
pub struct Image {
    pub hash: Option<u64>,
    pub partial_hash: u64,
    pub path: String
}

impl Image {
    pub fn new(path: &Path) -> Result<Image> {    
        Ok(Image {
            hash: None,
            partial_hash: Image::read_part(path)?,
            path: path.to_str().unwrap().to_owned()
        })
    }
    
    fn read_part(path: &Path) -> Result<u64> {
        let input = File::open(path)?;
        let reader = BufReader::with_capacity(BUFFER, input);
        Ok(hash_one_part(reader)?)
    }
    
    pub fn read_complete_hash(&mut self) -> Result<()> {
        if let Some(_) = &self.hash {
            return Ok(());
        }
        self.hash = Some(Image::read(&self.path)?);
        Ok(())
    }
    
    fn read(path: &str) -> Result<u64> {
        let input = File::open(path)?;
        let reader = BufReader::new(input);
        Ok(default_hasher(reader)?)
    }    
}

impl Hash for Image {
    fn hash<H: Hasher>(&self, _state: &mut H) {
    }
}

impl Eq for Image {}

impl PartialEq for Image {
    fn eq(&self, other: &Self) -> bool {
        self.hash == other.hash
    }
}
