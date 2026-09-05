use std::{collections::LinkedList, io, path::PathBuf};

use crate::image::ImageReference;

pub(crate) trait IImagePruner {
    fn prune<'a>(
        self,
        simillar_images: Vec<LinkedList<ImageReference<'a>>>,
    ) -> io::Result<Vec<LinkedList<ImageReference<'a>>>>;

    fn prune_from_paths<'a>(
        self,
        paths: &'a [PathBuf],
    ) -> io::Result<Vec<LinkedList<ImageReference<'a>>>>;
}
