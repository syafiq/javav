    private void injectBaseMacros(VelocityEngine velocityEngine, Template skinMacrosTemplate) throws Exception
    {
        // Inject main macros
        try (InputStream stream = this.environment.getResourceAsStream("/templates/macros.vm")) {
            if (stream != null) {
                try (InputStreamReader reader = new InputStreamReader(stream)) {
                    VelocityTemplate mainMacros = compile("", reader);

                    velocityEngine.addGlobalMacros(mainMacros.getMacros());
                }
            }
        }

        // Inject skin macros if their author has at least Script rights.
        if (skinMacrosTemplate != null
            && this.authorizationManager.hasAccess(Right.SCRIPT, skinMacrosTemplate.getContent().getAuthorReference(),
            skinMacrosTemplate.getContent().getDocumentReference()))
        {
            VelocityTemplate skinMacros =
                compile("", new StringReader(skinMacrosTemplate.getContent().getContent()));
            velocityEngine.addGlobalMacros(skinMacros.getMacros());
        }
    }