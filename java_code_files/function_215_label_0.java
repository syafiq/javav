    void obfuscateEmailsSort(boolean obfuscate, String expectedHql, Map<String, Object> expectedBindValues,
        String expectedMail, String expectedMailValue) throws Exception
    {
        // TODO: We mock the mail configuration as it relies on document that are loaded through xar files from external
        //  modules, which is currently not possible (would it be loaded using a mandatory document initializer, it 
        //  would work though).
        when(this.generalMailConfiguration.shouldObfuscate()).thenReturn(obfuscate);

        DocumentReference myClassReference = new DocumentReference("xwiki", "Space", "MyClass");
        XWikiDocument xClassDocument = new XWikiDocument(myClassReference);
        xClassDocument.getXClass().addEmailField("mail", "Email", 100);
        xClassDocument.getXClass().addTextField("name", "Name", 100);
        this.xwiki.saveDocument(xClassDocument, this.context);

        XWikiDocument xObjectDocument = new XWikiDocument(new DocumentReference("xwiki", "Space", "MyObject"));
        xObjectDocument.setSyntax(XWIKI_2_1);
        BaseObject baseObject = xObjectDocument.newXObject(myClassReference, this.context);
        baseObject.set("mail", "test@mail.com", this.context);
        baseObject.set("name", "testName", this.context);
        this.xwiki.saveDocument(xObjectDocument, this.context);

        setColumns("mail,name");
        setClassName("Space.MyClass");
        setSort("email", true);

        when(this.queryService.hql(anyString())).thenReturn(this.query);
        when(this.query.setLimit(anyInt())).thenReturn(this.query);
        when(this.query.setOffset(anyInt())).thenReturn(this.query);
        when(this.query.bindValues(any(Map.class))).thenReturn(this.query);
        when(this.query.count()).thenReturn(1L);
        when(this.query.execute()).thenReturn(singletonList("Space.MyObject"));

        renderPage();

        List<Map<String, Object>> rows = getRows();
        assertEquals(expectedMail, StringUtils.trim((String) rows.get(0).get("mail")));
        assertEquals(expectedMailValue, rows.get(0).get("mail_value"));

        verify(this.queryService).hql(expectedHql);
        verify(this.query).bindValues(expectedBindValues);
    }