    public XDOMOfficeDocument build(InputStream officeFileStream, String officeFileName,
        DocumentReference documentReference) throws OfficeImporterException
    {
        // Accents seems to cause issues in some conditions
        // See https://jira.xwiki.org/browse/XWIKI-14692
        String cleanedOfficeFileName = StringUtils.stripAccents(officeFileName);

        // Invoke the office document converter.
        OfficeConverterResult officeConverterResult = importPresentation(officeFileStream, cleanedOfficeFileName);

        Pair<String, Set<File>> htmlPresentationResult = null;
        // Create presentation HTML.
        try {
            htmlPresentationResult = buildPresentationHTML(officeConverterResult,
                StringUtils.substringBeforeLast(cleanedOfficeFileName, "."));
        } catch (IOException e) {
            throw new OfficeImporterException("Error while preparing the presentation artifacts.", e);
        }

        // Clear and adjust presentation HTML (slide image URLs are updated to point to the corresponding attachments).
        String html = cleanPresentationHTML(htmlPresentationResult.getLeft(), documentReference);

        // Create the XDOM.
        XDOM xdom = buildPresentationXDOM(html, documentReference);

        return new XDOMOfficeDocument(xdom, htmlPresentationResult.getRight(),
            this.contextComponentManagerProvider.get(), officeConverterResult);
    }