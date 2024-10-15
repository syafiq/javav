    public void renderHTMLWithCSS() throws Exception
    {
        IconSet iconSet = new IconSet("default");
        iconSet.setRenderHTML("<img src=\"$icon.png\" />");
        iconSet.setCss("css");
        iconSet.addIcon("test", new Icon("blabla"));
        when(velocityRenderer.render("css")).thenReturn("velocityParsedCSS");

        // Test
        iconRenderer.renderHTML("test", iconSet);

        // Verify
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rel", "stylesheet");
        verify(linkExtension).use(eq("velocityParsedCSS"), eq(parameters));
        verify(skinExtension, never()).use(any());
        verify(jsExtension, never()).use(any());
    }