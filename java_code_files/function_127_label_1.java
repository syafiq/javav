    public void filterAddsImageMarkers()
    {
        AttachmentReference attachmentReference = new AttachmentReference("-foo--bar.png-", this.documentReference);
        when(this.dab.getAttachmentURL(attachmentReference, false)).thenReturn("/path/to/foo.png");

        ResourceReference resourceReference = new ResourceReference("-foo--bar.png-", ResourceType.ATTACHMENT);
        resourceReference.setTyped(false);
        when(this.xhtmlMarkerSerializer.serialize(resourceReference)).thenReturn("false|-|attach|-|-foo--bar.png-");

        filterAndAssertOutput("<img src=\"../../some/path/-foo--b%61r.png-\"/>",
            Collections.singletonMap("targetDocument", "Path.To.Page"),
            "<!--startimage:false|-|attach|-|-foo-\\-bar.png-\\--><img src=\"/path/to/foo.png\"/><!--stopimage-->");
    }