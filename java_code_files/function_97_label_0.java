    void contentAuthorRightPreAccess(Right right)
    {
        when(this.authorizationManager.hasAccess(eq(right), any(), any())).thenReturn(true);

        assertTrue(this.contextualAuthorizationManager.hasAccess(right));

        // Check restricted rendering context (once).
        when(this.renderingContext.isRestricted()).thenReturn(true).thenReturn(false);
        assertFalse(this.contextualAuthorizationManager.hasAccess(right));

        XWikiDocument contextDocument = mock(XWikiDocument.class);
        this.oldcore.getXWikiContext().setDoc(contextDocument);
        assertTrue(this.contextualAuthorizationManager.hasAccess(right));
        verify(contextDocument).isRestricted();
        // Check restricted document denies script right.
        when(contextDocument.isRestricted()).thenReturn(true).thenReturn(false);
        assertFalse(this.contextualAuthorizationManager.hasAccess(right));

        // Check dropping permissions keeps script but not programming right
        this.oldcore.getXWikiContext().dropPermissions();
        if (right == Right.PROGRAM) {
            assertFalse(this.contextualAuthorizationManager.hasAccess(right));
        } else {
            assertTrue(this.contextualAuthorizationManager.hasAccess(right));
        }
    }