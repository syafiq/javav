    void setUp() throws Exception
    {
        this.oldcore.notifyDocumentCreatedEvent(true);

        when(this.mockQueryManager.createQuery(anyString(), anyString())).thenReturn(this.mockQuery);
        when(this.mockQuery.execute()).thenReturn(Collections.emptyList());

        when(this.componentManagerManager.getComponentManager("wiki:xwiki", true)).thenReturn(this.oldcore.getMocker());
        this.oldcore.getMocker().getInstance(TranslationBundleFactory.class, DocumentTranslationBundleFactory.ID);

        XWikiDocument translationRootDocument = this.oldcore.getSpyXWiki().getDocument(TRANSLATION_ROOT_REFERENCE,
            this.oldcore.getXWikiContext());

        BaseObject translationObject = translationRootDocument.newXObject(
            new DocumentReference("xwiki", "XWiki", "TranslationDocumentClass"),
            this.oldcore.getXWikiContext());
        translationObject.setStringValue(TranslationDocumentModel.TRANSLATIONCLASS_PROP_SCOPE,
            TranslationDocumentModel.Scope.WIKI.toString());

        translationRootDocument.setSyntax(Syntax.PLAIN_1_0);
        translationRootDocument.setContent("xwiki.translation=root");
        translationRootDocument.setAuthorReference(ADMIN_USER_REFERENCE);
        this.oldcore.getSpyXWiki().saveDocument(translationRootDocument, this.oldcore.getXWikiContext());

        this.translationFrDocument = translationRootDocument.getTranslatedDocument(Locale.FRENCH,
            this.oldcore.getXWikiContext());
        if (this.translationFrDocument == translationRootDocument) {
            this.translationFrDocument =
                new XWikiDocument(this.translationFrDocument.getDocumentReference(), Locale.FRENCH);
            this.translationFrDocument.setDefaultLocale(this.translationFrDocument.getDefaultLocale());
        }
        this.translationFrDocument.setSyntax(Syntax.PLAIN_1_0);
        this.translationFrDocument.setContent("xwiki.translation=fr");
        this.oldcore.getSpyXWiki().saveDocument(this.translationFrDocument, this.oldcore.getXWikiContext());

        doThrow(new AccessDeniedException(Right.SCRIPT, null, translationRootDocument.getDocumentReference()))
            .when(this.oldcore.getMockAuthorizationManager()).checkAccess(Right.ADMIN, null,
                TRANSLATION_ROOT_REFERENCE.getWikiReference());
    }