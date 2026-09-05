use std::{
    collections::{HashMap, LinkedList},
    io::Result,
};

use crate::{
    duplication_pipeline::{filter_images, iimage_pruner::IImagePruner},
    image::ImageReference,
};

#[derive(Default)]
pub(crate) struct LengthPruner;

impl LengthPruner {
    fn prune_internal<'a>(
        image: ImageReference<'a>,
        duplicate_images: &mut HashMap<u64, LinkedList<ImageReference<'a>>>,
    ) {
        let filesize = image.get_file_size();

        match duplicate_images.get_mut(&filesize) {
            Some(images) => images.push_back(image),
            None => {
                let mut images = LinkedList::new();
                images.push_back(image);
                _ = duplicate_images.insert(filesize, images);
            }
        }
    }
}

impl IImagePruner for LengthPruner {
    fn prune<'a>(
        self,
        simillar_images: Vec<LinkedList<ImageReference<'a>>>,
    ) -> Result<Vec<LinkedList<ImageReference<'a>>>> {
        let mut duplicate_images: HashMap<u64, LinkedList<ImageReference>> = HashMap::default();

        for images in simillar_images {
            for image in images {
                Self::prune_internal(image, &mut duplicate_images);
            }
        }

        Ok(duplicate_images
            .into_iter()
            .filter_map(filter_images)
            .collect())
    }

    fn prune_from_paths<'a>(
        self,
        paths: &'a [std::path::PathBuf],
    ) -> Result<Vec<LinkedList<ImageReference<'a>>>> {
        let mut duplicate_images: HashMap<u64, LinkedList<ImageReference>> = HashMap::default();

        for path in paths {
            let image = ImageReference::new(path);

            Self::prune_internal(image, &mut duplicate_images);
        }

        Ok(duplicate_images
            .into_iter()
            .filter_map(filter_images)
            .collect())
    }
}
