    public List<ObjectDiff> getDiff(Object oldEntity, XWikiContext context)
    {
        ArrayList<ObjectDiff> difflist = new ArrayList<ObjectDiff>();
        BaseObject oldObject = (BaseObject) oldEntity;
        // Iterate over the new properties first, to handle changed and added objects
        for (String propertyName : this.getPropertyList()) {
            BaseProperty newProperty = (BaseProperty) this.getField(propertyName);
            BaseProperty oldProperty = (BaseProperty) oldObject.getField(propertyName);
            BaseClass bclass = getXClass(context);
            PropertyClass pclass = (PropertyClass) ((bclass == null) ? null : bclass.getField(propertyName));
            String propertyType = (pclass == null) ? "" : pclass.getClassType();

            if (oldProperty == null) {
                // The property exist in the new object, but not in the old one
                if ((newProperty != null) && (!newProperty.toText().equals(""))) {
                    String newPropertyValue = (newProperty.getValue() instanceof String || pclass == null)
                        ? newProperty.toText() : pclass.displayView(propertyName, this, context);
                    difflist.add(new ObjectDiff(getXClassReference(), getNumber(), getGuid(),
                        ObjectDiff.ACTION_PROPERTYADDED, propertyName, propertyType, "", newPropertyValue));
                }
            } else if (!oldProperty.toText().equals(((newProperty == null) ? "" : newProperty.toText()))) {
                // The property exists in both objects and is different
                if (pclass != null) {
                    // Put the values as they would be displayed in the interface
                    String newPropertyValue = (newProperty.getValue() instanceof String) ? newProperty.toText()
                        : pclass.displayView(propertyName, this, context);
                    String oldPropertyValue = (oldProperty.getValue() instanceof String) ? oldProperty.toText()
                        : pclass.displayView(propertyName, oldObject, context);
                    difflist.add(
                        new ObjectDiff(getXClassReference(), getNumber(), getGuid(), ObjectDiff.ACTION_PROPERTYCHANGED,
                            propertyName, propertyType, oldPropertyValue, newPropertyValue));
                } else {
                    // Cannot get property definition, so use the plain value
                    difflist.add(
                        new ObjectDiff(getXClassReference(), getNumber(), getGuid(), ObjectDiff.ACTION_PROPERTYCHANGED,
                            propertyName, propertyType, oldProperty.toText(), newProperty.toText()));
                }
            }
        }

        // Iterate over the old properties, in case there are some removed properties
        for (String propertyName : oldObject.getPropertyList()) {
            BaseProperty newProperty = (BaseProperty) this.getField(propertyName);
            BaseProperty oldProperty = (BaseProperty) oldObject.getField(propertyName);
            BaseClass bclass = getXClass(context);
            // Bulletproofing: in theory the BaseObject is defined with a xclass reference allowing to resolve it
            // however, it's possible that the reference is not set, in which case we might still find the info
            // in the old object.
            if (bclass == null) {
                bclass = oldObject.getXClass(context);
            }
            PropertyClass pclass = (PropertyClass) ((bclass == null) ? null : bclass.getField(propertyName));
            String propertyType = (pclass == null) ? "" : pclass.getClassType();

            if (newProperty == null) {
                // The property exists in the old object, but not in the new one
                if ((oldProperty != null) && (!oldProperty.toText().equals(""))) {
                    if (pclass != null) {
                        // Put the values as they would be displayed in the interface
                        String oldPropertyValue = (oldProperty.getValue() instanceof String) ? oldProperty.toText()
                            : pclass.displayView(propertyName, oldObject, context);
                        difflist.add(
                            new ObjectDiff(oldObject.getXClassReference(), oldObject.getNumber(), oldObject.getGuid(),
                                ObjectDiff.ACTION_PROPERTYREMOVED, propertyName, propertyType, oldPropertyValue, ""));
                    } else {
                        // Cannot get property definition, so use the plain value
                        difflist.add(new ObjectDiff(oldObject.getXClassReference(), oldObject.getNumber(),
                            oldObject.getGuid(), ObjectDiff.ACTION_PROPERTYREMOVED, propertyName, propertyType,
                            oldProperty.toText(), ""));
                    }
                }
            }
        }

        return difflist;
    }