    private String getPDFTemplateProperty(String propertyName, XWikiContext context)
    {
        String pdftemplate = context.getRequest().getParameter("pdftemplate");

        DocumentReference templateReference;
        DocumentReference classReference;
        if (StringUtils.isNotEmpty(pdftemplate)) {
            templateReference = referenceResolver.resolve(pdftemplate);
            classReference = new DocumentReference(templateReference.getWikiReference().getName(), "XWiki", "PDFClass");
        } else {
            templateReference = dab.getCurrentDocumentReference();
            String currentWiki = dab.getCurrentDocumentReference().getRoot().getName();
            classReference = new DocumentReference(currentWiki, "XWiki", "PDFClass");
        }

        String result = (String) dab.getProperty(templateReference, classReference, propertyName);
        if (StringUtils.isBlank(result)) {
            return "";
        }
        String templateName = referenceSerializer.serialize(templateReference);
        try {
            StringWriter writer = new StringWriter();
            VelocityContext vcontext = velocityManager.getVelocityContext();
            velocityManager.getVelocityEngine().evaluate(vcontext, writer, templateName, result);
            result = writer.toString();
        } catch (XWikiVelocityException e) {
            LOGGER.warn("Error applying Velocity to the [{}] property of the [{}] document. Using the property's value "
                + "without applying Velocity.", propertyName, templateName, ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }