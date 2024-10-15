    public void test_consecutive_retries_will_not_result_in_duplicate_reads() throws Exception {
        Path tempFile = createTempFile("tempfile1", ".csv");
        List<String> lines = List.of("id", "1", "2", "3", "4", "5");
        Files.write(tempFile, lines);
        List<URI> fileUris = Stream.of(tempFile.toUri().toString())
            .map(FileReadingIterator::toURI).toList();

        Supplier<BatchIterator<LineCursor>> batchIteratorSupplier =
            () -> new FileReadingIterator(
                fileUris,
                null,
                Map.of(LocalFsFileInputFactory.NAME, new LocalFsFileInputFactory()),
                false,
                1,
                0,
                Settings.EMPTY,
                THREAD_POOL.scheduler()
            ) {
                int retry = 0;

                @Override
                BufferedReader createBufferedReader(InputStream inputStream) throws IOException {
                    return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                        private int currentLineNumber = 0;

                        @Override
                        public String readLine() throws IOException {
                            var line = super.readLine();
                            // current implementation does not handle SocketTimeoutException thrown when parsing header so skip it here as well.
                            if (currentLineNumber++ > 0 && retry++ < MAX_SOCKET_TIMEOUT_RETRIES) {
                                throw new SocketTimeoutException("dummy");
                            }
                            return line;
                        }
                    };
                }
            };

        var tester = new BatchIteratorTester<>(() -> batchIteratorSupplier.get().map(LineCursor::line));
        tester.verifyResultAndEdgeCaseBehaviour(lines);
    }