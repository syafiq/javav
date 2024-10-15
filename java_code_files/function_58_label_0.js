    public void render() throws Exception
    {
        IconSet iconSet = new IconSet("default");
        iconSet.setRenderWiki("image:$icon.png");
        iconSet.addIcon("test", new Icon("blabla"));
        when(this.velocityRenderer.render("#set($icon = \"blabla\")\nimage:$icon.png", null))
            .thenReturn("image:blabla.png");

        // Test
        String result = iconRenderer.render("test", iconSet);

        // Verify
        assertEquals("image:blabla.png", result);
        verify(linkExtension, never()).use(any());
        verify(skinExtension, never()).use(any());
        verify(jsExtension, never()).use(any());
    }