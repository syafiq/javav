    void xhtmlOfficeDocumentBuilding() throws Exception
    {
        DocumentReference documentReference = new DocumentReference("wiki", Arrays.asList("Path", "To"), "Page");
        when(this.entityReferenceSerializer.serialize(documentReference)).thenReturn("wiki:Path.To.Page");

        InputStream officeFileStream = new ByteArrayInputStream("office content".getBytes());

        OfficeConverterResult converterResult = mock(OfficeConverterResult.class);
        when(converterResult.getOutputDirectory()).thenReturn(this.outputDirectory);
        File outputFile = new File(this.outputDirectory, "file.html");
        when(converterResult.getOutputFile()).thenReturn(outputFile);
        Set<File> allFiles = new HashSet<>();
        allFiles.add(outputFile);
        File otherArtifact = new File(this.outputDirectory, "file.txt");
        allFiles.add(otherArtifact);

        for (File file : allFiles) {
            Files.createFile(file.toPath());
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            IOUtils.write("HTML content".getBytes(), fos);
        }
        String otherArtifactContent = "Other content";
        try (FileOutputStream fos = new FileOutputStream(otherArtifact)) {
            IOUtils.write(otherArtifactContent.getBytes(), fos);
        }

        when(converterResult.getAllFiles()).thenReturn(allFiles);

        when(this.officeConverter.convertDocument(Collections.singletonMap("input.odt", officeFileStream), "input.odt",
            "output.html")).thenReturn(converterResult);

        byte[] imageContent = "Image content".getBytes();
        Map<String, byte[]> embeddedImages = Collections.singletonMap("image.png", imageContent);
        Document xhtmlDoc = mock(Document.class);
        when(xhtmlDoc.getUserData("embeddedImages")).thenReturn(embeddedImages);

        HTMLCleanerConfiguration config = mock(HTMLCleanerConfiguration.class);
        when(this.officeHTMLCleaner.getDefaultConfiguration()).thenReturn(config);
        when(this.officeHTMLCleaner.clean(any(Reader.class), eq(config))).thenReturn(xhtmlDoc);

        XHTMLOfficeDocument result =
            this.officeDocumentBuilder.build(officeFileStream, "file.odt", documentReference, true);

        Map<String, String> params = new HashMap<String, String>();
        params.put("targetDocument", "wiki:Path.To.Page");
        params.put("attachEmbeddedImages", "true");
        params.put("filterStyles", "strict");
        params.put("replaceImagePrefix", "output_html_");
        params.put("replacementImagePrefix", "file_");
        verify(config).setParameters(params);

        assertEquals(xhtmlDoc, result.getContentDocument());

        Map<String, OfficeDocumentArtifact> actualArtifacts = result.getArtifactsMap();
        OfficeDocumentArtifact actualOtherArtifact = actualArtifacts.get("file.txt");
        assertEquals(otherArtifactContent,
            IOUtils.toString(actualOtherArtifact.getContentInputStream(), StandardCharsets.UTF_8));
        assertTrue(actualArtifacts.containsKey("image.png"));
        OfficeDocumentArtifact imageArtifact = actualArtifacts.get("image.png");
        assertArrayEquals(imageContent, IOUtils.toByteArray(imageArtifact.getContentInputStream()));
    }