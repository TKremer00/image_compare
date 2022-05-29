use crate::image::Image;
use std::collections::BTreeMap;
use std::fs::File;
use std::io::prelude::*;
use std::io::Result;

pub fn write_csv(duplicates: BTreeMap<Image, Vec<Image>>) -> Result<()> {
    let mut f = File::create("duplicates.csv")?;
    f.write(b"root*duplicates\n")?;
    for (k, v) in duplicates {
        f.write(k.path.as_bytes())?;
        f.write(b"*")?;
        let paths: Vec<String> = v.iter().map(|i| i.path.clone()).collect();

        f.write(paths.join(":").as_bytes())?;
        f.write(b"\n")?;
    }

    Ok(())
}
