    public void renderHTML() throws Exception
    {
        IconSet iconSet = new IconSet("default");
        iconSet.setRenderHTML("<img src=\"$icon.png\" />");
        iconSet.addIcon("test", new Icon("blabla"));

        when(velocityRenderer.render("#set($icon = \"blabla\")\n<img src=\"$icon.png\" />"))
            .thenReturn("<img src=\"blabla.png\" />");

        // Test
        String result = iconRenderer.renderHTML("test", iconSet);

        // Verify
        assertEquals("<img src=\"blabla.png\" />", result);
    }