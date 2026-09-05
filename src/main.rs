use std::{
    fs::{read_dir, DirEntry},
    io::Result,
};

mod duplication_pipeline;
mod hasher;
mod image;
mod image_reader;

fn main() -> Result<()> {
    let directory_entries: Vec<Result<DirEntry>> =
        read_dir("/mnt/hdd/_del/extHddData/anubis/output/0-20000/")?
            .into_iter()
            .collect();

    let duplicates = duplication_pipeline::process_images(&directory_entries)?;

    for images in duplicates {
        for image in images {
            print!("\t{:?}", image.path);
        }
    }

    Ok(())
}
