    void getRenderedTitleRestricted()
    {
        this.document.setRestricted(true);
        // Title with velocity that shouldn't be evaluated
        String title = "#set($key = \"title\")$key";
        this.document.setTitle(title);
        assertEquals(title, this.document.getRenderedTitle(Syntax.XHTML_1_0, this.oldcore.getXWikiContext()));
    }