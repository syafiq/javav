    void setUp() throws Exception
    {
        // Initialize the UIExtension class.
        this.xwiki.initializeMandatoryDocuments(this.context);

        when(this.oldcore.getMockAuthorizationManager().hasAccess(any(), eq(ADMIN_REFERENCE), any())).thenReturn(true);

        // Register the component manager as wiki component manager so that the UIX can be registered on the wiki.
        this.componentManager.registerComponent(ComponentManager.class, "wiki", this.componentManager);
    }