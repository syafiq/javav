    void handleFileChangeExistingNoConflict() throws Exception
    {
        XWikiRequest request = mock(XWikiRequest.class);
        when(context.getRequest()).thenReturn(request);
        XWikiResponse response = mock(XWikiResponse.class);
        when(context.getResponse()).thenReturn(response);
        when(this.requestParameterConverter.convert(request, response)).thenReturn(Optional.of(request));
        String docReference = "XWiki.Doc.Reference";
        when(request.getParameter("docReference")).thenReturn(docReference);
        DocumentReference documentReference = mock(DocumentReference.class, "editedDoc");
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
        UserReference userReference = mock(UserReference.class, "currentUser");
        when(this.userReferenceResolver.resolve(CurrentUserReference.INSTANCE)).thenReturn(userReference);
        when(request.getParameter(AddChangesChangeRequestHandler.PREVIOUS_VERSION_PARAMETER)).thenReturn("2.1");

        XWikiDocumentArchive documentArchive = mock(XWikiDocumentArchive.class);
        when(versioningStore.getXWikiDocumentArchive(document, context)).thenReturn(documentArchive);
        XWikiDocument previousVersionDoc = mock(XWikiDocument.class);
        when(documentArchive.loadDocument(new Version("2.1"), context)).thenReturn(previousVersionDoc);
        when(previousVersionDoc.getDate()).thenReturn(new Date(478));

        FileChange existingFileChange = mock(FileChange.class);
        when(changeRequest.getLatestFileChangeFor(documentReference)).thenReturn(Optional.of(existingFileChange));
        when(existingFileChange.getPreviousPublishedVersion()).thenReturn("1.1");
        when(existingFileChange.getPreviousPublishedVersionDate()).thenReturn(new Date(58));
        MergeDocumentResult mergeDocumentResult = mock(MergeDocumentResult.class);
        when(this.changeRequestMergeManager.mergeDocumentChanges(document, "2.1", changeRequest))
            .thenReturn(Optional.of(mergeDocumentResult));
        when(this.fileChangeVersionManager.getNextFileChangeVersion("2.1", true)).thenReturn("filechange-2.2");
        when(mergeDocumentResult.hasConflicts()).thenReturn(false);
        XWikiDocument mergedDocument = mock(XWikiDocument.class);
        when(mergeDocumentResult.getMergeResult()).thenReturn(mergedDocument);
        FileChange expectedFileChange = new FileChange(changeRequest)
            .setAuthor(userReference)
            .setTargetEntity(documentReference)
            .setPreviousVersion("2.1")
            .setPreviousPublishedVersion("1.1", new Date(58))
            .setVersion("filechange-2.2")
            .setModifiedDocument(mergedDocument);

        when(changeRequest.addFileChange(any())).then(invocationOnMock -> {
            FileChange fileChange = invocationOnMock.getArgument(0);
            expectedFileChange.setCreationDate(fileChange.getCreationDate());
            return null;
        });
        DocumentReference changeRequestDocReference = mock(DocumentReference.class);
        when(this.changeRequestDocumentReferenceResolver.resolve(changeRequest)).thenReturn(changeRequestDocReference);
        String url = "some url";
        when(wiki.getURL(changeRequestDocReference, "view", context)).thenReturn(url);

        when(this.changeRequestRightsManager.isEditWithChangeRequestAllowed(userReference, documentReference))
            .thenReturn(false);
        ChangeRequestException changeRequestException =
            assertThrows(ChangeRequestException.class, () -> this.handler.handle(changeRequestReference));
        assertEquals("User [currentUser] is not allowed to edit the document [editedDoc] through a change request.",
            changeRequestException.getMessage());

        when(this.changeRequestRightsManager.isEditWithChangeRequestAllowed(userReference, documentReference))
            .thenReturn(true);
        this.handler.handle(changeRequestReference);

        verify(this.requestParameterConverter, times(2)).convert(request, response);
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