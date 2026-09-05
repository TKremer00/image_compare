use std::{collections::LinkedList, fs::DirEntry, io::Result, path::PathBuf};

use crate::{
    duplication_pipeline::{
        full_hash_pruner::FullHashPruner, iimage_pruner::IImagePruner, length_pruner::LengthPruner,
        small_hash_pruner::SmallHashPruner,
    },
    image::{Image, ImageReference},
    image_reader::xxh_reader,
};

mod full_hash_pruner;
mod iimage_pruner;
mod length_pruner;
mod small_hash_pruner;

pub fn process_images(directory_entries: &[Result<DirEntry>]) -> Result<Vec<LinkedList<Image>>> {
    let mut image_reader = xxh_reader()?;

    let paths: Vec<PathBuf> = directory_entries
        .iter()
        .filter_map(|e| e.as_ref().ok().map(|e| e.path()))
        .collect();

    let small_hash_pruner = SmallHashPruner::new(&mut image_reader);
    let images = small_hash_pruner.prune_from_paths(&paths)?;

    let length_pruner = LengthPruner::default();
    let images = length_pruner.prune(images)?;

    let full_hash_pruner = FullHashPruner::new(&mut image_reader);
    let images = full_hash_pruner.prune(images)?;

    let images: Vec<LinkedList<Image>> = images
        .into_iter()
        .filter_map(move |images| {
            if images.len() > 1 {
                Some(images.iter().map(Image::new).collect::<LinkedList<Image>>())
            } else {
                None
            }
        })
        .collect();

    Ok(images)
}

#[inline]
pub(crate) fn filter_images(
    element: (u64, LinkedList<ImageReference>),
) -> Option<LinkedList<ImageReference>> {
    if element.1.len() > 1 {
        Some(element.1)
    } else {
        None
    }
}

#[cfg(test)]
mod tests {
    use std::fs::read_dir;

    use super::*;

    fn read_path(path: &str) -> Vec<Result<DirEntry>> {
        read_dir(path)
            .expect("We are testing, this should exist")
            .into_iter()
            .collect()
    }

    #[test]
    fn all_match() {
        let images = read_path("./test_files/all_match/");
        let resp = process_images(&images).expect("Test should be valid");

        assert!(!resp.is_empty());
        assert_eq!(resp.len(), 1);

        for v in resp {
            assert_eq!(v.len(), 5);
        }
    }

    #[test]
    fn no_match() {
        let images = read_path("./test_files/no_match/");
        let resp = process_images(&images).expect("Test should be valid");
        assert!(resp.is_empty());
        assert_eq!(resp.len(), 0);
    }

    #[test]
    fn pixel_diff() {
        let images = read_path("./test_files/pixel_diff/");
        let resp = process_images(&images).expect("Test should be valid");

        assert!(resp.is_empty());
        assert_eq!(resp.len(), 0);
    }
}
