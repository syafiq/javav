    private String maybeEvaluateContent(String name, boolean isolated, String content, XWikiDocument sdoc)
    {
        if (sdoc != null) {
            // Start with a pass of Velocity
            // TODO: maybe make velocity+wiki a syntax so that getRenderedContent can directly take care
            // of that
            AuthorExecutor authorExecutor = Utils.getComponent(AuthorExecutor.class);
            VelocityEvaluator velocityEvaluator = Utils.getComponent(VelocityEvaluator.class);
            try {
                return authorExecutor.call(() -> {
                    String result;
                    // Check script right inside the author executor as otherwise the context author might not be
                    // correct.
                    if (isDocumentAuthorAllowedToEvaluateScript(sdoc)) {
                        result = velocityEvaluator.evaluateVelocityNoException(content,
                            isolated ? sdoc.getDocumentReference() : null);
                    } else {
                        result = content;
                    }
                    return result;
                }, sdoc.getAuthorReference(), sdoc.getDocumentReference());
            } catch (Exception e) {
                LOGGER.warn(FAILED_VELOCITY_EXECUTION_WARNING, name, ExceptionUtils.getRootCauseMessage(e));
            }
        }

        return content;
    }