    public void test_iterator_closes_current_reader_on_io_error() throws Exception {
        Path tempFile1 = createTempFile("tempfile1", ".csv");
        List<String> lines1 = List.of(
            "name,id,age",
            "Arthur,4,38",
            "Douglas,6,42"  // <--- reader will fail on this line, so it is not part of the expected results
        );
        Files.write(tempFile1, lines1);
        Path tempFile2 = createTempFile("tempfile2", ".csv");
        List<String> lines2 = List.of("name,id,age", "Trillian,5,33");
        Files.write(tempFile2, lines2);
        List<String> fileUris = List.of(tempFile1.toUri().toString(), tempFile2.toUri().toString());

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

                @Override
                BufferedReader createBufferedReader(InputStream inputStream) throws IOException {
                    return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                        private int currentLineNumber = 0;

                        @Override
                        public String readLine() throws IOException {
                            var line = super.readLine();
                            currentLineNumber++;
                            if (line != null && currentLineNumber > 2) {      // fail on 3rd line, succeed on header and first row
                                throw new IOException("dummy");
                            }
                            return line;
                        }
                    };
                }
            };

        List<String> expectedResult = Arrays.asList(
            "name,id,age",
            "Arthur,4,38",
            "name,id,age",
            "Trillian,5,33"
        );
        var tester = new BatchIteratorTester<>(() -> batchIteratorSupplier.get().map(LineCursor::line));
        tester.verifyResultAndEdgeCaseBehaviour(expectedResult);
    }