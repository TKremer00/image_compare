use std::fs::read_dir;
use std::io::Result;
use std::collections::HashMap;
use std::time::SystemTime;

mod image;
mod hasher;
mod csv;

use image::Image;

fn main() -> Result<()> {
    let path = "/home/tim/Documents/code/image_compare/resources/";
    let resp = dir_to_images(path);
    let _ = csv::write_csv(resp);       
    
    Ok(())
}

fn dir_to_images(path: &str) -> HashMap<Image, Vec<Image>> {
    let mut images: HashMap<u64, (Image, Vec<Image>)> = HashMap::new();
    let paths = read_dir(path).unwrap();
    let mut images_done = 0;
    let last_time = SystemTime::now();
    for path in paths {
        match path.unwrap().path().to_str() {
            Some(str_path) => {
                proccess_image(str_path, &mut images);
                if images_done % 25 == 0 {
                    println!("{} - {}", images_done, last_time.elapsed().unwrap().as_secs());
                }
                
                images_done += 1;
            },
            None => {
                println!("No str path");
            }
        }
    }
    
    let duplicates : HashMap<Image, Vec<Image>> = images.into_iter().map(move |(_,v)| {
        (v.0, v.1)
    }).filter(|(_, v)| {
        v.len() > 0            
    }).collect();
    
    duplicates
}

fn proccess_image(path: &str, images: &mut HashMap<u64, (Image, Vec<Image>)>) {
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