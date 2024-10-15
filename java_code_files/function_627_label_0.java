    protected void setPropertyValue(SolrInputDocument solrDocument, BaseProperty<?> property,
        PropertyClass propertyClass, Locale locale)
    {
        Object propertyValue = property.getValue();
        if (propertyClass instanceof StaticListClass) {
            setStaticListPropertyValue(solrDocument, property, (StaticListClass) propertyClass, locale);
        } else if (propertyClass instanceof TextAreaClass
            || (propertyClass != null && "String".equals(propertyClass.getClassType()))
            || (propertyValue instanceof CharSequence
                && String.valueOf(propertyValue).length() > getShortTextLimit())) {
            // Index TextArea and String properties as text, based on the document locale. We didn't check if the
            // property class is an instance of StringClass because it has subclasses that don't store free text (like
            // the EmailClass). Plus we didn't want to include the PasswordClass (which extends StringClass).
            //
            // We also index large strings as localized text in order to cover custom XClass properties that may not
            // extend TextArea but still have large strings as value, and also the case when a TextArea property is
            // removed from an XClass but there are still objects that have a (large) value set for it (the property
            // class is null in this case). The 255 limit is defined in xwiki.hbm.xml for string properties.

            // It's important here to make sure we give strings to Solr, as it can mutate the value we give it,
            // so we need to make sure we don't endanger the state of the document
            setPropertyValue(solrDocument, property, new TypedValue(String.valueOf(propertyValue), TypedValue.TEXT),
                locale);

            if (!(propertyClass instanceof TextAreaClass)
                && String.valueOf(propertyValue).length() <= getShortTextLimit()) {
                // Also index the raw value that is saved in the database. This provide a stable field name and also
                // allows exact matching
                setPropertyValue(solrDocument, property, new TypedValue(propertyValue), locale);
            }
        } else if (propertyValue instanceof Collection) {
            // We iterate the collection instead of giving it to Solr because, although it supports passing collections,
            // it reuses the collection in some cases, when the value of a field is set for the first time for instance,
            // which can lead to side effects on our side.
            for (Object value : (Collection<?>) propertyValue) {
                if (value != null) {
                    // Avoid indexing null values.
                    setPropertyValue(solrDocument, property, new TypedValue(value), locale);
                }
            }
        } else if (propertyValue instanceof Integer && propertyClass instanceof BooleanClass) {
            // Boolean properties are stored as integers (0 is false and 1 is true).
            Boolean booleanValue = ((Integer) propertyValue) != 0;
            setPropertyValue(solrDocument, property, new TypedValue(booleanValue), locale);
        } else if (!(propertyClass instanceof PasswordClass)
            && !((propertyClass instanceof EmailClass) && this.generalMailConfiguration.shouldObfuscate()))
        {
            // Avoid indexing passwords and, when obfuscation is enabled, emails.
            setPropertyValue(solrDocument, property, new TypedValue(propertyValue), locale);
        }
    }