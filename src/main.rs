use ahash::AHashMap;
use clap::Parser;
use kdam::prelude::*;
use std::borrow::Cow;
use std::fs::read_dir;
use std::io::Result;
use std::path::Path;

mod csv;
mod hasher;
mod image;

use image::Image;

const SIZE: usize = 300;

#[derive(Parser, Debug)]
#[clap(author, version)]
struct Args {
    /// Path to the folder
    #[clap(short, long)]
    path: String,
    // TODO: set update frequency as a argument, default 100
}

fn main() -> Result<()> {
    let args = Args::parse();

    let path = &args.path;
    let total_images = read_dir(path).unwrap().count();
    let resp = dir_to_images(path, total_images);
    let _ = csv::write_csv(resp);

    Ok(())
}

fn dir_to_images(path: &str, total_images: usize) -> Vec<Image> {
    let paths = read_dir(path).unwrap();
    let mut pb = tqdm!(total = total_images);

    let mut images: AHashMap<Cow<u64>, Vec<Image>> = AHashMap::with_capacity(SIZE);
    let mut index = 0;
    for path in paths {
        proccess_image(&path.unwrap().path(), &mut images);
        if (index + 1) % 100 == 0 {
            pb.update(100);
        }
        index += 1;
    }
    println!();

    let duplicates: Vec<Image> = images
        .into_iter()
        .flat_map(move |(_, image)| image)
        .filter(move |image| !image.is_empty())
        .collect();
    duplicates
}

fn proccess_image(path: &Path, images: &mut AHashMap<Cow<u64>, Vec<Image>>) {
    let mut new_image = Image::new(path).unwrap();

    if let Some(images) = images.get_mut(&new_image.hex.partial_hash) {
        // does the partial hash match?
        for image in images.iter_mut() {
            if image.compare(&mut new_image) {
                // does the full hash match
                image.add(new_image); // add to the root_image of this full hash
                return;
            }
        }

        images.push(new_image); // add to the partial hash, this image has no root
        return;
    }

    images.insert(new_image.hex.partial_hash.clone(), vec![new_image]);
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn all_match() {
        let path = "./test_files/all_match/";
        let resp = dir_to_images(path, 0);
        assert!(!resp.is_empty());
        assert_eq!(resp.len(), 1);
        for v in resp {
            assert_eq!(v.duplicates.len(), 4);
        }
    }

    #[test]
    fn no_match() {
        let path = "./test_files/no_match/";
        let resp = dir_to_images(path, 0);
        assert!(resp.is_empty());
        assert_eq!(resp.len(), 0);
    }

    #[test]
    fn pixel_diff() {
        let path = "./test_files/pixel_diff/";
        let resp = dir_to_images(path, 0);
        assert!(resp.is_empty());
        assert_eq!(resp.len(), 0);
    }
}
