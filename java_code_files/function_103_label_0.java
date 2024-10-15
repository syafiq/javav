    public void testPOSTObjectFormUrlEncodedNoCSRF() throws Exception
    {
        final String tagValue = "TAG";
        NameValuePair[] nameValuePairs = new NameValuePair[2];
        String className = "XWiki.TagClass";
        nameValuePairs[0] = new NameValuePair("className", className);
        nameValuePairs[1] = new NameValuePair("property#tags", tagValue);

        String objectGetURI = buildURI(ObjectsResource.class, getWiki(), this.spaces, this.pageName, className);

        // Count objects before to ensure nothing is added on the failed request.
        GetMethod getMethod = executeGet(objectGetURI);
        Assert.assertEquals(getHttpMethodInfo(getMethod), HttpStatus.SC_OK, getMethod.getStatusCode());
        Objects objects = (Objects) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());
        int numObjects = objects.getObjectSummaries().size();

        PostMethod postMethod = executePostForm(
            buildURI(ObjectsResource.class, getWiki(), this.spaces, this.pageName), nameValuePairs,
            TestUtils.SUPER_ADMIN_CREDENTIALS.getUserName(), TestUtils.SUPER_ADMIN_CREDENTIALS.getPassword(), null);
        Assert.assertEquals(getHttpMethodInfo(postMethod), HttpStatus.SC_FORBIDDEN, postMethod.getStatusCode());
        Assert.assertEquals("Invalid or missing form token.", postMethod.getResponseBodyAsString());

        getMethod = executeGet(objectGetURI);
        Assert.assertEquals(getHttpMethodInfo(getMethod), HttpStatus.SC_OK, getMethod.getStatusCode());

        objects = (Objects) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());
        Assert.assertEquals(numObjects, objects.getObjectSummaries().size());
    }