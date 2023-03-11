use crate::hasher::{default_hasher, hash_one_part, BUFFER};
use std::borrow::Cow;
use std::boxed::Box;
use std::cmp::PartialEq;
use std::fs::File;
use std::hash::{Hash, Hasher};
use std::io::{BufReader, Result};
use std::path::Path;
use std::vec::Vec;

#[derive(Debug, Clone)]
pub struct Image {
    pub hex: ImageHash,
    pub path: Box<Path>,
    pub duplicates: Vec<Image>,
}

impl Image {
    pub fn new(path: &Path) -> Result<Image> {
        Ok(Image {
            hex: ImageHash::new(&path)?,
            path: Box::from(path),
            duplicates: Vec::default(),
        })
    }

    #[inline]
    pub fn is_empty(&self) -> bool {
        self.duplicates.is_empty()
    }

    pub fn compare(&mut self, image: &mut Image) -> bool {
        let my_hash = if self.hex.has_hash() {
            self.hex.get_hash()
        } else {
            self.hex.get_and_set_hash(self.path.as_ref())
        };

        let other_hash = if image.hex.has_hash() {
            image.hex.get_hash()
        } else {
            image.hex.get_and_set_hash(image.path.as_ref())
        };

        my_hash == other_hash
    }

    #[inline]
    pub fn add(&mut self, image: Image) {
        self.duplicates.push(image);
    }
}

impl Hash for Image {
    #[inline]
    fn hash<H: Hasher>(&self, _state: &mut H) {}
}

impl Eq for Image {}

impl PartialEq for Image {
    #[inline]
    fn eq(&self, other: &Self) -> bool {
        self.hex.partial_hash == other.hex.partial_hash
    }
}

#[derive(Debug, Clone)]
pub struct ImageHash {
    pub partial_hash: Cow<'static, u64>,
    hash: Option<u64>,
}

impl ImageHash {
    pub fn new(path: &Path) -> Result<ImageHash> {
        Ok(ImageHash {
            partial_hash: Cow::Owned(ImageHash::read_partial(path)?),
            hash: None,
        })
    }

    #[inline]
    pub fn has_hash(&self) -> bool {
        self.hash.is_some()
    }

    #[inline]
    pub fn get_hash(&self) -> &Option<u64> {
        &self.hash
    }

    #[inline]
    pub fn get_and_set_hash(&mut self, path: &Path) -> &Option<u64> {
        if self.hash.is_none() {
            self.hash = Some(ImageHash::read(path).unwrap());
        }
        &self.hash
    }

    fn read_partial(path: &Path) -> Result<u64> {
        let input = File::open(path)?;
        let reader = BufReader::with_capacity(BUFFER, input);
        Ok(hash_one_part(reader)?)
    }

    fn read(path: &Path) -> Result<u64> {
        let input = File::open(path)?;
        let reader = BufReader::new(input);
        Ok(default_hasher(reader)?)
    }
}

impl Hash for ImageHash {
    #[inline]
    fn hash<H: Hasher>(&self, _state: &mut H) {}
}

impl Eq for ImageHash {}

impl PartialEq for ImageHash {
    #[inline]
    fn eq(&self, other: &Self) -> bool {
        self.partial_hash == other.partial_hash
    }
}
