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

        // Inject skin macros
        if (skinMacrosTemplate != null) {
            VelocityTemplate skinMacros = compile("", new StringReader(skinMacrosTemplate.getContent().getContent()));

            velocityEngine.addGlobalMacros(skinMacros.getMacros());
        }
    }