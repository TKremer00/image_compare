use std::hash::{BuildHasherDefault, Hasher};

pub type FastHashMap<K, V> = std::collections::HashMap<K, V, BuildHasherDefault<IdentityHasher>>;

#[derive(Default)]
pub struct IdentityHasher {
    value: u64,
}

impl Hasher for IdentityHasher {
    #[inline]
    fn write(&mut self, bytes: &[u8]) {
        debug_assert_eq!(bytes.len(), 8);

        let bytes: [u8; 8] = bytes
            .try_into()
            .expect("IdentityHasher::write expected 8 bytes");

        self.value = u64::from_ne_bytes(bytes);
    }

    #[inline]
    fn write_u64(&mut self, value: u64) {
        self.value = value;
    }

    #[inline]
    fn finish(&self) -> u64 {
        self.value
    }
}
