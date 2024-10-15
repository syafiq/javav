    public void beforeEach() throws Exception
    {
        this.context = this.oldcore.getXWikiContext();

        Utils.setComponentManager(this.oldcore.getMocker());

        QueryManager mockSecureQueryManager =
            this.oldcore.getMocker().registerMockComponent((Type) QueryManager.class, "secure");

        this.mockTemplateProvidersQuery = mock(Query.class);
        when(mockSecureQueryManager.createQuery(any(), any())).thenReturn(this.mockTemplateProvidersQuery);
        when(this.mockTemplateProvidersQuery.execute()).thenReturn(Collections.emptyList());

        when(this.oldcore.getMockContextualAuthorizationManager().hasAccess(any(Right.class),
            any(EntityReference.class))).thenReturn(true);

        Provider<DocumentReference> mockDocumentReferenceProvider =
            this.oldcore.getMocker().registerMockComponent(DocumentReference.TYPE_PROVIDER);
        when(mockDocumentReferenceProvider.get())
            .thenReturn(new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome"));

        this.mockURLFactory = mock(XWikiURLFactory.class);
        this.context.setURLFactory(this.mockURLFactory);

        this.mockRequest = mock(XWikiRequest.class);
        this.context.setRequest(this.mockRequest);

        this.mockResponse = mock(XWikiResponse.class);
        this.context.setResponse(this.mockResponse);

        when(this.mockRequest.get("type")).thenReturn("plain");

        this.oldcore.getMocker().registerMockComponent(ObservationManager.class);

        when(this.csrfToken.getToken()).thenReturn(CSRF_TOKEN_VALUE);
        when(this.csrfToken.isTokenValid(CSRF_TOKEN_VALUE)).thenReturn(true);
        when(this.mockRequest.getParameter("form_token")).thenReturn(CSRF_TOKEN_VALUE);
    }