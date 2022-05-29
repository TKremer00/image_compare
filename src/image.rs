use crate::hasher::{hash_one_part, default_hasher};
use std::fs::File;
use std::io::{BufReader, Result};
use std::hash::{Hash, Hasher};
use std::cmp::PartialEq;


#[derive(Debug)]
pub struct Image {
    pub hash: Option<u64>,
    pub partial_hash: u64,
    pub path: String
}

impl Image {
    pub fn new(path: String) -> Result<Image> {    
        Ok(Image {
            hash: None,
            partial_hash: Image::read_part(path.clone())?,
            path: path
        })
    }
    
    fn read_part(path: String) -> Result<u64> {
        let input = File::open(path)?;
        let reader = BufReader::new(input);
        Ok(hash_one_part(reader)?)
    }
    
    pub fn read_complete_hash(&mut self) -> Result<()> {
        if let Some(_) = &self.hash {
            return Ok(());
        }
        self.hash = Some(Image::read(self.path.clone())?);
        Ok(())
    }
    
    
    fn read(path: String) -> Result<u64> {
        let input = File::open(path)?;
        let reader = BufReader::new(input);
        Ok(default_hasher(reader)?)
    }    
}

impl Hash for Image {
    fn hash<H: Hasher>(&self, state: &mut H) {
        self.hash.hash(state);
    }
}

impl Eq for Image {}

impl PartialEq for Image {
    fn eq(&self, other: &Self) -> bool {
        self.hash == other.hash
    }
}
