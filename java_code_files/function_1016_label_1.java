    private FileReadingIterator createBatchIterator(S3ObjectInputStream inputStream, String ... fileUris) {
        String compression = null;
        return new FileReadingIterator(
            Arrays.asList(fileUris),
            compression,
            Map.of(
                S3FileInputFactory.NAME,
                (uri, withClauseOptions) -> new S3FileInput(new S3ClientHelper() {
                    @Override
                    protected AmazonS3 initClient(String accessKey, String secretKey, String endpoint, String protocol) {
                        AmazonS3 client = mock(AmazonS3Client.class);
                        ObjectListing objectListing = mock(ObjectListing.class);
                        S3ObjectSummary summary = mock(S3ObjectSummary.class);
                        S3Object s3Object = mock(S3Object.class);
                        when(client.listObjects(anyString(), anyString())).thenReturn(objectListing);
                        when(objectListing.getObjectSummaries()).thenReturn(Collections.singletonList(summary));
                        when(summary.getKey()).thenReturn("foo");
                        when(client.getObject("fakebucket", "foo")).thenReturn(s3Object);
                        when(s3Object.getObjectContent()).thenReturn(inputStream);
                        when(client.listNextBatchOfObjects(any(ObjectListing.class))).thenReturn(objectListing);
                        when(objectListing.isTruncated()).thenReturn(false);
                        return client;
                    }
                }, uri, "https")),
            false,
            1,
            0,
            Settings.EMPTY,
            THREAD_POOL.scheduler());
    }