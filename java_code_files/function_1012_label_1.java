    public FileReadingIterator(Collection<String> fileUris,
                               String compression,
                               Map<String, FileInputFactory> fileInputFactories,
                               Boolean shared,
                               int numReaders,
                               int readerNumber,
                               Settings withClauseOptions,
                               ScheduledExecutorService scheduler) {
        this.compressed = compression != null && compression.equalsIgnoreCase("gzip");
        this.fileInputFactories = fileInputFactories;
        this.cursor = new LineCursor();
        this.shared = shared;
        this.numReaders = numReaders;
        this.readerNumber = readerNumber;
        this.scheduler = scheduler;
        this.backOffPolicy = BackoffPolicy.exponentialBackoff(TimeValue.ZERO, MAX_SOCKET_TIMEOUT_RETRIES).iterator();

        this.fileInputs = fileUris.stream()
            .map(uri -> toFileInput(uri, withClauseOptions))
            .filter(Objects::nonNull)
            .toList();
        fileInputsIterator = fileInputs.iterator();
    }