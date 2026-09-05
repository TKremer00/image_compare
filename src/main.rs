use clap::Parser;
use std::{
    fs::{read_dir, DirEntry},
    io::Result,
    path::PathBuf,
};

mod duplication_pipeline;
mod hasher;
mod image;
mod image_reader;

/// Program to determine multiple images with the same content.
#[derive(Parser, Debug)]
#[command(version, about, long_about = None)]
struct Args {
    /// The folder to search for images with the same content.
    #[arg(short, long)]
    folder: PathBuf,
}

fn main() -> Result<()> {
    let args = Args::parse();

    let directory_entries: Vec<Result<DirEntry>> = read_dir(args.folder)?.into_iter().collect();

    let duplicates = duplication_pipeline::process_images(&directory_entries)?;

    // TODO: Write to file
    for images in duplicates {
        for image in images {
            print!("\t{:?}", image.path);
        }
    }

    Ok(())
}
