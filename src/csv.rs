use crate::image::Image;
use std::fs::File;
use std::io::prelude::*;
use std::io::Result;

pub fn write_csv(duplicates: Vec<Image>) -> Result<()> {
    let mut f = File::create("duplicates.csv")?;
    f.write(b"root*duplicates\n")?;
    for ref v in duplicates {
        f.write(v.path.as_ref().to_str().unwrap().as_bytes())?;
        f.write(b"*")?;
        let paths: Vec<_> = v.duplicates.iter().map(|i| i.path.as_ref().to_str().unwrap().clone()).collect();

        f.write(paths.join(":").as_bytes())?;
        f.write(b"\n")?;
    }

    Ok(())
}
