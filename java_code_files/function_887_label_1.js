    public void beforeEach() throws ComponentLookupException
    {
        this.oldcore.getExecutionContext().setProperty(VelocityExecutionContextInitializer.VELOCITY_CONTEXT_ID,
            new VelocityContext());
    }