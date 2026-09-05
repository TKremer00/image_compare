use std::{
    fs::{read_dir, DirEntry},
    io::Result,
};

mod duplication_pipeline;
mod hasher;
mod image;

// TODO: !!!!! Need to fix linked list, because now it reads random memory!
// hyperfine --warmup 3   './target/release/image_compare_old' './target/release/image_compare' '../image_compare_old/target/release/image_compare --path /mnt/hdd/_del/extHddData/anubis/output/0-20000/'
// https://github.com/jonhoo/inferno

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
