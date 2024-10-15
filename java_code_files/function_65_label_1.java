    public void renderIcon() throws Exception
    {
        IconSet iconSet = new IconSet("iconSet");
        iconSet.addIcon("test", new Icon("hello"));
        when(velocityRenderer.render("#set($icon = \"hello\")\nfa fa-$icon")).thenReturn("fa fa-hello");

        // Test
        String renderedIcon1 = iconRenderer.render("test", iconSet, "fa fa-$icon");
        String renderedIcon2 = iconRenderer.render("none", iconSet, "fa fa-$icon");
        String renderedIcon3 = iconRenderer.render("none", null, "fa fa-$icon");
        String renderedIcon4 = iconRenderer.render("none", iconSet, null);

        // Verify
        assertEquals("fa fa-hello", renderedIcon1);
        assertEquals("", renderedIcon2);
        assertEquals("", renderedIcon3);
        assertEquals("", renderedIcon4);
    }