use std::{os::unix::fs::MetadataExt, path::Path};

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
