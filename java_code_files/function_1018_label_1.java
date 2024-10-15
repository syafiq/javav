    public void test_retry_from_one_uri_does_not_affect_reading_next_uri() throws Exception {
        Path tempFile = createTempFile("tempfile1", ".csv");
        Files.write(tempFile, List.of("1", "2", "3"));
        Path tempFile2 = createTempFile("tempfile2", ".csv");
        Files.write(tempFile2, List.of("4", "5", "6"));
        List<String> fileUris = List.of(tempFile.toUri().toString(), tempFile2.toUri().toString());

        var fi = new FileReadingIterator(
            fileUris,
            null,
            Map.of(LocalFsFileInputFactory.NAME, new LocalFsFileInputFactory()),
            false,
            1,
            0,
            Settings.EMPTY,
            THREAD_POOL.scheduler()
        ) {
            private boolean isThrownOnce = false;
            final int lineToThrow = 2;

            @Override
            BufferedReader createBufferedReader(InputStream inputStream) throws IOException {
                return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                    private int currentLineNumber = 0;
                    @Override
                    public String readLine() throws IOException {
                        var line = super.readLine();
                        if (!isThrownOnce && currentLineNumber++ == lineToThrow) {
                            isThrownOnce = true;
                            throw new SocketTimeoutException("dummy");
                        }
                        return line;
                    }
                };
            }
        };

        assertThat(fi.moveNext()).isEqualTo(true);
        assertThat(fi.currentElement().line()).isEqualTo("1");
        assertThat(fi.moveNext()).isEqualTo(true);
        assertThat(fi.currentElement().line()).isEqualTo("2");
        assertThat(fi.moveNext()).isEqualTo(false);
        assertThat(fi.allLoaded()).isEqualTo(false);
        assertThat(fi.loadNextBatch()).succeedsWithin(5, TimeUnit.SECONDS)
            .satisfies(x -> {
                assertThat(fi.currentElement().line()).isEqualTo("2");
                assertThat(fi.watermark).isEqualTo(3);
                assertThat(fi.moveNext()).isEqualTo(true);
                // the watermark helped 'fi' to recover the state right before the exception then cleared
                assertThat(fi.watermark).isEqualTo(0);
                assertThat(fi.currentElement().line()).isEqualTo("3");

                // verify the exception did not prevent reading the next URI
                assertThat(fi.moveNext()).isEqualTo(true);
                assertThat(fi.currentElement().line()).isEqualTo("4");
            });
    }