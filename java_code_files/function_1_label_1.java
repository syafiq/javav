    public void before(Class<?> testClass) throws Exception
    {
        // Statically store the component manager in {@link Utils} to be able to access it without
        // the context.
        Utils.setComponentManager(getMocker());

        this.context = new XWikiContext();

        getXWikiContext().setWikiId("xwiki");
        getXWikiContext().setMainXWiki("xwiki");

        this.spyXWiki = spy(new XWiki());
        getXWikiContext().setWiki(this.spyXWiki);

        this.mockHibernateStore = getMocker().registerMockComponent(HibernateStore.class);
        this.mockXWikiHibernateStore = mock(XWikiHibernateStore.class);
        getMocker().registerComponent(XWikiStoreInterface.class, XWikiHibernateBaseStore.HINT,
            this.mockXWikiHibernateStore);
        this.mockVersioningStore =
            getMocker().registerMockComponent(XWikiVersioningStoreInterface.class, XWikiHibernateBaseStore.HINT);
        this.mockAttachmentVersioningStore =
            getMocker().registerMockComponent(AttachmentVersioningStore.class, XWikiHibernateBaseStore.HINT);
        this.mockRightService = mock(XWikiRightService.class);
        this.mockGroupService = mock(XWikiGroupService.class);
        this.mockAuthService = mock(XWikiAuthService.class);

        doReturn(new Configuration()).when(this.mockHibernateStore).getConfiguration();

        this.spyXWiki.setStore(this.mockXWikiHibernateStore);
        this.spyXWiki.setVersioningStore(this.mockVersioningStore);
        this.spyXWiki.setDefaultAttachmentArchiveStore(this.mockAttachmentVersioningStore);
        this.spyXWiki.setRightService(this.mockRightService);
        this.spyXWiki.setAuthService(this.mockAuthService);
        this.spyXWiki.setGroupService(this.mockGroupService);
        this.spyXWiki.setPluginManager(new XWikiPluginManager());

        // We need to initialize the Component Manager so that the components can be looked up
        getXWikiContext().put(ComponentManager.class.getName(), getMocker());

        if (testClass.getAnnotation(AllComponents.class) != null) {
            // If @AllComponents is enabled force mocking AuthorizationManager and ContextualAuthorizationManager if not
            // already mocked
            this.mockAuthorizationManager = getMocker().registerMockComponent(AuthorizationManager.class, false);
            this.mockContextualAuthorizationManager =
                getMocker().registerMockComponent(ContextualAuthorizationManager.class, false);
        } else {
            // Make sure an AuthorizationManager and a ContextualAuthorizationManager is available
            if (!getMocker().hasComponent(AuthorizationManager.class)) {
                this.mockAuthorizationManager = getMocker().registerMockComponent(AuthorizationManager.class);
            }
            if (!getMocker().hasComponent(ContextualAuthorizationManager.class)) {
                this.mockContextualAuthorizationManager =
                    getMocker().registerMockComponent(ContextualAuthorizationManager.class);
            }
        }

        // Make sure to provide a EntityReferenceFactory
        if (!getMocker().hasComponent(EntityReferenceFactory.class)) {
            EntityReferenceFactory factory = getMocker().registerMockComponent(EntityReferenceFactory.class);
            when(factory.getReference(any())).thenAnswer((invocation) -> invocation.getArgument(0));
        }

        // Make sure a default ConfigurationSource is available
        if (!getMocker().hasComponent(ConfigurationSource.class)) {
            this.configurationSource = getMocker().registerMemoryConfigurationSource();
        }

        // Make sure a "xwikicfg" ConfigurationSource is available
        if (!getMocker().hasComponent(ConfigurationSource.class, XWikiCfgConfigurationSource.ROLEHINT)) {
            this.xwikicfgConfigurationSource = new MockConfigurationSource();
            getMocker().registerComponent(MockConfigurationSource.getDescriptor(XWikiCfgConfigurationSource.ROLEHINT),
                this.xwikicfgConfigurationSource);
        }
        // Make sure a "wiki" ConfigurationSource is available
        if (!getMocker().hasComponent(ConfigurationSource.class, "wiki")) {
            this.wikiConfigurationSource = new MockConfigurationSource();
            getMocker().registerComponent(MockConfigurationSource.getDescriptor("wiki"), this.wikiConfigurationSource);
        }

        // Make sure a "space" ConfigurationSource is available
        if (!getMocker().hasComponent(ConfigurationSource.class, "space")) {
            this.spaceConfigurationSource = new MockConfigurationSource();
            getMocker().registerComponent(MockConfigurationSource.getDescriptor("space"),
                this.spaceConfigurationSource);
        }

        // Since the oldcore module draws the Servlet Environment in its dependencies we need to ensure it's set up
        // correctly with a Servlet Context.
        if (getMocker().hasComponent(Environment.class)) {
            if (getMocker().getInstance(Environment.class) instanceof ServletEnvironment) {
                ServletEnvironment servletEnvironment = getMocker().getInstance(Environment.class);

                ServletContext servletContextMock = mock(ServletContext.class);
                servletEnvironment.setServletContext(servletContextMock);
                when(servletContextMock.getAttribute("javax.servlet.context.tempdir"))
                    .thenReturn(new File(System.getProperty("java.io.tmpdir")));

                initEnvironmentDirectories();

                servletEnvironment.setTemporaryDirectory(this.temporaryDirectory);
                servletEnvironment.setPermanentDirectory(this.permanentDirectory);

                this.environment = servletEnvironment;
            }
        } else {
            // Automatically register an Environment when none is available since it's a very common need
            registerMockEnvironment();
        }

        // Initialize XWikiContext provider
        if (!this.componentManager.hasComponent(XWikiContext.TYPE_PROVIDER)) {
            Provider<XWikiContext> xcontextProvider =
                this.componentManager.registerMockComponent(XWikiContext.TYPE_PROVIDER);
            when(xcontextProvider.get()).thenReturn(getXWikiContext());
        } else {
            Provider<XWikiContext> xcontextProvider = this.componentManager.getInstance(XWikiContext.TYPE_PROVIDER);
            if (MockUtil.isMock(xcontextProvider)) {
                when(xcontextProvider.get()).thenReturn(getXWikiContext());
            }
        }

        // Initialize readonly XWikiContext provider
        if (!this.componentManager.hasComponent(XWikiContext.TYPE_PROVIDER, "readonly")) {
            Provider<XWikiContext> xcontextProvider =
                this.componentManager.registerMockComponent(XWikiContext.TYPE_PROVIDER, "readonly");
            when(xcontextProvider.get()).thenReturn(getXWikiContext());
        } else {
            Provider<XWikiContext> xcontextProvider = this.componentManager.getInstance(XWikiContext.TYPE_PROVIDER);
            if (MockUtil.isMock(xcontextProvider)) {
                when(xcontextProvider.get()).thenReturn(getXWikiContext());
            }
        }

        // Initialize the Execution Context
        if (this.componentManager.hasComponent(ExecutionContextManager.class)) {
            ExecutionContextManager ecm = this.componentManager.getInstance(ExecutionContextManager.class);
            ExecutionContext ec = new ExecutionContext();
            ecm.initialize(ec);
        }

        // Bridge with old XWiki Context, required for old code.
        Execution execution;
        if (this.componentManager.hasComponent(Execution.class)) {
            execution = this.componentManager.getInstance(Execution.class);
        } else {
            execution = this.componentManager.registerMockComponent(Execution.class);
        }
        ExecutionContext econtext;
        if (MockUtil.isMock(execution)) {
            econtext = new ExecutionContext();
            when(execution.getContext()).thenReturn(econtext);
        } else {
            econtext = execution.getContext();
        }

        // Set a few standard things in the ExecutionContext
        econtext.setProperty(XWikiContext.EXECUTIONCONTEXT_KEY, getXWikiContext());
        this.scriptContext = (ScriptContext) econtext.getProperty(ScriptExecutionContextInitializer.SCRIPT_CONTEXT_ID);
        if (this.scriptContext == null) {
            this.scriptContext = new CloneableSimpleScriptContext();
            econtext.setProperty(ScriptExecutionContextInitializer.SCRIPT_CONTEXT_ID, this.scriptContext);
        }

        if (!this.componentManager.hasComponent(ScriptContextManager.class)) {
            ScriptContextManager scriptContextManager =
                this.componentManager.registerMockComponent(ScriptContextManager.class);
            when(scriptContextManager.getCurrentScriptContext()).thenReturn(this.scriptContext);
            when(scriptContextManager.getScriptContext()).thenReturn(this.scriptContext);
        }

        // Initialize the initial context in the stub context provider (which is then used for all calls to
        // createStubContext() in the stub context provider). This is to simulate an XWiki runtime, where this is
        // initialized on the first HTTP request.
        if (this.componentManager.hasComponent(XWikiStubContextProvider.class)) {
            XWikiStubContextProvider stubContextProvider =
                this.componentManager.getInstance(XWikiStubContextProvider.class);
            if (!MockUtil.isMock(stubContextProvider)) {
                // TODO: Since we create the XWikiContext in this method and since no request has been set into it at
                // this point, the initial context in XWikiStubContextProvider would normally be initialized with an
                // empty request which would lead to some NPE when ServletRequest is used later on, for example. Thus
                // we force a request in the context here before the call to stubContextProvider.initialize().
                // Note that this needs to be refactored to let the test control what to initialize in the initial
                // context (i.e. before stubContextProvider.initialize() is called).
                // Also note that setting a non null request forces us to set a non null URL as otherwise it would lead
                // to another NPE...
                XWikiRequest originalRequest = getXWikiContext().getRequest();
                if (getXWikiContext().getRequest() == null) {
                    getXWikiContext().setRequest(new XWikiServletRequestStub());
                }
                URL originalURL = getXWikiContext().getURL();
                if (getXWikiContext().getURL() == null) {
                    getXWikiContext().setURL(new URL("http://localhost:8080"));
                }
                stubContextProvider.initialize(getXWikiContext());
                // Make sure we leave the XWikiContext unchanged (since we just temporarily modified the URL and
                // request to set up the initial context in XWikiStubContextProvider).
                getXWikiContext().setURL(originalURL);
                getXWikiContext().setRequest(originalRequest);
            }
        }

        // Make sure to have a mocked CoreConfiguration (even if one already exist)
        if (!this.componentManager.hasComponent(CoreConfiguration.class)) {
            CoreConfiguration coreConfigurationMock =
                this.componentManager.registerMockComponent(CoreConfiguration.class);
            when(coreConfigurationMock.getDefaultDocumentSyntax()).thenReturn(Syntax.XWIKI_2_1);
        } else {
            CoreConfiguration coreConfiguration =
                this.componentManager.registerMockComponent(CoreConfiguration.class, false);
            if (MockUtil.isMock(coreConfiguration)) {
                when(coreConfiguration.getDefaultDocumentSyntax()).thenReturn(Syntax.XWIKI_2_1);
            }
        }

        // Set a context ComponentManager if none exist
        if (!this.componentManager.hasComponent(ComponentManager.class, "context")) {
            DefaultComponentDescriptor<ComponentManager> componentManagerDescriptor =
                new DefaultComponentDescriptor<>();
            componentManagerDescriptor.setRoleHint("context");
            componentManagerDescriptor.setRoleType(ComponentManager.class);
            this.componentManager.registerComponent(componentManagerDescriptor, this.componentManager);
        }

        // Register mock components for refactoring listener components
        if (!this.componentManager.hasComponent(ModelBridge.class)) {
            this.componentManager.registerMockComponent(ModelBridge.class);
        }

        if (!this.componentManager.hasComponent(ReferenceUpdater.class)) {
            this.componentManager.registerMockComponent(ReferenceUpdater.class);
        }

        // Make sure to a have an URLConfiguration component.
        if (!this.componentManager.hasComponent(URLConfiguration.class)) {
            URLConfiguration mockUrlConfigComponent =
                this.componentManager.registerMockComponent(URLConfiguration.class);
            when(mockUrlConfigComponent.getURLFormatId()).thenReturn("standard");
        }

        if (!this.componentManager.hasComponent(EntityNameValidationManager.class)) {
            this.componentManager.registerMockComponent(EntityNameValidationManager.class);
        }
        if (!this.componentManager.hasComponent(EntityNameValidationConfiguration.class)) {
            this.componentManager.registerMockComponent(EntityNameValidationConfiguration.class);
        }

        boolean supportRevisionStore = this.componentManager.hasComponent(XWikiDocumentFilterUtils.class);

        // Mock getting document revisions using DocumentRevisionProvider.
        if (supportRevisionStore && !this.componentManager.hasComponent(DocumentRevisionProvider.class, "database")) {
            DocumentRevisionProvider documentRevisionProvider =
                this.componentManager.registerMockComponent(DocumentRevisionProvider.class, "database");

            when(documentRevisionProvider.getRevision(any(DocumentReference.class), anyString()))
                .then(invocation -> {
                    DocumentReference reference = invocation.getArgument(0);
                    String revision = invocation.getArgument(1);
                    XWikiDocument document = getSpyXWiki().getDocument(reference, this.context);

                    return documentRevisionProvider.getRevision(document, revision);
                });

            when(documentRevisionProvider.getRevision(anyXWikiDocument(), anyString()))
                .then(invocation -> {
                        XWikiDocument baseDocument = invocation.getArgument(0);
                        String revision = invocation.getArgument(1);
                        return getMockVersioningStore().loadXWikiDoc(baseDocument, revision, this.context);
                    }
                );
        }

        // Set the default revision provider to the database-one when missing as this covers most cases.
        if (supportRevisionStore && !this.componentManager.hasComponent(DocumentRevisionProvider.class)) {
            DocumentRevisionProvider databaseRevisionProvider =
                this.componentManager.getInstance(DocumentRevisionProvider.class, "database");
            this.componentManager.registerComponent(DocumentRevisionProvider.class, databaseRevisionProvider);
        }

        getXWikiContext().setLocale(Locale.ENGLISH);

        // XWikiStoreInterface

        when(getMockStore().getTranslationList(anyXWikiDocument(), anyXWikiContext())).then(new Answer<List<String>>()
        {
            @Override
            public List<String> answer(InvocationOnMock invocation) throws Throwable
            {
                XWikiDocument document = invocation.getArgument(0);

                List<String> translations = new ArrayList<String>();

                for (XWikiDocument storedDocument : documents.values()) {
                    Locale storedLocale = storedDocument.getLocale();
                    if (!storedLocale.equals(Locale.ROOT)
                        && storedDocument.getDocumentReference().equals(document.getDocumentReference())) {
                        translations.add(storedLocale.toString());
                    }
                }

                return translations;
            }
        });
        when(getMockStore().loadXWikiDoc(anyXWikiDocument(), anyXWikiContext())).then(new Answer<XWikiDocument>()
        {
            @Override
            public XWikiDocument answer(InvocationOnMock invocation) throws Throwable
            {
                // The store is based on the contex for the wiki
                DocumentReference reference = invocation.<XWikiDocument>getArgument(0).getDocumentReferenceWithLocale();
                XWikiContext xcontext = invocation.getArgument(1);
                if (!xcontext.getWikiReference().equals(reference.getWikiReference())) {
                    reference = reference.setWikiReference(xcontext.getWikiReference());
                }

                XWikiDocument document = documents.get(reference);

                if (document == null) {
                    document = new XWikiDocument(reference, reference.getLocale());
                    document.setSyntax(Syntax.PLAIN_1_0);
                    document.setOriginalDocument(document.clone());
                }

                return document;
            }
        });
        when(getMockStore().exists(anyXWikiDocument(), anyXWikiContext())).then(new Answer<Boolean>()
        {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable
            {
                // The store is based on the context for the wiki
                DocumentReference reference = invocation.<XWikiDocument>getArgument(0).getDocumentReferenceWithLocale();
                XWikiContext xcontext = invocation.getArgument(1);
                if (!xcontext.getWikiReference().equals(reference.getWikiReference())) {
                    reference = reference.setWikiReference(xcontext.getWikiReference());
                }

                return documents.containsKey(reference);
            }
        });
        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                // The store is based on the context for the wiki
                DocumentReference reference = invocation.<XWikiDocument>getArgument(0).getDocumentReferenceWithLocale();
                XWikiContext xcontext = invocation.getArgument(1);
                if (!xcontext.getWikiReference().equals(reference.getWikiReference())) {
                    reference = reference.setWikiReference(xcontext.getWikiReference());
                }

                documents.remove(reference);
                documentArchives.remove(reference);

                return null;
            }
        }).when(getMockStore()).deleteXWikiDoc(anyXWikiDocument(), anyXWikiContext());
        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                XWikiDocument document = invocation.getArgument(0);

                // The store is based on the context for the wiki
                DocumentReference reference = document.getDocumentReferenceWithLocale();
                XWikiContext xcontext = invocation.getArgument(1);
                if (!xcontext.getWikiReference().equals(reference.getWikiReference())) {
                    reference = reference.setWikiReference(xcontext.getWikiReference());
                }

                if (document.isContentDirty() || document.isMetaDataDirty()) {
                    document.setDate(new Date());
                    if (document.isContentDirty()) {
                        document.setContentUpdateDate(new Date());
                        document.setContentAuthorReference(document.getAuthorReference());
                    }
                    document.incrementVersion();

                    document.setContentDirty(false);
                    document.setMetaDataDirty(false);

                    if (supportRevisionStore) {
                        // Save the document in the document archive.
                        getMockVersioningStore().updateXWikiDocArchive(document, true, xcontext);
                    }
                }
                document.setNew(false);
                document.setStore(getMockStore());

                XWikiDocument savedDocument = document.clone();

                documents.put(document.getDocumentReferenceWithLocale(), savedDocument);


                // Set the document as it's original document
                savedDocument.setOriginalDocument(savedDocument.clone());

                return null;
            }
        }).when(getMockStore()).saveXWikiDoc(anyXWikiDocument(), anyXWikiContext());
        when(getMockStore().getLimitSize(any(), any(), any())).thenReturn(255);

        // XWikiVersioningStoreInterface

        when(getMockVersioningStore().getXWikiDocumentArchive(anyXWikiDocument(), anyXWikiContext()))
            .then(new Answer<XWikiDocumentArchive>()
            {
                @Override
                public XWikiDocumentArchive answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiDocument document = invocation.getArgument(0);

                    // The store is based on the context for the wiki
                    DocumentReference reference = document.getDocumentReferenceWithLocale();

                    XWikiDocumentArchive archiveDoc = documentArchives.get(reference);

                    if (archiveDoc == null) {
                        XWikiContext xcontext = invocation.getArgument(1);
                        String db = xcontext.getWikiId();
                        try {
                            if (reference.getWikiReference().getName() != null) {
                                xcontext.setWikiId(reference.getWikiReference().getName());
                            }
                            archiveDoc = new XWikiDocumentArchive(document.getId());
                            document.setDocumentArchive(archiveDoc);
                            documentArchives.put(reference, archiveDoc);
                        } finally {
                            xcontext.setWikiId(db);
                        }
                    }

                    return archiveDoc;
                }
            });
        when(getMockVersioningStore().getXWikiDocVersions(anyXWikiDocument(), anyXWikiContext()))
            .then(new Answer<Version[]>()
            {
                @Override
                public Version[] answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiDocumentArchive archive = getMockVersioningStore()
                        .getXWikiDocumentArchive(invocation.getArgument(0), invocation.getArgument(1));

                    if (archive == null) {
                        return new Version[0];
                    }
                    Collection<XWikiRCSNodeInfo> nodes = archive.getNodes();
                    Version[] versions = new Version[nodes.size()];
                    Iterator<XWikiRCSNodeInfo> it = nodes.iterator();
                    for (int i = 0; i < versions.length; i++) {
                        XWikiRCSNodeInfo node = it.next();
                        versions[versions.length - 1 - i] = node.getId().getVersion();
                    }

                    return versions;
                }
            });
        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                XWikiDocument document = invocation.getArgument(0);
                XWikiContext xcontext = invocation.getArgument(2);

                XWikiDocumentArchive archiveDoc = getMockVersioningStore().getXWikiDocumentArchive(document, xcontext);
                archiveDoc.updateArchive(document, document.getAuthor(), document.getDate(), document.getComment(),
                    document.getRCSVersion(), xcontext);
                document.setRCSVersion(archiveDoc.getLatestVersion());

                return null;
            }
        }).when(getMockVersioningStore()).updateXWikiDocArchive(any(), anyBoolean(), any());

        doAnswer(invocation -> {
            XWikiDocument baseDocument = invocation.getArgument(0);
            String revision = invocation.getArgument(1);
            XWikiContext xContext = invocation.getArgument(2);

            XWikiDocumentArchive archive = getMockVersioningStore().getXWikiDocumentArchive(baseDocument, xContext);
            Version version = new Version(revision);

            XWikiDocument doc = archive.loadDocument(version, xContext);
            if (doc == null) {
                Object[] args = { baseDocument.getDocumentReferenceWithLocale(), version.toString() };
                throw new XWikiException(XWikiException.MODULE_XWIKI_STORE,
                    XWikiException.ERROR_XWIKI_STORE_HIBERNATE_UNEXISTANT_VERSION,
                    "Version {1} does not exist while reading document {0}", null, args);
            }

            doc.setStore(baseDocument.getStore());

            // Make sure the attachment of the revision document have the right store
            for (XWikiAttachment revisionAttachment : doc.getAttachmentList()) {
                XWikiAttachment attachment = baseDocument.getAttachment(revisionAttachment.getFilename());

                if (attachment != null) {
                    revisionAttachment.setContentStore(attachment.getContentStore());
                    revisionAttachment.setArchiveStore(attachment.getArchiveStore());
                }
            }

            return doc;
        }).when(getMockVersioningStore()).loadXWikiDoc(anyXWikiDocument(), anyString(), anyXWikiContext());

        // XWiki

        if (this.mockXWiki) {
            if (!supportRevisionStore) {
                doAnswer((Answer<XWikiDocument>) invocation -> {
                    XWikiDocument doc = invocation.getArgument(0);
                    String revision = invocation.getArgument(1);

                    if (StringUtils.equals(revision, doc.getVersion())) {
                        return doc;
                    }

                    return new XWikiDocument(doc.getDocumentReference());
                }).when(getSpyXWiki()).getDocument(anyXWikiDocument(), any(), anyXWikiContext());
            }
            doAnswer(new Answer<XWikiDocument>()
            {
                @Override
                public XWikiDocument answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiDocument document = invocation.getArgument(0);

                    String currentWiki = getXWikiContext().getWikiId();
                    try {
                        getXWikiContext().setWikiId(document.getDocumentReference().getWikiReference().getName());

                        return getMockStore().loadXWikiDoc(document, getXWikiContext());
                    } finally {
                        getXWikiContext().setWikiId(currentWiki);
                    }
                }
            }).when(getSpyXWiki()).getDocument(anyXWikiDocument(), any(XWikiContext.class));
            doAnswer(new Answer<Void>()
            {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiDocument document = invocation.getArgument(0);
                    String comment = invocation.getArgument(1);
                    boolean minorEdit = invocation.getArgument(2);
                    XWikiContext xcontext = invocation.getArgument(3);

                    boolean isNew = document.isNew();

                    document.setComment(StringUtils.defaultString(comment));
                    document.setMinorEdit(minorEdit);

                    if (document.isContentDirty() || document.isMetaDataDirty()) {
                        document.setDate(new Date());
                        if (document.isContentDirty()) {
                            document.setContentUpdateDate(new Date());
                            document.setContentAuthorReference(document.getAuthorReference());
                        }
                        document.incrementVersion();

                        document.setContentDirty(false);
                        document.setMetaDataDirty(false);

                        // Save the document in the document archive.
                        if (supportRevisionStore) {
                            getMockVersioningStore().updateXWikiDocArchive(document, true, xcontext);
                        }
                    }
                    document.setNew(false);
                    document.setStore(getMockStore());

                    XWikiDocument previousDocument = documents.get(document.getDocumentReferenceWithLocale());

                    if (previousDocument != null && previousDocument != document) {
                        for (XWikiAttachment attachment : document.getAttachmentList()) {
                            if (!attachment.isContentDirty()) {
                                attachment.setAttachment_content(
                                    previousDocument.getAttachment(attachment.getFilename()).getAttachment_content());
                            }
                        }
                    }

                    XWikiDocument originalDocument = document.getOriginalDocument();
                    if (originalDocument == null) {
                        originalDocument =
                            spyXWiki.getDocument(document.getDocumentReferenceWithLocale(), getXWikiContext());
                        document.setOriginalDocument(originalDocument);
                    }

                    XWikiDocument savedDocument = document.clone();
                    documents.put(document.getDocumentReferenceWithLocale(), savedDocument);

                    if (isNew) {
                        if (notifyDocumentCreatedEvent) {
                            getObservationManager().notify(new DocumentCreatedEvent(document.getDocumentReference()),
                                document, getXWikiContext());
                        }
                    } else {
                        if (notifyDocumentUpdatedEvent) {
                            getObservationManager().notify(new DocumentUpdatedEvent(document.getDocumentReference()),
                                document, getXWikiContext());
                        }
                    }

                    // Set the document as it's original document
                    savedDocument.setOriginalDocument(savedDocument.clone());

                    return null;
                }
            }).when(getSpyXWiki()).saveDocument(anyXWikiDocument(), any(String.class), anyBoolean(), anyXWikiContext());
            doNothing().when(getSpyXWiki()).checkSavingDocument(any(DocumentReference.class), anyXWikiDocument(),
                any(String.class), anyBoolean(), anyXWikiContext());
            doAnswer(new Answer<Void>()
            {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiDocument document = invocation.getArgument(0);

                    String currentWiki = null;

                    currentWiki = getXWikiContext().getWikiId();
                    try {
                        getXWikiContext().setWikiId(document.getDocumentReference().getWikiReference().getName());

                        getMockStore().deleteXWikiDoc(document, getXWikiContext());

                        if (notifyDocumentDeletedEvent) {
                            getObservationManager().notify(new DocumentDeletedEvent(document.getDocumentReference()),
                                document, getXWikiContext());
                        }
                    } finally {
                        getXWikiContext().setWikiId(currentWiki);
                    }

                    return null;
                }
            }).when(getSpyXWiki()).deleteDocument(anyXWikiDocument(), any(Boolean.class), anyXWikiContext());
            doNothing().when(getSpyXWiki()).checkDeletingDocument(any(DocumentReference.class), anyXWikiDocument(),
                anyXWikiContext());
            doAnswer(new Answer<BaseClass>()
            {
                @Override
                public BaseClass answer(InvocationOnMock invocation) throws Throwable
                {
                    return getSpyXWiki()
                        .getDocument((DocumentReference) invocation.getArguments()[0], invocation.getArgument(1))
                        .getXClass();
                }
            }).when(getSpyXWiki()).getXClass(any(DocumentReference.class), anyXWikiContext());
            doAnswer(new Answer<String>()
            {
                @Override
                public String answer(InvocationOnMock invocation) throws Throwable
                {
                    return getXWikiContext().getLanguage();
                }
            }).when(getSpyXWiki()).getLanguagePreference(anyXWikiContext());

            // Users

            doAnswer(new Answer<BaseClass>()
            {
                @Override
                public BaseClass answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiContext xcontext = invocation.getArgument(0);

                    XWikiDocument userDocument = getSpyXWiki().getDocument(
                        new DocumentReference(USER_CLASS, new WikiReference(xcontext.getWikiId())), xcontext);

                    final BaseClass userClass = userDocument.getXClass();

                    if (userDocument.isNew()) {
                        userClass.addTextField("first_name", "First Name", 30);
                        userClass.addTextField("last_name", "Last Name", 30);
                        userClass.addEmailField("email", "e-Mail", 30);
                        userClass.addPasswordField("password", "Password", 10);
                        userClass.addBooleanField("active", "Active", "active");
                        userClass.addTextAreaField("comment", "Comment", 40, 5);
                        userClass.addTextField("avatar", "Avatar", 30);
                        userClass.addTextField("phone", "Phone", 30);
                        userClass.addTextAreaField("address", "Address", 40, 3);

                        getSpyXWiki().saveDocument(userDocument, xcontext);
                    }

                    return userClass;
                }
            }).when(getSpyXWiki()).getUserClass(anyXWikiContext());
            doAnswer(new Answer<BaseClass>()
            {
                @Override
                public BaseClass answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiContext xcontext = invocation.getArgument(0);

                    XWikiDocument groupDocument = getSpyXWiki().getDocument(
                        new DocumentReference(GROUP_CLASS, new WikiReference(xcontext.getWikiId())), xcontext);

                    final BaseClass groupClass = groupDocument.getXClass();

                    if (groupDocument.isNew()) {
                        groupClass.addTextField("member", "Member", 30);

                        getSpyXWiki().saveDocument(groupDocument, xcontext);
                    }

                    return groupClass;
                }
            }).when(getSpyXWiki()).getGroupClass(anyXWikiContext());

            // RightsClass
            doAnswer(new Answer<BaseClass>()
            {
                @Override
                public BaseClass answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiContext xcontext = invocation.getArgument(0);

                    XWikiDocument rightDocument = getSpyXWiki().getDocument(
                        new DocumentReference(RIGHTS_CLASS, new WikiReference(xcontext.getWikiId())), xcontext);

                    final BaseClass rightClass = rightDocument.getXClass();

                    if (rightDocument.isNew()) {
                        rightClass.addTextField("groups", "groups", 80);
                        rightClass.addTextField("levels", "Access Levels", 80);
                        rightClass.addTextField("users", "Users", 80);
                        rightClass.addBooleanField("allow", "Allow/Deny", "allow");
                        getSpyXWiki().saveDocument(rightDocument, xcontext);
                    }

                    return rightClass;
                }
            }).when(getSpyXWiki()).getRightsClass(anyXWikiContext());

            // GlobalRightsClass
            doAnswer(new Answer<BaseClass>()
            {
                @Override
                public BaseClass answer(InvocationOnMock invocation) throws Throwable
                {
                    XWikiContext xcontext = invocation.getArgument(0);

                    XWikiDocument globalRightDocument = getSpyXWiki().getDocument(
                        new DocumentReference(GLOBAL_RIGHTS_CLASS, new WikiReference(xcontext.getWikiId())), xcontext);

                    final BaseClass globalRightClass = globalRightDocument.getXClass();

                    if (globalRightDocument.isNew()) {
                        globalRightClass.addTextField("groups", "groups", 80);
                        globalRightClass.addTextField("levels", "Access Levels", 80);
                        globalRightClass.addTextField("users", "Users", 80);
                        globalRightClass.addBooleanField("allow", "Allow/Deny", "allow");
                        getSpyXWiki().saveDocument(globalRightDocument, xcontext);
                    }

                    return globalRightClass;
                }
            }).when(getSpyXWiki()).getGlobalRightsClass(anyXWikiContext());
        }

        // DocumentAccessBridge
        if (!this.componentManager.hasComponent(DocumentAccessBridge.class)) {
            this.documentAccessBridge = this.componentManager.registerMockComponent(DocumentAccessBridge.class);
        } else {
            this.documentAccessBridge = this.componentManager.getInstance(DocumentAccessBridge.class);
        }
        if (MockUtil.isMock(this.documentAccessBridge)) {
            when(this.documentAccessBridge.exists(any(DocumentReference.class))).thenAnswer(new Answer<Boolean>()
            {
                @Override
                public Boolean answer(InvocationOnMock invocation) throws Throwable
                {
                    DocumentReference documentReference = invocation.getArgument(0);

                    return spyXWiki.exists(documentReference, context);
                }
            });
        }

        // Query Manager
        // If there's already a Query Manager registered, use it instead.
        // This allows, for example, using @ComponentList to use the real Query Manager, in integration tests.
        if (!this.componentManager.hasComponent(QueryManager.class)) {
            mockQueryManager();
        }
        when(getMockStore().getQueryManager()).then(new Answer<QueryManager>()
        {

            @Override
            public QueryManager answer(InvocationOnMock invocation) throws Throwable
            {
                return getQueryManager();
            }
        });

        // WikiDescriptorManager
        // If there's already a WikiDescriptorManager registered, use it instead.
        // This allows, for example, using @ComponentList to use the real WikiDescriptorManager, in integration tests.
        if (!this.componentManager.hasComponent(WikiDescriptorManager.class)) {
            this.wikiDescriptorManager = getMocker().registerMockComponent(WikiDescriptorManager.class);
            when(this.wikiDescriptorManager.getMainWikiId()).then(new Answer<String>()
            {
                @Override
                public String answer(InvocationOnMock invocation) throws Throwable
                {
                    return getXWikiContext().getMainXWiki();
                }
            });
            when(this.wikiDescriptorManager.getCurrentWikiId()).then(new Answer<String>()
            {
                @Override
                public String answer(InvocationOnMock invocation) throws Throwable
                {
                    return getXWikiContext().getWikiId();
                }
            });
        }

        DefaultParameterizedType userReferenceDocumentReferenceResolverType =
            new DefaultParameterizedType(null, UserReferenceResolver.class, DocumentReference.class);
        if (!this.componentManager.hasComponent(userReferenceDocumentReferenceResolverType, "document")) {
            UserReferenceResolver<DocumentReference> userReferenceResolver =
                getMocker().registerMockComponent(userReferenceDocumentReferenceResolverType, "document");

            DefaultParameterizedType userReferenceDocumentReferenceSerializer =
                new DefaultParameterizedType(null, UserReferenceSerializer.class, DocumentReference.class);
            UserReferenceSerializer<DocumentReference> documentReferenceUserReferenceSerializer;
            if (!this.componentManager.hasComponent(userReferenceDocumentReferenceSerializer, "document")) {
                documentReferenceUserReferenceSerializer =
                    getMocker().registerMockComponent(userReferenceDocumentReferenceSerializer, "document");
            } else {
                documentReferenceUserReferenceSerializer =
                    getMocker().getInstance(userReferenceDocumentReferenceSerializer, "document");
            }

            // we ensure that when trying to resolve a DocumentReference to UserReference, then the returned mock
            // will return the original DocumentReference when resolved back to DocumentReference.
            when(userReferenceResolver.resolve(any())).then(invocationOnMock -> {
                UserReference userReference = mock(UserReference.class);
                when(documentReferenceUserReferenceSerializer.serialize(userReference))
                    .thenReturn(invocationOnMock.getArgument(0));
                return userReference;
            });
        }
    }