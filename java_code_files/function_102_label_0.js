    public void testPOSTPageFormUrlEncodedNoCSRF() throws Exception
    {
        final String CONTENT = String.format("This is a content (%d)", System.currentTimeMillis());
        final String TITLE = String.format("Title (%s)", UUID.randomUUID().toString());

        Page originalPage = getFirstPage();

        Link link = getFirstLinkByRelation(originalPage, Relations.SELF);
        Assert.assertNotNull(link);

        NameValuePair[] nameValuePairs = new NameValuePair[2];
        nameValuePairs[0] = new NameValuePair("title", TITLE);
        nameValuePairs[1] = new NameValuePair("content", CONTENT);

        PostMethod postMethod = executePostForm(String.format("%s?method=PUT", link.getHref()), nameValuePairs,
            TestUtils.SUPER_ADMIN_CREDENTIALS.getUserName(), TestUtils.SUPER_ADMIN_CREDENTIALS.getPassword(), null);
        Assert.assertEquals(getHttpMethodInfo(postMethod), HttpStatus.SC_FORBIDDEN, postMethod.getStatusCode());
        Assert.assertEquals("Invalid or missing form token.", postMethod.getResponseBodyAsString());

        // Assert that the page hasn't been modified.
        GetMethod getMethod = executeGet(link.getHref());
        Assert.assertEquals(getHttpMethodInfo(getMethod), HttpStatus.SC_OK, getMethod.getStatusCode());

        Page modifiedPage = (Page) this.unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());

        Assert.assertEquals(originalPage.getContent(), modifiedPage.getContent());
        Assert.assertEquals(originalPage.getTitle(), modifiedPage.getTitle());
    }