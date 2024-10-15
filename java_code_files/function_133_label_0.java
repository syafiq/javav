    void viewPresentation(MockitoComponentManager componentManager) throws Exception
    {
        AttachmentResourceReference attachResourceRef =
            new AttachmentResourceReference("xwiki:Some.Page@presentation.odp");
        DocumentReference documentReference = new DocumentReference("wiki", "Some", "Page");
        AttachmentReference attachmentReference = new AttachmentReference("presentation.odp", documentReference);
        OfficeConverter officeConverter = mock(OfficeConverter.class);
        when(this.officeServer.getConverter()).thenReturn(officeConverter);
        when(officeConverter.isPresentation("presentation.odp")).thenReturn(true);

        when(attachmentReferenceResolver.resolve(attachResourceRef.getReference())).thenReturn(attachmentReference);

        when(documentAccessBridge.getAttachmentReferences(attachmentReference.getDocumentReference())).thenReturn(
            Arrays.asList(attachmentReference));
        when(documentAccessBridge.getAttachmentVersion(attachmentReference)).thenReturn("3.2");

        ByteArrayInputStream attachmentContent = new ByteArrayInputStream(new byte[256]);
        when(documentAccessBridge.getAttachmentContent(attachmentReference)).thenReturn(attachmentContent);

        String imageName = "slide0.png";
        ResourceReference imageReference = new ResourceReference(imageName, ResourceType.URL);
        ExpandedMacroBlock galleryMacro =
            new ExpandedMacroBlock("gallery", Collections.singletonMap("width", "300px"), null, false);
        galleryMacro.addChild(new ImageBlock(imageReference, true));
        XDOM xdom = new XDOM(Collections.<Block>singletonList(galleryMacro));

        OfficeConverterResult converterResult = mock(OfficeConverterResult.class);
        XDOMOfficeDocument xdomOfficeDocument = new XDOMOfficeDocument(xdom, Collections.singletonMap(imageName,
            new ByteArrayOfficeDocumentArtifact(imageName, new byte[8])), componentManager, converterResult);

        when(presentationBuilder.build(attachmentContent, attachmentReference.getName(), documentReference))
            .thenReturn(xdomOfficeDocument);

        Map<String, ?> viewParameters = Collections.singletonMap("ownerDocument", documentReference);
        TemporaryResourceReference temporaryResourceReference = new TemporaryResourceReference("officeviewer",
            Arrays.asList(String.valueOf(viewParameters.hashCode()), imageName), documentReference);

        ExtendedURL extendedURL = new ExtendedURL(Arrays.asList("url", "to", imageName));
        when(this.resourceReferenceSerializer.serialize(temporaryResourceReference)).thenReturn(extendedURL);

        XDOM output = this.officeResourceViewer.createView(attachResourceRef, viewParameters);

        ImageBlock imageBlock =
            (ImageBlock) output.getBlocks(new ClassBlockMatcher(ImageBlock.class), Block.Axes.DESCENDANT).get(0);
        assertEquals("/url/to/slide0.png", imageBlock.getReference().getReference());

        galleryMacro = (ExpandedMacroBlock) output
            .getBlocks(new ClassBlockMatcher(ExpandedMacroBlock.class), Block.Axes.DESCENDANT).get(0);
        assertFalse(galleryMacro.getParent() instanceof XDOM);
        assertEquals(Syntax.XWIKI_2_1,
            ((MetaDataBlock) galleryMacro.getParent()).getMetaData().getMetaData(MetaData.SYNTAX));

        verify(this.temporaryResourceStore).createTemporaryFile(eq(temporaryResourceReference), any(InputStream.class));
        verify(converterResult).close();
    }