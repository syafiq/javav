    private void activeCSS(IconSet iconSet) throws IconException
    {
        String url = velocityRenderer.render(iconSet.getCss());
        Map<String, Object> parameters = new HashMap();
        parameters.put("rel", "stylesheet");
        linkExtension.use(url, parameters);
    }