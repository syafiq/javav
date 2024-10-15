    public void getVelocityContext()
    {
        VelocityContext context = velocityManager.getVelocityContext();

        assertNull(context.get("doc"));
        assertNull(context.get("sdoc"));

        DocumentReference docReference = new DocumentReference("wiki", "space", "doc");
        DocumentReference sdocReference = new DocumentReference("wiki", "space", "sdoc");

        this.oldcore.getXWikiContext().setDoc(new XWikiDocument(docReference));
        this.oldcore.getXWikiContext().put("sdoc", new XWikiDocument(sdocReference));

        context = this.velocityManager.getVelocityContext();

        Document doc = (Document) context.get("doc");
        assertNotNull(doc);

        Document sdoc = (Document) context.get("sdoc");
        assertNotNull(sdoc);

        // Instances are kept the same when the documents in the context don't change
        context = this.velocityManager.getVelocityContext();
        assertSame(doc, context.get("doc"));
        assertSame(sdoc, context.get("sdoc"));

        // Instances change when the documents in the context change
        docReference = new DocumentReference("wiki", "space", "doc2");
        sdocReference = new DocumentReference("wiki", "space", "sdoc2");
        this.oldcore.getXWikiContext().setDoc(new XWikiDocument(docReference));
        this.oldcore.getXWikiContext().put("sdoc", new XWikiDocument(sdocReference));

        context = this.velocityManager.getVelocityContext();
        assertNotNull(context.get("doc"));
        assertNotSame(doc, context.get("doc"));
        assertNotNull(context.get("sdoc"));
        assertNotSame(sdoc, context.get("sdoc"));
    }