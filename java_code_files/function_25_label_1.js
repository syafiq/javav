    void getIconsIconManagerException() throws Exception
    {
        IconException iconException = new IconException("icon error", null);

        when(this.iconManager.hasIcon(any(), any())).thenThrow(iconException);
        when(this.iconSetManager.getCurrentIconSet()).thenReturn(new IconSet("testTheme"));

        List<String> names = asList("iconA", "unknownIcon", "iconB");
        WebApplicationException webApplicationException =
            assertThrows(WebApplicationException.class, () -> this.iconThemesResource.getIcons("wikiTest", names));

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), webApplicationException.getResponse().getStatus());
        assertEquals(iconException, webApplicationException.getCause());

        verify(this.modelContext).setCurrentEntityReference(new WikiReference("wikiTest"));
        verify(this.modelContext).setCurrentEntityReference(CURRENT_ENTITY);
        verify(this.iconSetManager, never()).getIconSet(any());
        verify(this.iconSetManager, never()).getDefaultIconSet();
    }