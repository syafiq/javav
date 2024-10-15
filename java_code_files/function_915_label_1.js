    String applyCSS(String html, String css, XWikiContext context)
    {
        LOGGER.debug("Applying the following CSS [{}] to HTML [{}]", css, html);
        try {
            // Prepare the input
            Reader re = new StringReader(html);
            InputSource source = new InputSource(re);
            XHTMLDocumentFactory docFactory = XHTMLDocumentFactory.getInstance();
            SAXReader reader = new SAXReader(docFactory);

            // Dom4J 2.1.1 disables external DTDs by default, so we set our own XMLReader.
            // See https://github.com/dom4j/dom4j/issues/51
            XMLReader xmlReader = xmlReaderFactory.createXMLReader();
            reader.setXMLReader(xmlReader);

            reader.setEntityResolver(new DefaultEntityResolver());
            XHTMLDocument document = (XHTMLDocument) reader.read(source);

            // Set the base URL so that CSS4J can resolve URLs in CSS. Use the current document in the XWiki Context
            document.setBaseURL(new URL(context.getDoc().getExternalURL("view", context)));

            // Apply the style sheet.
            document.addStyleSheet(new io.sf.carte.doc.style.css.nsac.InputSource(new StringReader(css)));
            applyInlineStyle(document.getRootElement());

            OutputFormat outputFormat = new OutputFormat("", false);
            if ((context == null) || (context.getWiki() == null)) {
                outputFormat.setEncoding("UTF-8");
            } else {
                outputFormat.setEncoding(context.getWiki().getEncoding());
            }
            StringWriter out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, outputFormat);
            writer.write(document);
            String result = out.toString();
            LOGGER.debug("HTML with CSS applied [{}]", result);
            return result;
        } catch (Exception e) {
            LOGGER.warn("Failed to apply CSS [{}] to HTML [{}]", css, html, e);
            return html;
        }
    }