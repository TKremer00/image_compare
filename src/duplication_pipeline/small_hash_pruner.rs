use std::{
    collections::{HashMap, LinkedList},
    hash::Hasher,
    io::Result,
};

use crate::{
    duplication_pipeline::{filter_images, iimage_pruner::IImagePruner},
    hasher::FastHashMap,
    image::ImageReference,
    image_reader::ImageReader,
};

pub(crate) struct SmallHashPruner<'r, H: Hasher + Default>(&'r mut ImageReader<H>);

impl<'r, H: Hasher + Default> SmallHashPruner<'r, H> {
    pub fn new(reader: &'r mut ImageReader<H>) -> Self {
        Self(reader)
    }

    fn prune_internal<'a>(
        &mut self,
        image: ImageReference<'a>,
        duplicate_images: &mut FastHashMap<u64, LinkedList<ImageReference<'a>>>,
    ) -> Result<()> {
        let hash = self.0.read_file_part(image.path)?;

        match duplicate_images.get_mut(&hash) {
            Some(images) => images.push_back(image),
            None => {
                let mut images = LinkedList::new();
                images.push_back(image);
                _ = duplicate_images.insert(hash, images);
            }
        }

        Ok(())
    }
}

impl<'r, H: Hasher + Default> IImagePruner for SmallHashPruner<'r, H> {
    fn prune<'a>(
        mut self,
        simillar_images: Vec<LinkedList<ImageReference<'a>>>,
    ) -> Result<Vec<LinkedList<ImageReference<'a>>>> {
        let mut duplicate_images: FastHashMap<u64, LinkedList<ImageReference>> = HashMap::default();

        for images in simillar_images {
            for image in images {
                self.prune_internal(image, &mut duplicate_images)?;
            }
        }

        Ok(duplicate_images
            .into_iter()
            .filter_map(filter_images)
            .collect())
    }

    fn prune_from_paths<'a>(
        mut self,
        paths: &'a [std::path::PathBuf],
    ) -> Result<Vec<LinkedList<ImageReference<'a>>>> {
        let mut duplicate_images: FastHashMap<u64, LinkedList<ImageReference>> = HashMap::default();

        for path in paths {
            let image = ImageReference::new(path);

            self.prune_internal(image, &mut duplicate_images)?;
        }

        Ok(duplicate_images
            .into_iter()
            .filter_map(filter_images)
            .collect())
    }
}
