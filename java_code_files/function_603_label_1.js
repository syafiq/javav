	public static XMLMemento createReadRoot(Reader reader, String baseDir) throws WorkbenchException {
		String errorMessage = null;
		Exception exception = null;
		DocumentBuilderFactory factory = null;
		Object attributeDTDOldValue = null;
		Object attributeSchemaOldValue = null;
		try {
			factory = DocumentBuilderFactory.newInstance();
			try {
				attributeDTDOldValue = factory.getAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD);
				attributeSchemaOldValue = factory.getAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_SCHEMA);
			} catch (NullPointerException | IllegalArgumentException e) {
				// Attributes not defined
			}
			factory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD,
					getAttributeNewValue(attributeDTDOldValue));
			factory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_SCHEMA,
					getAttributeNewValue(attributeSchemaOldValue));
			DocumentBuilder parser = factory.newDocumentBuilder();
			InputSource source = new InputSource(reader);
			if (baseDir != null) {
				source.setSystemId(baseDir);
			}

			parser.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException exception) {
					// ignore
				}

				@Override
				public void error(SAXParseException exception) {
					// ignore
				}

				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}
			});

			Document document = parser.parse(source);
			NodeList list = document.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node instanceof Element) {
					return new XMLMemento(document, (Element) node);
				}
			}
		} catch (ParserConfigurationException e) {
			exception = e;
			errorMessage = WorkbenchMessages.XMLMemento_parserConfigError;
		} catch (IOException e) {
			exception = e;
			errorMessage = WorkbenchMessages.XMLMemento_ioError;
		} catch (SAXException e) {
			exception = e;
			errorMessage = WorkbenchMessages.XMLMemento_formatError;
		} finally {
			if (factory != null) {
				factory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, attributeDTDOldValue);
				factory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_SCHEMA, attributeSchemaOldValue);
			}
		}

		String problemText = null;
		if (exception != null) {
			problemText = exception.getMessage();
		}
		if (problemText == null || problemText.isEmpty()) {
			problemText = errorMessage != null ? errorMessage : WorkbenchMessages.XMLMemento_noElement;
		}
		throw new WorkbenchException(problemText, exception);
	}