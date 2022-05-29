use std::fs::read_dir;
use std::borrow::Cow;
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
    let mut images: HashMap<Cow<str>, (Image, Vec<Image>)> = HashMap::new();
    let paths = read_dir(path).unwrap();
    let mut images_done = 0;
    let last_time = SystemTime::now();
    for path in paths {
        match path.unwrap().path().to_str() {
            Some(str_path) => {
                proccess_image(str_path.to_string(), &mut images);
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

fn proccess_image(path : String, images: &mut HashMap<Cow<str>, (Image, Vec<Image>)>) {
    let mut image = Image::new(path).unwrap();

    if images.contains_key(&image.partial_hash) {
        let entry = if let Some(entry) = images.get_mut(&image.partial_hash) { entry } else { panic!("no entry"); };
        let _ = entry.0.read_complete_hash();
        let _ = image.read_complete_hash(); 
        if entry.0.hash == image.hash {
            entry.1.push(image);
        }
    } else {
        images.insert(image.partial_hash.clone(), (image, Vec::default()));
    }
}
