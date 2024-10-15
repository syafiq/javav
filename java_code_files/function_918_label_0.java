    private void renderXSLFO(String xmlfo, OutputStream out, ExportType type, final XWikiContext context)
        throws XWikiException
    {
        try {
            this.xslFORenderer.render(new ByteArrayInputStream(xmlfo.getBytes(StandardCharsets.UTF_8)), out, type.getMimeType());
        } catch (IllegalStateException e) {
            throw createException(e, type, XWikiException.ERROR_XWIKI_APP_SEND_RESPONSE_EXCEPTION);
        } catch (Exception e) {
            throw createException(e, type, XWikiException.ERROR_XWIKI_EXPORT_PDF_FOP_FAILED);
        }
    }