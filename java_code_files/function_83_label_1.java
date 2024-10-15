    public void setup() throws Exception
    {
        this.xwiki = this.oldcore.getSpyXWiki();

        DocumentReference documentReference = new DocumentReference(DOCWIKI, DOCSPACE, DOCNAME);
        this.document = new XWikiDocument(documentReference);
        this.document.setSyntax(Syntax.XWIKI_2_1);
        this.document.setNew(false);

        this.oldcore.getXWikiContext().setDoc(this.document);

        this.baseClass = this.document.getXClass();
        this.baseClass.addTextField("string", "String", 30);
        this.baseClass.addTextAreaField("area", "Area", 10, 10);
        this.baseClass.addTextAreaField("puretextarea", "Pure text area", 10, 10);
        // set the text areas an non interpreted content
        ((TextAreaClass) this.baseClass.getField("puretextarea")).setContentType("puretext");
        this.baseClass.addPasswordField("passwd", "Password", 30);
        this.baseClass.addBooleanField("boolean", "Boolean", "yesno");
        this.baseClass.addNumberField("int", "Int", 10, "integer");
        this.baseClass.addStaticListField("stringlist", "StringList", "value1, value2");

        doReturn(this.baseClass).when(this.xwiki).getXClass(any(), any());

        this.baseObject = this.document.newObject(CLASSNAME, this.oldcore.getXWikiContext());
        this.baseObject.setStringValue("string", "string");
        this.baseObject.setLargeStringValue("area", "area");
        this.baseObject.setStringValue("passwd", "passwd");
        this.baseObject.setIntValue("boolean", 1);
        this.baseObject.setIntValue("int", 42);
        this.baseObject.setStringListValue("stringlist", Arrays.asList("VALUE1", "VALUE2"));

        this.oldcore.getXWikiContext().put("isInRenderingEngine", true);

        when(this.oldcore.getMockAuthorizationManager().hasAccess(any(), any(), any())).thenReturn(true);
        when(this.oldcore.getMockContextualAuthorizationManager().hasAccess(any())).thenReturn(true);
        when(this.xwiki.getRightService().hasProgrammingRights(any())).thenReturn(true);

        this.componentManager
            .registerComponent(ConfigurationSource.class, "xwikicfg", this.oldcore.getConfigurationSource());
    }