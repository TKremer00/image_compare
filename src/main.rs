use ahash::AHashMap;
use clap::Parser;
use kdam::prelude::*;
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
}

fn main() -> Result<()> {
    let args = Args::parse();

    let path = &args.path;
    let resp = dir_to_images(path);
    let _ = csv::write_csv(resp);

    Ok(())
}

fn dir_to_images(path: &str) -> Vec<Image> {
    let paths = read_dir(path).unwrap();
    let total_images = read_dir(path).unwrap().count();
    let mut pb = tqdm!(total = total_images);

    let mut images: Vec<AHashMap<u64, Image>> = vec![AHashMap::with_capacity(SIZE)];
    let mut index = 0;
    for path in paths {
        match path {
            Ok(path) => {
                proccess_image(&path.path(), &mut images);
                if (index + 1) % 100 == 0 {
                    pb.update(100);
                }
                index += 1;
            }
            Err(_) => {
                println!("No str path");
            }
        }
    }
    println!();

    let duplicates: Vec<Image> = images
        .into_iter()
        .flat_map(move |v| {
            let vec: Vec<Image> = v.into_iter().map(|(_,v)| v).filter(move |v| !v.is_empty()).collect();
            vec
        })
        .collect();
    duplicates
}

fn proccess_image(path: &Path, images: &mut Vec<AHashMap<u64, Image>>) {
    let mut image = Image::new(path).unwrap();
    for list in images.iter_mut() {
        if list.contains_key(&image.partial_hash) {
            let entry = if let Some(entry) = list.get_mut(&image.partial_hash) {
                entry
            } else {
                panic!("no entry");
            };
            let _ = entry.read_complete_hash();
            let _ = image.read_complete_hash();

            if entry.hash == image.hash {
                entry.add(image);
                return;
            }
        } 
    }
    
    if let Some(last) = images.last_mut() {
        if last.len() < SIZE {
            last.insert(image.partial_hash, image);
            return;
        }
    }

    let mut map = AHashMap::with_capacity(SIZE);
    map.insert(image.partial_hash, image);
    images.push(map);
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn all_match() {
        let path = "./test_files/all_match/";
        let resp = dir_to_images(path);
        assert!(!resp.is_empty());
        assert_eq!(resp.len(), 1);
        for v in resp {
            assert_eq!(v.duplicates.len(), 4);
        }
    }

    #[test]
    fn no_match() {
        let path = "./test_files/no_match/";
        let resp = dir_to_images(path);
        assert!(resp.is_empty());
        assert_eq!(resp.len(), 0);
    }

    #[test]
    fn pixel_diff() {
        let path = "./test_files/pixel_diff/";
        let resp = dir_to_images(path);
        assert!(resp.is_empty());
        assert_eq!(resp.len(), 0);
    }
}
