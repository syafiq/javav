    void documentSplitting() throws Exception
    {
        // Create xwiki/2.0 document.
        StringBuffer buffer = new StringBuffer();
        buffer.append("=Heading1=").append('\n');
        buffer.append("Content").append('\n');
        buffer.append("==Heading11==").append('\n');
        buffer.append("Content").append('\n');
        buffer.append("==Heading12==").append('\n');
        buffer.append("Content").append('\n');
        buffer.append("=Heading2=").append('\n');
        buffer.append("Content").append('\n');
        XDOM xdom = xwikiSyntaxParser.parse(new StringReader(buffer.toString()));

        // Create xdom office document.
        XDOMOfficeDocument officeDocument =
            new XDOMOfficeDocument(xdom, Collections.emptySet(), this.componentManager, null);
        final DocumentReference baseDocument = new DocumentReference("xwiki", "Test", "Test");

        // Add expectations to mock document name serializer.
        when(this.mockCompactWikiStringEntityReferenceSerializer.serialize(baseDocument)).thenReturn("Test.Test");
        when(this.mockCompactWikiStringEntityReferenceSerializer
            .serialize(new DocumentReference("xwiki", "Test", "Heading1"))).thenReturn("Test.Heading1");
        when(this.mockCompactWikiStringEntityReferenceSerializer
            .serialize(new DocumentReference("xwiki", "Test", "Heading11"))).thenReturn("Test.Heading11");
        when(this.mockCompactWikiStringEntityReferenceSerializer
            .serialize(new DocumentReference("xwiki", "Test", "Heading12"))).thenReturn("Test.Heading12");
        when(this.mockCompactWikiStringEntityReferenceSerializer
            .serialize(new DocumentReference("xwiki", "Test", "Heading2"))).thenReturn("Test.Heading2");

        // Add expectations to mock document name factory.
        when(this.mockDocumentReferenceResolver.resolve("Test.Test"))
            .thenReturn(new DocumentReference("xwiki", "Test", "Test"));
        when(this.mockDocumentReferenceResolver.resolve("xwiki:Test.Heading1"))
            .thenReturn(new DocumentReference("xwiki", "Test", "Heading1"));
        when(this.mockDocumentReferenceResolver.resolve("xwiki:Test.Heading11"))
            .thenReturn(new DocumentReference("xwiki", "Test", "Heading11"));
        when(this.mockDocumentReferenceResolver.resolve("xwiki:Test.Heading12"))
            .thenReturn(new DocumentReference("xwiki", "Test", "Heading12"));
        when(this.mockDocumentReferenceResolver.resolve("xwiki:Test.Heading2"))
            .thenReturn(new DocumentReference("xwiki", "Test", "Heading2"));

        // Perform the split operation.
        OfficeDocumentSplitterParameters parameters = new OfficeDocumentSplitterParameters();
        parameters.setHeadingLevelsToSplit(new int[] {1, 2, 3, 4, 5, 6});
        parameters.setNamingCriterionHint("headingNames");
        parameters.setBaseDocumentReference(baseDocument);
        Map<TargetDocumentDescriptor, XDOMOfficeDocument> result =
            this.officeDocumentSplitter.split(officeDocument, parameters);

        // There should be five XDOM office documents.
        assertEquals(5, result.size());
    }