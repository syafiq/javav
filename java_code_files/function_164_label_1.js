    public void xdomOfficeDocumentBuilding() throws Exception
    {
        // Create & register a mock document converter to by-pass the office server.
        final InputStream mockOfficeFileStream = new ByteArrayInputStream(new byte[1024]);
        final Map<String, InputStream> mockInput = new HashMap<String, InputStream>();
        mockInput.put(INPUT_FILE_NAME, mockOfficeFileStream);
        OfficeConverterResult converterResult = mock(OfficeConverterResult.class);
        when(converterResult.getOutputDirectory()).thenReturn(this.outputDirectory);
        File outputFile = new File(this.outputDirectory, OUTPUT_FILE_NAME);
        when(converterResult.getOutputFile()).thenReturn(outputFile);
        Files.createFile(outputFile.toPath());
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            IOUtils.write(
                "<html><head><title></tile></head><body><p><strong>Hello There</strong></p></body></html>".getBytes(),
                fos);
        }

        final OfficeConverter mockDocumentConverter = mock(OfficeConverter.class);
        final DocumentReference documentReference = new DocumentReference("xwiki", "Main", "Test");

        when(mockOfficeServer.getConverter()).thenReturn(mockDocumentConverter);
        when(mockDocumentConverter.convertDocument(mockInput, INPUT_FILE_NAME, OUTPUT_FILE_NAME))
            .thenReturn(converterResult);
        when(mockDocumentReferenceResolver.resolve("xwiki:Main.Test")).thenReturn(documentReference);
        when(mockDefaultStringEntityReferenceSerializer.serialize(documentReference)).thenReturn("xwiki:Main.Test");

        XDOMOfficeDocument document =
            xdomOfficeDocumentBuilder.build(mockOfficeFileStream, INPUT_FILE_NAME, documentReference, true);
        assertEquals("xwiki:Main.Test", document.getContentDocument().getMetaData().getMetaData(MetaData.BASE));
        assertEquals("**Hello There**", document.getContentAsString());
        assertEquals(0, document.getArtifactsFiles().size());

        verify(mockOfficeServer).getConverter();
    }