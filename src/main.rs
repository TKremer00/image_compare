use std::fs::read_dir;
use std::io::Result;
use clap::Parser;
use std::path::Path;
use ahash::AHashMap;
use kdam::prelude::*;

mod image;
mod hasher;
mod csv;

use image::Image;

#[derive(Parser, Debug)]
#[clap(author, version)]
struct Args {
    /// Path to the folder
    #[clap(short, long)]
    path : String,
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
    let mut images: AHashMap<u64, Image> = AHashMap::with_capacity(total_images / 4 * 3);
    for path in paths {
        match path {
            Ok(path) => {
                proccess_image(&path.path(), &mut images);
                pb.update(1);
            },
            Err(_) => {
                println!("No str path");
            }
        }
    }
    println!();
    let duplicates : Vec<Image> = images.into_iter().map(move |(_,v)| {
        v
    }).filter(|v| {
        !v.is_empty()            
    }).collect();
    
    duplicates
}

fn proccess_image(path: &Path, images: &mut AHashMap<u64, Image>) {
    let mut image = Image::new(path).unwrap();

    if images.contains_key(&image.partial_hash) {
        let entry = if let Some(entry) = images.get_mut(&image.partial_hash) { entry } else { panic!("no entry"); };
        let _ = entry.read_complete_hash();
        let _ = image.read_complete_hash();

        if entry.hash == image.hash {
            entry.add(image);
        }
    } else {
        images.insert(image.partial_hash, image);
    }
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