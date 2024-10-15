    public void renderWithCSS() throws Exception
    {
        IconSet iconSet = new IconSet("default");
        iconSet.setRenderWiki("image:$icon.png");
        iconSet.setCss("css");
        iconSet.addIcon("test", new Icon("blabla"));
        when(velocityRenderer.render("css")).thenReturn("velocityParsedCSS");

        // Test
        iconRenderer.render("test", iconSet);

        // Verify
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rel", "stylesheet");
        verify(linkExtension).use(eq("velocityParsedCSS"), eq(parameters));
        verify(skinExtension, never()).use(any());
        verify(jsExtension, never()).use(any());
    }