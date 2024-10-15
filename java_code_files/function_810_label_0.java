    void handleFileChangeNotExisting() throws Exception
    {

        XWikiRequest request = mock(XWikiRequest.class);
        when(context.getRequest()).thenReturn(request);
        XWikiResponse response = mock(XWikiResponse.class);
        when(context.getResponse()).thenReturn(response);
        when(this.requestParameterConverter.convert(request, response)).thenReturn(Optional.of(request));
        String docReference = "XWiki.Doc.Reference";
        when(request.getParameter("docReference")).thenReturn(docReference);
        DocumentReference documentReference = mock(DocumentReference.class);
        when(this.documentReferenceResolver.resolve(docReference)).thenReturn(documentReference);
        XWikiDocument document = mock(XWikiDocument.class);
        when(wiki.getDocument(documentReference, context)).thenReturn(document);
        when(document.clone()).thenReturn(document);
        when(document.getDocumentReferenceWithLocale()).thenReturn(documentReference);
        ChangeRequestReference changeRequestReference = mock(ChangeRequestReference.class);
        String changeRequestId = "some id";
        when(changeRequestReference.getId()).thenReturn(changeRequestId);
        ChangeRequest changeRequest = mock(ChangeRequest.class);
        when(this.storageManager.load(changeRequestId)).thenReturn(Optional.of(changeRequest));
        when(changeRequest.getId()).thenReturn(changeRequestId);
        UserReference userReference = mock(UserReference.class);
        when(this.userReferenceResolver.resolve(CurrentUserReference.INSTANCE)).thenReturn(userReference);
        when(changeRequest.getLatestFileChangeFor(documentReference)).thenReturn(Optional.empty());
        when(request.getParameter(AddChangesChangeRequestHandler.PREVIOUS_VERSION_PARAMETER)).thenReturn("2.1");
        XWikiDocumentArchive documentArchive = mock(XWikiDocumentArchive.class);
        when(versioningStore.getXWikiDocumentArchive(document, context)).thenReturn(documentArchive);
        XWikiDocument previousVersionDoc = mock(XWikiDocument.class);
        when(documentArchive.loadDocument(new Version("2.1"), context)).thenReturn(previousVersionDoc);
        when(previousVersionDoc.getDate()).thenReturn(new Date(4100));
        when(this.fileChangeVersionManager.getNextFileChangeVersion("2.1", false)).thenReturn("filechange-3.1");
        FileChange expectedFileChange = new FileChange(changeRequest)
            .setAuthor(userReference)
            .setTargetEntity(documentReference)
            .setPreviousVersion("2.1")
            .setPreviousPublishedVersion("2.1", new Date(4100))
            .setVersion("filechange-3.1")
            .setModifiedDocument(document);
        when(changeRequest.addFileChange(any())).then(invocationOnMock -> {
            FileChange fileChange = invocationOnMock.getArgument(0);
            expectedFileChange.setCreationDate(fileChange.getCreationDate());
            return null;
        });
        when(this.changeRequestRightsManager.isViewAccessConsistent(changeRequest, documentReference)).thenReturn(true);
        DocumentReference changeRequestDocReference = mock(DocumentReference.class);
        when(this.changeRequestDocumentReferenceResolver.resolve(changeRequest)).thenReturn(changeRequestDocReference);
        String url = "some url";
        when(wiki.getURL(changeRequestDocReference, "view", context)).thenReturn(url);

        when(this.changeRequestRightsManager.isEditWithChangeRequestAllowed(userReference, documentReference))
            .thenReturn(true);
        this.handler.handle(changeRequestReference);
        verify(this.requestParameterConverter).convert(request, response);
        verify(document).clone();
        verify(document).readFromForm(any(EditForm.class), eq(context));
        verify(changeRequest).addFileChange(expectedFileChange);
        verify(this.storageManager).save(changeRequest);
        verify(this.changeRequestApproversManager).getAllApprovers(changeRequest, false);
        verify(this.fileChangeApproversManager).getAllApprovers(expectedFileChange, false);
        verify(this.changeRequestApproversManager).getGroupsApprovers(changeRequest);
        verify(this.fileChangeApproversManager).getGroupsApprovers(expectedFileChange);
        verify(this.observationManager)
            .notify(any(ChangeRequestFileChangeAddedEvent.class), eq(changeRequestId), eq(expectedFileChange));
        verify(response).sendRedirect(url);
    }