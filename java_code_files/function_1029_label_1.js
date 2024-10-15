    private static FileReadingIterator it(Collection<String> fileUris, String compression) {
        return new FileReadingIterator(
            fileUris,
            compression,
            Map.of(LocalFsFileInputFactory.NAME, new LocalFsFileInputFactory()),
            false,
            1,
            0,
            Settings.EMPTY,
            THREAD_POOL.scheduler());
    }