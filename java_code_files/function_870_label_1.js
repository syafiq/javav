    public Map<String, String> get()
    {
        boolean isCacheValid = false;

        // Even though the parameters are dynamic, we cache a rendered version of them in order to improve performance.
        // This cache has a short lifespan, it gets discarded for each new request, or if the database has been switched
        // during a request.
        int currentContextId = this.execution.getContext().hashCode();
        String currentWiki = modelContext.getCurrentEntityReference().extractReference(EntityType.WIKI).getName();
        if (currentContextId == this.previousContextId
                && currentWiki.equals(previousWiki) && this.evaluatedParameters != null)
        {
            isCacheValid = true;
        }

        if (!isCacheValid) {
            this.evaluatedParameters = new HashMap<>();

            if (this.parameters.size() > 0) {
                try {
                    VelocityEngine velocityEngine = this.velocityManager.getVelocityEngine();
                    VelocityContext velocityContext = this.velocityManager.getVelocityContext();

                    for (String propertyKey : this.parameters.stringPropertyNames()) {
                        if (!StringUtils.isBlank(propertyKey)) {
                            String propertyValue = this.parameters.getProperty(propertyKey);
                            StringWriter writer = new StringWriter();
                            try {
                                String namespace = this.id + ':' + propertyKey;
                                velocityEngine.evaluate(
                                    new XWikiVelocityContext(velocityContext,
                                        this.loggerConfiguration.isDeprecatedLogEnabled()),
                                    writer, namespace, propertyValue);
                                this.evaluatedParameters.put(propertyKey, writer.toString());
                            } catch (XWikiVelocityException e) {
                                LOGGER.warn(String.format(
                                    "Failed to evaluate UI extension data value, key [%s], value [%s]. Reason: [%s]",
                                    propertyKey, propertyValue, e.getMessage()));
                            }
                        }
                    }
                } catch (XWikiVelocityException ex) {
                    LOGGER.warn(String.format("Failed to get velocity engine. Reason: [%s]", ex.getMessage()));
                }
                this.previousContextId = currentContextId;
                this.previousWiki = currentWiki;
            }
        }

        return this.evaluatedParameters;
    }