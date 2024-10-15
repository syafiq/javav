    void rewriteImagePrefix() throws UnsupportedEncodingException
    {
        Map<String, String> configuration = Map.of(
            "targetDocument", "Path.To.Page",
            "replaceImagePrefix", "output_",
            "replacementImagePrefix", "re@placement_"
        );

        String imageName = "re\\@placement_image.png";
        String encodedImageName = URLEncoder.encode(imageName.replace("\\@", "@"), StandardCharsets.UTF_8);
        AttachmentReference attachmentReference = new AttachmentReference(imageName, this.documentReference);
        when(this.dab.getAttachmentURL(attachmentReference, false)).thenReturn("/path/to/" + encodedImageName);

        filterAndAssertOutput("<img src=\"output_image.png\" />", configuration,
            "<!--startimage:false|-|attach|-|re\\\\@placement_image.png-->"
                + "<img src=\"/path/to/re%40placement_image.png\"/><!--stopimage-->");

        verify(this.dab).getAttachmentURL(attachmentReference, false);
    }