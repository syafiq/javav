    public void testPOSTCommentWithTextPlainNoCSRF() throws Exception
    {
        String commentsUri = buildURI(CommentsResource.class, getWiki(), this.spaces, this.pageName).toString();

        GetMethod getMethod = executeGet(commentsUri);
        Assert.assertEquals(getHttpMethodInfo(getMethod), HttpStatus.SC_OK, getMethod.getStatusCode());

        Comments comments = (Comments) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());

        int numberOfComments = comments.getComments().size();

        PostMethod postMethod = executePost(commentsUri, "Comment", MediaType.TEXT_PLAIN,
            TestUtils.SUPER_ADMIN_CREDENTIALS.getUserName(), TestUtils.SUPER_ADMIN_CREDENTIALS.getPassword(), null);
        Assert.assertEquals(getHttpMethodInfo(postMethod), HttpStatus.SC_FORBIDDEN, postMethod.getStatusCode());
        Assert.assertEquals("Invalid or missing form token.", postMethod.getResponseBodyAsString());

        getMethod = executeGet(commentsUri);
        Assert.assertEquals(getHttpMethodInfo(getMethod), HttpStatus.SC_OK, getMethod.getStatusCode());

        comments = (Comments) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());

        Assert.assertEquals(numberOfComments, comments.getComments().size());
    }