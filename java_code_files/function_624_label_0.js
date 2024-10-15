    protected void setPropertyValue(SolrInputDocument solrDocument, BaseProperty<?> property,
        TypedValue typedValue, Locale locale)
    {
        // Collect all the property values from all the objects of a document in a single (localized) field.
        String fieldName = FieldUtils.getFieldName(FieldUtils.OBJECT_CONTENT, locale);
        String fieldValue = String.format(OBJCONTENT_FORMAT, property.getName(), typedValue.getValue());
        // The current method can be called multiple times for the same property value (but with a different type).
        // Since we don't care about the value type here (all the values are collected in a localized field) we need to
        // make sure we don't add the same value twice. Derived classes can override this method and use the value type.
        addFieldValueOnce(solrDocument, fieldName, fieldValue);
    }