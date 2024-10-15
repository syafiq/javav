    public void filterAddsImageMarkersSpecialCharacters() throws UnsupportedEncodingException
    {
        String imageNameUrl = "foo&amp;+_b%61r@.png";
        String imageName = "foo&+_bar\\@.png";
        String encodedImageName = URLEncoder.encode(imageName, "UTF-8");
        AttachmentReference attachmentReference = new AttachmentReference(imageName, this.documentReference);
        when(this.dab.getAttachmentURL(attachmentReference, false)).thenReturn("/path/to/" + encodedImageName);

        ResourceReference resourceReference = new ResourceReference(imageName, ResourceType.ATTACHMENT);
        resourceReference.setTyped(false);
        String imageNameEscaped = "foo&+_bar\\\\@.png";
        when(this.xhtmlMarkerSerializer.serialize(resourceReference)).thenReturn("false|-|attach|-|" + imageName);

        filterAndAssertOutput(String.format( "<img src=\"../../some/path/%s\"/>", imageNameUrl),
            Collections.singletonMap("targetDocument", "Path.To.Page"),
            String.format("<!--startimage:false|-|attach|-|%s--><img src=\"/path/to/%s\"/><!--stopimage-->",
                imageNameEscaped, encodedImageName));
    }