    public Map<String, String> get()
    {
        Map<String, String> result;

        // Even though the parameters are dynamic, we cache a rendered version of them in order to improve performance.
        // This cache has a short lifespan, it gets discarded for each new request, or if the database has been switched
        // during a request.
        int currentContextId = this.execution.getContext().hashCode();
        String currentWiki = modelContext.getCurrentEntityReference().extractReference(EntityType.WIKI).getName();
        if (currentContextId == this.previousContextId
                && currentWiki.equals(previousWiki) && this.evaluatedParameters != null)
        {
            result = this.evaluatedParameters;
        } else {
            result = this.parameters.stringPropertyNames().stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toMap(Function.identity(), this.parameters::getProperty));

            if (!this.parameters.isEmpty()
                && this.authorizationManager.hasAccess(Right.SCRIPT, this.authorReference, this.documentReference))
            {
                try {
                    this.authorExecutor.call(() -> {
                        VelocityEngine velocityEngine = this.velocityManager.getVelocityEngine();
                        VelocityContext velocityContext = this.velocityManager.getVelocityContext();

                        result.replaceAll((propertyKey, propertyValue) -> {
                            StringWriter writer = new StringWriter();
                            try {
                                String namespace = this.id + ':' + propertyKey;
                                velocityEngine.evaluate(
                                    new XWikiVelocityContext(velocityContext,
                                        this.loggerConfiguration.isDeprecatedLogEnabled()),
                                    writer, namespace, propertyValue);
                                return writer.toString();
                            } catch (XWikiVelocityException e) {
                                LOGGER.warn(String.format(
                                    "Failed to evaluate UI extension data value, key [%s], value [%s]. Reason: [%s]",
                                    propertyKey, propertyValue, e.getMessage()));
                            }

                            return propertyValue;
                        });

                        return null;
                    }, this.authorReference, this.documentReference);
                } catch (Exception ex) {
                    LOGGER.warn(String.format("Failed to get velocity engine. Reason: [%s]", ex.getMessage()));
                }
            }

            this.evaluatedParameters = result;
            this.previousContextId = currentContextId;
            this.previousWiki = currentWiki;
        }

        return result;
    }