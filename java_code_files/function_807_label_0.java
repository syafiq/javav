    void isEditWithChangeRequestAllowed() throws ChangeRequestException, XWikiException
    {
        Right crRight = ChangeRequestRight.getRight();
        UserReference userReference = mock(UserReference.class);
        DocumentReference userDocReference = mock(DocumentReference.class);
        DocumentReference documentReference = mock(DocumentReference.class);
        XWikiDocument document = mock(XWikiDocument.class);
        XWiki wiki = mock(XWiki.class);
        XWikiContext context = mock(XWikiContext.class);

        when(this.userReferenceConverter.convert(userReference)).thenReturn(userDocReference);
        when(this.contextProvider.get()).thenReturn(context);
        when(context.getWiki()).thenReturn(wiki);
        when(wiki.getDocument(documentReference, context)).thenReturn(document);
        when(this.authorizationManager.hasAccess(crRight, userDocReference, documentReference)).thenReturn(false);

        assertFalse(this.rightsManager.isEditWithChangeRequestAllowed(userReference, documentReference));

        verify(this.authorizationManager).hasAccess(crRight, userDocReference, documentReference);
        verifyNoInteractions(document);

        when(this.authorizationManager.hasAccess(crRight, userDocReference, documentReference)).thenReturn(true);
        when(document.isNew()).thenReturn(true);
        assertTrue(this.rightsManager.isEditWithChangeRequestAllowed(userReference, documentReference));

        verify(this.authorizationManager, times(2)).hasAccess(crRight, userDocReference, documentReference);

        when(document.isNew()).thenReturn(false);

        DocumentReference objReference1 = mock(DocumentReference.class);
        DocumentReference objReference2 = mock(DocumentReference.class);

        BaseObject baseObject1 = mock(BaseObject.class);
        BaseObject baseObject2 = mock(BaseObject.class);
        BaseObject baseObject3 = mock(BaseObject.class);

        List<BaseObject> baseObjectList1 = Arrays.asList(null, baseObject1);
        List<BaseObject> baseObjectList2 = Arrays.asList(null, baseObject2, baseObject3);

        Map<DocumentReference, List<BaseObject>> objectMap = new LinkedHashMap<>();
        objectMap.put(objReference1, baseObjectList1);
        objectMap.put(objReference2, baseObjectList2);

        when(document.getXObjects()).thenReturn(objectMap);
        BaseClass class1 = mock(BaseClass.class);
        List<PropertyClass> propertyClasses = List.of(mock(PropertyClass.class), mock(PropertyClass.class),
            mock(PasswordClass.class));
        when(class1.getFieldList()).thenReturn(propertyClasses);
        when(baseObject1.getXClass(context)).thenReturn(class1);

        assertFalse(this.rightsManager.isEditWithChangeRequestAllowed(userReference, documentReference));
        verify(baseObject1).getXClass(context);
        verifyNoInteractions(baseObject2);
        verifyNoInteractions(baseObject3);

        when(class1.getFieldList()).thenReturn(List.of(mock(PropertyClass.class)));
        BaseClass class2 = mock(BaseClass.class);
        when(class2.getFieldList()).thenReturn(List.of(mock(PasswordClass.class)));
        when(baseObject2.getXClass(context)).thenReturn(class2);

        assertFalse(this.rightsManager.isEditWithChangeRequestAllowed(userReference, documentReference));
        verify(baseObject1, times(2)).getXClass(context);
        verify(baseObject2).getXClass(context);
        verifyNoInteractions(baseObject3);

        when(class2.getFieldList()).thenReturn(List.of());
        assertTrue(this.rightsManager.isEditWithChangeRequestAllowed(userReference, documentReference));
        verify(baseObject1, times(3)).getXClass(context);
        verify(baseObject2, times(2)).getXClass(context);
        verifyNoInteractions(baseObject3);
    }