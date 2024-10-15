    private static String generate(String language, GeneratorInput opts, Type type) {
        LOGGER.debug(String.format(Locale.ROOT,"generate %s for %s", type.getTypeName(), language));
        if (opts == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No options were supplied");
        }
        JsonNode node = opts.getSpec();
        if (node != null && "{}".equals(node.toString())) {
            LOGGER.debug("ignoring empty spec");
            node = null;
        }
        OpenAPI openapi;
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        if (node == null) {
            if (opts.getOpenAPIUrl() != null) {
                if (opts.getAuthorizationValue() != null) {
                    List<AuthorizationValue> authorizationValues = new ArrayList<>();
                    authorizationValues.add(opts.getAuthorizationValue());
                    openapi = new OpenAPIParser().readLocation(opts.getOpenAPIUrl(), authorizationValues, parseOptions).getOpenAPI();
                } else {
                    openapi = new OpenAPIParser().readLocation(opts.getOpenAPIUrl(), null, parseOptions).getOpenAPI();
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No OpenAPI specification was supplied");
            }
        } else if (opts.getAuthorizationValue() != null) {
            List<AuthorizationValue> authorizationValues = new ArrayList<>();
            authorizationValues.add(opts.getAuthorizationValue());
            openapi = new OpenAPIParser().readContents(node.toString(), authorizationValues, parseOptions).getOpenAPI();

        } else {
            openapi = new OpenAPIParser().readContents(node.toString(), null, parseOptions).getOpenAPI();
        }
        if (openapi == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The OpenAPI specification supplied was not valid");
        }

        String destPath = null;

        if (opts.getOptions() != null) {
            destPath = opts.getOptions().get("outputFolder");
        }
        if (destPath == null) {
            destPath = language + "-" + type.getTypeName();
        }

        ClientOptInput clientOptInput = new ClientOptInput();
        String outputFolder = getTmpFolder().getAbsolutePath() + File.separator + destPath;
        String outputFilename = outputFolder + "-bundle.zip";

        clientOptInput.openAPI(openapi);

        CodegenConfig codegenConfig;
        try {
            codegenConfig = CodegenConfigLoader.forName(language);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported target " + language + " supplied");
        }

        if (opts.getOptions() != null) {
            codegenConfig.additionalProperties().putAll(opts.getOptions());
            codegenConfig.additionalProperties().put("openAPI", openapi);
        }

        codegenConfig.setOutputDir(outputFolder);

        clientOptInput.config(codegenConfig);

        try {
            List<File> files = new DefaultGenerator().opts(clientOptInput).generate();
            if (files.size() > 0) {
                List<File> filesToAdd = new ArrayList<>();
                LOGGER.debug("adding to {}", outputFolder);
                filesToAdd.add(new File(outputFolder));
                ZipUtil zip = new ZipUtil();
                zip.compressFiles(filesToAdd, outputFilename);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A target generation was attempted, but no files were created!");
            }
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                    LOGGER.error("unable to delete file " + file.getAbsolutePath(), e);
                }
            }
            try {
                new File(outputFolder).delete();
            } catch (Exception e) {
                LOGGER.error("unable to delete output folder " + outputFolder, e);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to build target: " + e.getMessage(), e);
        }
        return outputFilename;
    }