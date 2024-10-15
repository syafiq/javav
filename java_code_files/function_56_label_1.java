    public IconSet getIconSet(String name) throws IconException
    {
        // Special case: the default icon theme
        if (DEFAULT_ICONSET_NAME.equals(name)) {
            return getDefaultIconSet();
        }

        // Get the icon set from the cache
        IconSet iconSet = iconSetCache.get(name, wikiDescriptorManager.getCurrentWikiId());

        // Load it if it is not loaded yet
        if (iconSet == null) {
            try {
                // Search by name
                String xwql = "FROM doc.object(IconThemesCode.IconThemeClass) obj WHERE obj.name = :name";
                Query query = queryManager.createQuery(xwql, Query.XWQL);
                query.bindValue("name", name);
                List<String> results = query.execute();
                if (results.isEmpty()) {
                    return null;
                }

                // Get the first result
                String docName = results.get(0);
                DocumentReference docRef = documentReferenceResolver.resolve(docName);

                // Load the icon theme
                iconSet = iconSetLoader.loadIconSet(docRef);

                // Put it in the cache
                iconSetCache.put(docRef, iconSet);
                iconSetCache.put(name, wikiDescriptorManager.getCurrentWikiId(), iconSet);
            } catch (QueryException e) {
                throw new IconException(String.format("Failed to load the icon set [%s].", name), e);
            }
        }

        // Return the icon set
        return iconSet;
    }