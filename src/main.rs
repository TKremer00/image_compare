use std::fs::read_dir;
use std::io::Result;
use std::collections::HashMap;
use clap::Parser;
use std::path::Path;

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

fn dir_to_images(path: &str) -> HashMap<Image, Vec<Image>> {
    let paths = read_dir(path).unwrap();
    let total_images = read_dir(path).unwrap().count();
    let mut images: HashMap<u64, (Image, Vec<Image>)> = HashMap::with_capacity(total_images / 2);
    let mut images_done = 0;
    for path in paths {
        match path {
            Ok(path) => {
                proccess_image(&path.path(), &mut images);
                images_done += 1;
                if images_done % 100 == 0 {
                    println!("{} / {}", images_done, total_images);
                }
            },
            Err(_) => {
                println!("No str path");
            }
        }
    }
    println!("{} / {}", images_done, total_images);
    let duplicates : HashMap<Image, Vec<Image>> = images.into_iter().map(move |(_,v)| {
        (v.0, v.1)
    }).filter(|(_, v)| {
        v.len() > 0            
    }).collect();
    
    duplicates
}

fn proccess_image(path: &Path, images: &mut HashMap<u64, (Image, Vec<Image>)>) {
    let mut image = Image::new(path).unwrap();

    if images.contains_key(&image.partial_hash) {
        let entry = if let Some(entry) = images.get_mut(&image.partial_hash) { entry } else { panic!("no entry"); };
        let _ = entry.0.read_complete_hash();
        let _ = image.read_complete_hash();

        if entry.0.hash == image.hash {
            entry.1.push(image);
        }
    } else {
        images.insert(image.partial_hash, (image, Vec::default()));
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
        for (_, v) in resp {
            assert_eq!(v.len(), 4);
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