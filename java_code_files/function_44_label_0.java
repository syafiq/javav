    public <V> V call(Callable<V> callable, DocumentModelBridge document) throws Exception
    {
        Map<String, Object> backupObjects = new HashMap<>();
        EntityReference currentWikiReference = this.modelContext.getCurrentEntityReference();
        boolean canPop = false;

        try {
            this.documentAccessBridge.pushDocumentInContext(backupObjects, document);
            canPop = true;
            // Make sure to synchronize the context wiki with the context document's wiki.
            this.modelContext.setCurrentEntityReference(document.getDocumentReference().getWikiReference());

            return callable.call();
        } finally {
            if (canPop) {
                this.documentAccessBridge.popDocumentFromContext(backupObjects);
                // Also restore the context wiki.
                this.modelContext.setCurrentEntityReference(currentWikiReference);
            }
        }
    }