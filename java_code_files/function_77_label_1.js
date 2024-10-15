    protected String evaluateTitle(String title, DocumentReference documentReference,
        DocumentDisplayerParameters parameters)
    {
        StringWriter writer = new StringWriter();
        String namespace = defaultEntityReferenceSerializer.serialize(parameters.isTransformationContextIsolated()
            ? documentReference : documentAccessBridge.getCurrentDocumentReference());

        // Get the velocity engine
        VelocityEngine velocityEngine;
        try {
            velocityEngine = this.velocityManager.getVelocityEngine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Execute Velocity code
        Map<String, Object> backupObjects = null;
        boolean canPop = false;
        EntityReference currentWikiReference = this.modelContext.getCurrentEntityReference();
        try {
            if (parameters.isExecutionContextIsolated()) {
                backupObjects = new HashMap<>();
                // The following method call also clones the execution context.
                documentAccessBridge.pushDocumentInContext(backupObjects, documentReference);
                // Pop the document from the context only if the push was successful!
                canPop = true;
                // Make sure to synchronize the context wiki with the context document's wiki.
                modelContext.setCurrentEntityReference(documentReference.getWikiReference());
            }
            velocityEngine.evaluate(velocityManager.getVelocityContext(), writer, namespace, title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (canPop) {
                documentAccessBridge.popDocumentFromContext(backupObjects);
                // Also restore the context wiki.
                this.modelContext.setCurrentEntityReference(currentWikiReference);
            }
        }
        return writer.toString();
    }