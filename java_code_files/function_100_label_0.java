    protected String getFormToken(String userName, String password) throws Exception
    {
        GetMethod getMethod = executeGet(getFullUri(WikisResource.class), userName, password);
        Assert.assertEquals(getHttpMethodInfo(getMethod), HttpStatus.SC_OK, getMethod.getStatusCode());
        return getMethod.getResponseHeader("XWiki-Form-Token").getValue();
    }