    public void testFileUriCollect() throws Exception {
        FileCollectSource fileCollectSource = new FileCollectSource(
            createNodeContext(),
            clusterService,
            Collections.emptyMap(),
            THREAD_POOL,
            () -> List.of(Role.CRATE_USER)
            );

        File tmpFile = temporaryFolder.newFile("fileUriCollectOperation.json");
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tmpFile), StandardCharsets.UTF_8)) {
            writer.write("{\"name\": \"Arthur\", \"id\": 4, \"details\": {\"age\": 38}}\n");
            writer.write("{\"id\": 5, \"name\": \"Trillian\", \"details\": {\"age\": 33}}\n");
        }

        FileUriCollectPhase collectNode = new FileUriCollectPhase(
            UUID.randomUUID(),
            0,
            "test",
            Collections.singletonList("noop_id"),
            Literal.of(Paths.get(tmpFile.toURI()).toUri().toString()),
            List.of("a", "b"),
            Arrays.asList(
                createReference("name", DataTypes.STRING),
                createReference(new ColumnIdent("details", "age"), DataTypes.INTEGER)
            ),
            Collections.emptyList(),
            null,
            false,
            CopyFromParserProperties.DEFAULT,
            FileUriCollectPhase.InputFormat.JSON,
            Settings.EMPTY
        );
        TestingRowConsumer consumer = new TestingRowConsumer();
        CollectTask collectTask = mock(CollectTask.class);
        BatchIterator<Row> iterator = fileCollectSource.getIterator(
            CoordinatorTxnCtx.systemTransactionContext(), collectNode, collectTask, false).get(5, TimeUnit.SECONDS);
        consumer.accept(iterator, null);
        assertThat(new CollectionBucket(consumer.getResult()), contains(
            isRow("Arthur", 38),
            isRow("Trillian", 33)
        ));
    }