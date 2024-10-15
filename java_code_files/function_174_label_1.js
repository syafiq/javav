    void build() throws Exception
    {
        DocumentReference documentReference = new DocumentReference("wiki", Arrays.asList("Path", "To"), "Page");
        when(this.entityReferenceSerializer.serialize(documentReference)).thenReturn("wiki:Path.To.Page");

        DocumentModelBridge document = mock(DocumentModelBridge.class);
        DocumentAccessBridge dab = this.componentManager.getInstance(DocumentAccessBridge.class);
        when(dab.getTranslatedDocumentInstance(documentReference)).thenReturn(document);
        when(document.getSyntax()).thenReturn(Syntax.XWIKI_2_1);

        InputStream officeFileStream = new ByteArrayInputStream("Presentation content".getBytes());

        OfficeConverterResult officeConverterResult = mock(OfficeConverterResult.class);
        when(officeConverterResult.getOutputDirectory()).thenReturn(this.outputDirectory);
        // Slide numbers including 10 so that we can validate that XWIKI-19565 has been fixed.
        List<Integer> slideNumbers = Arrays.asList(0, 1, 2, 10);
        File firstSlide = new File(this.outputDirectory, "img0.html");
        when(officeConverterResult.getOutputFile()).thenReturn(firstSlide);

        // list of files usually created by jodconverter for a presentation
        Set<File> allFiles = new HashSet<>();
        slideNumbers.forEach(slideNumber -> {
            allFiles.add(new File(this.outputDirectory, String.format("img%d.html", slideNumber)));
            allFiles.add(new File(this.outputDirectory, String.format("text%d.html", slideNumber)));
            allFiles.add(new File(this.outputDirectory, String.format("img%d.jpg", slideNumber)));
        });

        // We create the files since some IO operations will happen on them
        for (File file : allFiles) {
            Files.createFile(file.toPath());
        }

        when(officeConverterResult.getAllFiles()).thenReturn(allFiles);

        when(this.officeConverter.convertDocument(Collections.singletonMap("file.odp", officeFileStream), "file.odp",
            "img0.html")).thenReturn(officeConverterResult);

        HTMLCleanerConfiguration config = mock(HTMLCleanerConfiguration.class);
        when(this.officeHTMLCleaner.getDefaultConfiguration()).thenReturn(config);

        Document xhtmlDoc = XMLUtils.createDOMDocument();
        xhtmlDoc.appendChild(xhtmlDoc.createElement("html"));
        String presentationHTML = slideNumbers.stream().map(slideNumber ->
            String.format("<p><img src=\"file-slide%d.jpg\"/></p>", slideNumber)).collect(Collectors.joining());
        when(this.officeHTMLCleaner.clean(any(Reader.class), eq(config)))
            .then(returnMatchingDocument(presentationHTML, xhtmlDoc));

        XDOM galleryContent = new XDOM(Collections.<Block>emptyList());
        when(this.xhtmlParser.parse(any(Reader.class))).thenReturn(galleryContent);

        XDOMOfficeDocument result = this.presentationBuilder.build(officeFileStream, "file.odp", documentReference);

        verify(config).setParameters(Collections.singletonMap("targetDocument", "wiki:Path.To.Page"));
        Set<File> expectedArtifacts = slideNumbers.stream().map(slideNumber ->
            new File(this.outputDirectory, String.format("file-slide%d.jpg", slideNumber))).collect(Collectors.toSet());
        assertEquals(expectedArtifacts, result.getArtifactsFiles());

        assertEquals("wiki:Path.To.Page", result.getContentDocument().getMetaData().getMetaData(MetaData.BASE));

        List<ExpandedMacroBlock> macros =
            result.getContentDocument().getBlocks(new ClassBlockMatcher(ExpandedMacroBlock.class), Block.Axes.CHILD);
        assertEquals(1, macros.size());
        assertEquals("gallery", macros.get(0).getId());
        assertEquals(galleryContent, macros.get(0).getChildren().get(0));
    }