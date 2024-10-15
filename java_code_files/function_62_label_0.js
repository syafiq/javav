    public void renderWithException() throws Exception
    {
        IconSet iconSet = new IconSet("default");
        iconSet.setRenderWiki("image:$icon.png");
        iconSet.addIcon("test", new Icon("blabla"));
        IconException exception = new IconException("exception");
        when(this.velocityRenderer.render(any(), any())).thenThrow(exception);

        // Test
        IconException caughtException = null;
        try {
            iconRenderer.render("test", iconSet);
        } catch (IconException e) {
            caughtException = e;
        }

        // Verify
        assertNotNull(caughtException);
        assertEquals(exception, caughtException);
    }