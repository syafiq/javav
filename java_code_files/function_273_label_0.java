    private void displayVelocityCode(StringBuffer buffer, String name, String prefix, BaseCollection object,
        XWikiContext context)
    {
        StringBuffer result = new StringBuffer();
        super.displayView(result, name, prefix, object, context);
        XWikiDocument sdoc = getObjectDocument(object, context);
        if (getObjectDocumentSyntax(object, context).equals(Syntax.XWIKI_1_0) && sdoc != null) {
            try {
                Utils.getComponent(AuthorExecutor.class).call(() -> {
                    // Check script right inside the author executor as otherwise the context author might not be
                    // correct.
                    if (isDocumentAuthorAllowedToEvaluateScript(sdoc)) {
                        buffer.append(context.getWiki().parseContent(result.toString(), context));
                    } else {
                        buffer.append(result);
                    }
                    return null;
                }, sdoc.getAuthorReference(), sdoc.getDocumentReference());
            } catch (Exception e) {
                LOGGER.warn(FAILED_VELOCITY_EXECUTION_WARNING, name, ExceptionUtils.getRootCauseMessage(e));
                buffer.append(result);
            }
        } else {
            // Don't do anything since this mode is deprecated and not supported in the new rendering.
            buffer.append(result);
        }
    }