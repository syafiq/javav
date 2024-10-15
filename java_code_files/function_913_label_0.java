    void applyPDFTemplateWithoutScriptRights() throws Exception
    {
        when(this.authorizationManager.hasAccess(Right.SCRIPT, AUTHOR_REFERENCE, DOCUMENT_REFERENCE)).thenReturn(false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.pdfExport.exportHtml(this.htmlContent, baos, PdfExport.ExportType.PDF, this.context);
        verify(this.authorizationManager).hasAccess(Right.SCRIPT, AUTHOR_REFERENCE, DOCUMENT_REFERENCE);
        verifyNoInteractions(this.authorExecutor);
        verifyNoInteractions(this.velocityEngine);
    }