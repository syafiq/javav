    public WikiUIExtensionParameters(String id, String rawParameters, ComponentManager cm)
        throws WikiComponentException
    {
        this.id = id;
        this.parameters = parseParameters(rawParameters);

        try {
            this.execution = cm.getInstance(Execution.class);
            this.velocityManager = cm.getInstance(VelocityManager.class);
            this.modelContext = cm.getInstance(ModelContext.class);
            this.loggerConfiguration = cm.getInstance(LoggerConfiguration.class);
        } catch (ComponentLookupException e) {
            throw new WikiComponentException(
                "Failed to get an instance for a component role required by Wiki Components.", e);
        }
    }