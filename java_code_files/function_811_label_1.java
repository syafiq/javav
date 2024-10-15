    void handle() throws Exception
    {
        this.httpServletRequest = mock(XWikiRequest.class);
        when(this.context.getRequest()).thenReturn(this.httpServletRequest);
        this.httpServletResponse = mock(XWikiResponse.class);
        when(this.context.getResponse()).thenReturn(this.httpServletResponse);

        when(this.requestParameterConverter.convert(this.httpServletRequest, this.httpServletResponse))
            .thenReturn(Optional.of(this.httpServletRequest));
        String serializedReference = "XWiki.SomeReference";
        when(this.httpServletRequest.getParameter("docReference")).thenReturn(serializedReference);
        DocumentReference documentReference = mock(DocumentReference.class);
        when(this.documentReferenceResolver.resolve(serializedReference)).thenReturn(documentReference);
        XWikiDocument modifiedDocument = mock(XWikiDocument.class);
        when(this.xWiki.getDocument(documentReference, this.context)).thenReturn(modifiedDocument);
        when(modifiedDocument.clone()).thenReturn(modifiedDocument);
        DocumentReference documentReferenceWithLocale = mock(DocumentReference.class);
        when(modifiedDocument.getDocumentReferenceWithLocale()).thenReturn(documentReferenceWithLocale);

        String title = "some title";
        String description = "some description";
        when(this.httpServletRequest.getParameter("crTitle")).thenReturn(title);
        when(this.httpServletRequest.getParameter("crDescription")).thenReturn(description);

        UserReference userReference = mock(UserReference.class);
        when(this.userReferenceResolver.resolve(CurrentUserReference.INSTANCE)).thenReturn(userReference);
        String previousVersion = "3.2";
        when(this.httpServletRequest.getParameter("previousVersion")).thenReturn(previousVersion);
        XWikiDocumentArchive documentArchive = mock(XWikiDocumentArchive.class);
        when(versioningStore.getXWikiDocumentArchive(modifiedDocument, context)).thenReturn(documentArchive);
        XWikiDocument previousVersionDoc = mock(XWikiDocument.class);
        when(documentArchive.loadDocument(new Version("3.2"), context)).thenReturn(previousVersionDoc);
        when(previousVersionDoc.getDate()).thenReturn(new Date(458));

        ChangeRequest expectedChangeRequest = new ChangeRequest();
        FileChange expectedFileChange = new FileChange(expectedChangeRequest);
        expectedFileChange
            .setAuthor(userReference)
            .setTargetEntity(documentReferenceWithLocale)
            .setPreviousVersion(previousVersion)
            .setPreviousPublishedVersion(previousVersion, new Date(458))
            .setModifiedDocument(modifiedDocument);

        String crId = "myCrID";
        expectedChangeRequest
            .setId(crId)
            .setTitle(title)
            .setDescription(description)
            .setCreator(userReference)
            .addFileChange(expectedFileChange)
            .setStatus(ChangeRequestStatus.READY_FOR_REVIEW);

        doAnswer(invocationOnMock -> {
            ChangeRequest changeRequest = invocationOnMock.getArgument(0);
            List<FileChange> allFileChanges = changeRequest.getLastFileChanges();
            assertEquals(1, allFileChanges.size());
            // ensure to have the exact same date in file change
            Date creationDate = allFileChanges.get(0).getCreationDate();
            expectedFileChange.setCreationDate(creationDate);
            expectedChangeRequest.setCreationDate(changeRequest.getCreationDate());
            changeRequest.setId(crId);
            return null;
        }).when(this.storageManager).save(any());

        DocumentReference crDocReference = mock(DocumentReference.class);
        when(this.changeRequestDocumentReferenceResolver.resolve(expectedChangeRequest)).thenReturn(crDocReference);
        String expectedURL = "/mycr";
        when(this.xWiki.getURL(crDocReference, "view", this.context)).thenReturn(expectedURL);

        this.handler.handle(null);
        verify(this.storageManager).save(expectedChangeRequest);
        verify(this.observationManager)
            .notify(any(ChangeRequestCreatedEvent.class), eq(crId), eq(expectedChangeRequest));
        verify(this.httpServletResponse).sendRedirect(expectedURL);
    }