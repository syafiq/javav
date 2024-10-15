    public void filterCollectsEmbeddedImages()
    {
        AttachmentReference attachmentReference = new AttachmentReference("foo.png", this.documentReference);
        when(this.dab.getAttachmentURL(attachmentReference, false)).thenReturn("/path/to/foo.png");

        ResourceReference resourceReference = new ResourceReference("foo.png", ResourceType.ATTACHMENT);
        resourceReference.setTyped(false);
        when(this.xhtmlMarkerSerializer.serialize(resourceReference)).thenReturn("false|-|attach|-|foo.png");

        String fileName =
            DataUri.parse("data:image/jpeg;base64,GgoAAAAN==", Charset.forName("UTF-8")).hashCode() + ".jpg";
        attachmentReference = new AttachmentReference(fileName, this.documentReference);
        when(this.dab.getAttachmentURL(attachmentReference, false)).thenReturn("/path/to/" + fileName);

        resourceReference = new ResourceReference(fileName, ResourceType.ATTACHMENT);
        resourceReference.setTyped(false);
        when(this.xhtmlMarkerSerializer.serialize(resourceReference)).thenReturn("false|-|attach|-|" + fileName);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("targetDocument", "Path.To.Page");
        parameters.put("attachEmbeddedImages", "true");

        Document document = filterAndAssertOutput(
            "<img src=\"data:image/png;fileName=foo.png;base64,iVBORw0K==\"/>"
                + "<img src=\"data:image/jpeg;base64,GgoAAAAN==\"/>",
            parameters,
            "<!--startimage:false|-|attach|-|foo.png--><img src=\"/path/to/foo.png\"/><!--stopimage-->"
                + "<!--startimage:false|-|attach|-|" + fileName + "--><img src=\"/path/to/" + fileName
                + "\"/><!--stopimage-->");

        @SuppressWarnings("unchecked")
        Map<String, byte[]> embeddedImages = (Map<String, byte[]>) document.getUserData("embeddedImages");
        assertEquals(new HashSet<>(Arrays.asList("foo.png", fileName)), embeddedImages.keySet());
    }