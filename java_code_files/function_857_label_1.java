    public List<List<ObjectDiff>> getObjectDiff(XWikiDocument fromDoc, XWikiDocument toDoc, XWikiContext context)
    {
        List<List<ObjectDiff>> difflist = new ArrayList<List<ObjectDiff>>();

        // Since objects could have been deleted or added, we iterate on both the old and the new
        // object collections.
        // First, iterate over the old objects.
        for (List<BaseObject> objects : fromDoc.getXObjects().values()) {
            for (BaseObject originalObj : objects) {
                // This happens when objects are deleted, and the document is still in the cache
                // storage.
                if (originalObj != null) {
                    BaseObject newObj = toDoc.getXObject(originalObj.getXClassReference(), originalObj.getNumber());
                    List<ObjectDiff> dlist;
                    if (newObj == null) {
                        // The object was deleted.
                        dlist = new BaseObject().getDiff(originalObj, context);
                        ObjectDiff deleteMarker =
                            new ObjectDiff(originalObj.getXClassReference(), originalObj.getNumber(),
                                originalObj.getGuid(), ObjectDiff.ACTION_OBJECTREMOVED, "", "", "", "");
                        dlist.add(0, deleteMarker);
                    } else {
                        // The object exists in both versions, but might have been changed.
                        dlist = newObj.getDiff(originalObj, context);
                    }
                    if (!dlist.isEmpty()) {
                        difflist.add(dlist);
                    }
                }
            }
        }

        // Second, iterate over the objects which are only in the new version.
        for (List<BaseObject> objects : toDoc.getXObjects().values()) {
            for (BaseObject newObj : objects) {
                // This happens when objects are deleted, and the document is still in the cache
                // storage.
                if (newObj != null) {
                    BaseObject originalObj = fromDoc.getXObject(newObj.getXClassReference(), newObj.getNumber());
                    if (originalObj == null) {
                        // TODO: Refactor this so that getDiff() accepts null Object as input.
                        // Only consider added objects, the other case was treated above.
                        originalObj = new BaseObject();
                        originalObj.setXClassReference(newObj.getRelativeXClassReference());
                        originalObj.setNumber(newObj.getNumber());
                        originalObj.setGuid(newObj.getGuid());
                        List<ObjectDiff> dlist = newObj.getDiff(originalObj, context);
                        ObjectDiff addMarker = new ObjectDiff(newObj.getXClassReference(), newObj.getNumber(),
                            newObj.getGuid(), ObjectDiff.ACTION_OBJECTADDED, "", "", "", "");
                        dlist.add(0, addMarker);
                        if (!dlist.isEmpty()) {
                            difflist.add(dlist);
                        }
                    }
                }
            }
        }

        return difflist;
    }