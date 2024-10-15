    void applyPDFTemplateWithAuthorExecutor() throws Exception
    {
        when(this.authorizationManager.hasAccess(Right.SCRIPT, AUTHOR_REFERENCE, DOCUMENT_REFERENCE)).thenReturn(true);

        // Do not call the callable to check that the call to the Velocity engine is inside the author executor.
        doReturn("").when(this.authorExecutor).call(any(), any(), any());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.pdfExport.exportHtml(this.htmlContent, baos, PdfExport.ExportType.PDF, this.context);
        verify(this.authorizationManager).hasAccess(Right.SCRIPT, AUTHOR_REFERENCE, DOCUMENT_REFERENCE);
        verify(this.authorExecutor).call(any(), eq(AUTHOR_REFERENCE), eq(DOCUMENT_REFERENCE));
        verifyNoInteractions(this.velocityEngine);
    }