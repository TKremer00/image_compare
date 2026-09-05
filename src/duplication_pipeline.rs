use std::{
    collections::{HashMap, LinkedList},
    fs::DirEntry,
    hash::Hasher,
    io::Result,
    path::PathBuf,
};

use crate::{
    hasher::FastHashMap,
    image::{self, Image, ImageReader, ImageReference},
};

pub fn process_images(directory_entries: &[Result<DirEntry>]) -> Result<Vec<LinkedList<Image>>> {
    let mut image_reader = image::xxh_reader()?;

    let paths: Vec<PathBuf> = directory_entries
        .iter()
        .filter_map(|e| e.as_ref().ok().map(|e| e.path()))
        .collect();

    let images = small_hash(&mut image_reader, &paths)?;

    // Prune map
    let images: FastHashMap<u64, LinkedList<ImageReference>> = images
        .into_iter()
        .filter(move |(_, images)| !images.is_empty() && images.len() > 1)
        .collect();

    let images = full_hash(&mut image_reader, images)?;

    let images: Vec<LinkedList<Image>> = images
        .into_iter()
        .map(|(_, images)| images)
        .filter(move |images| !images.is_empty() && images.len() > 1)
        .map(|l| {
            l.iter()
                .map(|i| Image::new(i))
                .collect::<LinkedList<Image>>()
        })
        .collect();

    Ok(images)
}

fn small_hash<'a, H>(
    image_reader: &mut ImageReader<H>,
    paths: &'a [PathBuf],
) -> Result<FastHashMap<u64, LinkedList<ImageReference<'a>>>>
where
    H: Hasher + Default,
{
    let mut duplicate_images: FastHashMap<u64, LinkedList<ImageReference>> = HashMap::default();

    for path in paths {
        let small_hash = image_reader.read_file_part(path)?;
        let image = ImageReference::new(path);

        match duplicate_images.get_mut(&small_hash) {
            Some(images) => images.push_back(image),
            None => {
                let mut images = LinkedList::new();
                images.push_back(image);
                _ = duplicate_images.insert(small_hash, images);
            }
        }
    }

    Ok(duplicate_images)
}

fn full_hash<'a, H>(
    image_reader: &mut ImageReader<H>,
    images: FastHashMap<u64, LinkedList<ImageReference<'a>>>,
) -> Result<FastHashMap<u64, LinkedList<ImageReference<'a>>>>
where
    H: Hasher + Default,
{
    let mut duplicate_images: FastHashMap<u64, LinkedList<ImageReference>> = HashMap::default();

    for (_, images) in images {
        for image in images.into_iter() {
            let hash = image_reader.read_file(&image.path)?;

            match duplicate_images.get_mut(&hash) {
                Some(images) => images.push_back(image),
                None => {
                    let mut images = LinkedList::new();
                    images.push_back(image);
                    _ = duplicate_images.insert(hash, images);
                }
            }
        }
    }

    Ok(duplicate_images)
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
