use crate::hasher::{hash_reader, hash_one_part};
use data_encoding::HEXUPPER;
use std::fs::File;
use std::io::{BufReader, Result};
use std::borrow::Cow;
use std::hash::{Hash, Hasher};
use std::cmp::{Ordering, PartialEq};


#[derive(Debug, Ord)]
pub struct Image<'a> {
    pub hash: Option<Cow<'a, str>>,
    pub partial_hash: Cow<'a, str>,
    pub path: String
}

impl<'a> Image<'a> {
    pub fn new(path: String) -> Result<Image<'a>> {    
        Ok(Image {
            hash: None,
            partial_hash: Cow::from(Image::read_part(path.clone())?),
            path: path
        })
    }
    
    fn read_part(path: String) -> Result<String> {
        let input = File::open(path)?;
        let reader = BufReader::new(input);
        let digest = hash_one_part(reader)?;
        Ok(HEXUPPER.encode(digest.as_ref()))
    }
    
    pub fn read_complete_hash(&mut self) -> Result<()> {
        if let Some(_) = &self.hash {
            return Ok(());
        }
        self.hash = Some(Cow::from(Image::read(self.path.clone())?));
        Ok(())
    }
    
    fn read(path: String) -> Result<String> {
        let input = File::open(path)?;
        let reader = BufReader::new(input);
        let digest = hash_reader(reader)?;
        Ok(HEXUPPER.encode(digest.as_ref()))
    }    
}

impl Hash for Image<'_> {
    fn hash<H: Hasher>(&self, state: &mut H) {
        self.hash.hash(state);
    }
}

impl<'a> Eq for Image<'a> {}

impl<'a> PartialEq for Image<'a> {
    fn eq(&self, other: &Self) -> bool {
        self.hash == other.hash
    }
}

impl<'a> PartialOrd for Image<'a> {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}


