    private XWikiDocument cloneInternal(DocumentReference newDocumentReference,
        boolean keepsIdentity,
        boolean cloneArchive)
    {
        XWikiDocument doc = null;

        try {
            Constructor<? extends XWikiDocument> constructor = getClass().getConstructor(DocumentReference.class);
            doc = constructor.newInstance(newDocumentReference);

            // Make sure the coordinate of the document is fully accurate before any other manipulation
            doc.setLocale(getLocale());

            // use version field instead of getRCSVersion because it returns "1.1" if version==null.
            doc.version = this.version;
            doc.id = this.id;
            if (cloneArchive) {
                doc.cloneDocumentArchive(this);
            } else {
                // Without this explicit initialization, it is possible for the archive to be incorrectly initialized.
                // For instance, with the archive of the cloned document.
                // Here we guarantee that further calls of APIs to get the archive will properly populate the data.
                doc.setDocumentArchive((XWikiDocumentArchive) null);
            }
            doc.getAuthors().copyAuthors(getAuthors());
            doc.setContent(getContent());
            doc.setCreationDate(getCreationDate());
            doc.setDate(getDate());
            doc.setCustomClass(getCustomClass());
            doc.setContentUpdateDate(getContentUpdateDate());
            doc.setTitle(getTitle());
            doc.setFormat(getFormat());
            doc.setFromCache(isFromCache());
            doc.setElements(getElements());
            doc.setMeta(getMeta());
            doc.setMostRecent(isMostRecent());
            doc.setNew(isNew());
            doc.setStore(getStore());
            doc.setTemplateDocumentReference(getTemplateDocumentReference());
            doc.setParentReference(getRelativeParentReference());
            doc.setDefaultLocale(getDefaultLocale());
            doc.setDefaultTemplate(getDefaultTemplate());
            doc.setValidationScript(getValidationScript());
            doc.setComment(getComment());
            doc.setMinorEdit(isMinorEdit());
            doc.setSyntax(getSyntax());
            doc.setHidden(isHidden());
            doc.setRestricted(isRestricted());

            if (this.xClass != null) {
                doc.setXClass(this.xClass.clone());
            }

            if (keepsIdentity) {
                doc.setXClassXML(getXClassXML());
                doc.cloneXObjects(this);
                doc.cloneAttachments(this);
            } else {
                doc.getXClass().setCustomMapping(null);
                doc.duplicateXObjects(this);
                doc.copyAttachments(this);
            }

            doc.setContentDirty(isContentDirty());
            doc.setMetaDataDirty(isMetaDataDirty());

            doc.elements = this.elements;

            doc.originalDocument = this.originalDocument;
        } catch (Exception e) {
            // This should not happen
            LOGGER.error("Exception while cloning document", e);
        }
        return doc;
    }