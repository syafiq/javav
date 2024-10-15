    public void use(IconSet iconSet) throws IconException
    {
        if (iconSet == null) {
            throw new IconException("The icon set is null", null);
        }
        if (!StringUtils.isBlank(iconSet.getCss())) {
            activeCSS(iconSet);
        }
        if (!StringUtils.isBlank(iconSet.getSsx())) {
            activeSSX(iconSet);
        }
        if (!StringUtils.isBlank(iconSet.getJsx())) {
            activeJSX(iconSet);
        }
    }