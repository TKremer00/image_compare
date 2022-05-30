use crate::image::Image;
use std::collections::HashMap;
use std::fs::File;
use std::io::prelude::*;
use std::io::Result;

pub fn write_csv(duplicates: HashMap<Image, Vec<Image>>) -> Result<()> {
    let mut f = File::create("duplicates.csv")?;
    f.write(b"root*duplicates\n")?;
    for (ref k, ref v) in duplicates {
        f.write(k.path.as_bytes())?;
        f.write(b"*")?;
        let paths: Vec<String> = v.iter().map(|i| i.path.clone()).collect();

        f.write(paths.join(":").as_bytes())?;
        f.write(b"\n")?;
    }

    Ok(())
}
