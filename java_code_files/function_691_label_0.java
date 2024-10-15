	protected Element createXmlElementCopy() throws CoreException {

		try {
			synchronized (fLock) {
				Element newXmlEl = null;
				DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
				Document doc = builder.newDocument();
				synchronized (doc) {
					if (fElement.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
						Document baseDoc = fElement.getOwnerDocument();
						NodeList list = baseDoc.getChildNodes();
						for (int i = 0; i < list.getLength(); i++) {
							Node node = list.item(i);
							node = importAddNode(doc, node);
							if (node.getNodeType() == Node.ELEMENT_NODE && newXmlEl == null) {
								newXmlEl = (Element) node;
							}
						}

					} else {
						newXmlEl = (Element) importAddNode(doc, fElement);
					}
				}
				return newXmlEl;
			}
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (FactoryConfigurationError e) {
			throw ExceptionFactory.createCoreException(e);
		}
	}