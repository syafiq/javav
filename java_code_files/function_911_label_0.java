    private String convertToStrictXHtml(String input)
    {
        LOGGER.debug("Cleaning HTML:\n{}", input);

        HTMLCleaner cleaner = Utils.getComponent(HTMLCleaner.class);
        HTMLCleanerConfiguration config = cleaner.getDefaultConfiguration();
        List<HTMLFilter> filters = new ArrayList<>(config.getFilters());
        filters.add(Utils.getComponent(HTMLFilter.class, "uniqueId"));
        config.setFilters(filters);
        String result = HTMLUtils.toString(cleaner.clean(new StringReader(input), config));
        LOGGER.debug("Cleaned XHTML:\n{}", result);
        return result;
    }