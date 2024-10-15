    public String render(String code) throws IconException
    {
        // The macro namespace to use by the velocity engine, see afterwards.
        String namespace = "IconVelocityRenderer_" + Thread.currentThread().getId();

        // Create the output writer
        StringWriter output = new StringWriter();

        VelocityEngine engine = null;
        try {
            // Get the velocity engine
            engine = velocityManager.getVelocityEngine();

            // Use a new macro namespace to prevent the code redefining existing macro.
            // We use the thread name to have a unique id.
            engine.startedUsingMacroNamespace(namespace);

            // Create a new VelocityContext to prevent the code creating variables in the current context.
            // See https://jira.xwiki.org/browse/XWIKI-11400.
            // We set the current context as inner context of the new one to be able to read existing variables.
            // See https://jira.xwiki.org/browse/XWIKI-11426.
            VelocityContext context = new XWikiVelocityContext(velocityManager.getVelocityContext(),
                this.loggerConfiguration.isDeprecatedLogEnabled());

            // Render the code
            if (engine.evaluate(context, output, "DefaultIconRenderer", code)) {
                return output.toString();
            } else {
                // I don't know how to check the velocity runtime log
                throw new IconException("Failed to render the icon. See the Velocity runtime log.", null);
            }
        } catch (XWikiVelocityException e) {
            throw new IconException("Failed to render the icon.", e);
        } finally {
            // Do not forget to close the macro namespace we have created previously
            if (engine != null) {
                engine.stoppedUsingMacroNamespace(namespace);
            }
        }
    }