    public void test_file_collect_source_returns_iterator_that_can_skip_lines() throws Exception {
        List<String> targetColumns = List.of();
        List<Projection> projections = List.of();
        List<Symbol> toCollect = List.of(
            TestingHelpers.createReference("_raw", DataTypes.STRING)
        );
        Path tmpFile = createTempFile("tempfile1", ".csv");
        Files.write(tmpFile, List.of(
            "garbage1",
            "garbage2",
            "x,y",
            "1,2",
            "10,20"
        ));
        FileUriCollectPhase fileUriCollectPhase = new FileUriCollectPhase(
            UUID.randomUUID(),
            1,
            "copy from",
            List.of(),
            Literal.of(tmpFile.toUri().toString()),
            targetColumns,
            toCollect,
            projections,
            null,
            false,
            new CopyFromParserProperties(true, true, ',', 2),
            InputFormat.CSV,
            Settings.EMPTY
        );

        Roles roles = () -> List.of(Role.CRATE_USER);
        FileCollectSource fileCollectSource = new FileCollectSource(
            new NodeContext(new Functions(Map.of()), roles),
            clusterService,
            Map.of(),
            THREAD_POOL,
            () -> List.of(Role.CRATE_USER)
        );

        CompletableFuture<BatchIterator<Row>> iterator = fileCollectSource.getIterator(
            CoordinatorTxnCtx.systemTransactionContext(),
            fileUriCollectPhase,
            mock(CollectTask.class),
            false
        );
        assertThat(iterator).succeedsWithin(5, TimeUnit.SECONDS);
        CompletableFuture<List<Object>> resultFuture = iterator.join()
            .map(row -> row.get(0))
            .toList();

        assertThat(resultFuture).succeedsWithin(5, TimeUnit.SECONDS);
        assertThat(resultFuture.join()).containsExactly(
            "{\"x\":\"1\",\"y\":\"2\"}",
            "{\"x\":\"10\",\"y\":\"20\"}"
        );
    }