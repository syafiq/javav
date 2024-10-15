	public Element createXmlElementCopy(InternalXmlStorageElement el) throws CoreException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();
			Element newXmlEl = null;
			synchronized (doc) {
				synchronized (el.fLock) {
					if (el.fElement.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
						Document baseDoc = el.fElement.getOwnerDocument();
						NodeList list = baseDoc.getChildNodes();
						for (int i = 0; i < list.getLength(); i++) {
							Node node = list.item(i);
							node = importAddNode(doc, node);
							if (node.getNodeType() == Node.ELEMENT_NODE && newXmlEl == null) {
								newXmlEl = (Element) node;
							}
						}

					} else {
						newXmlEl = (Element) importAddNode(doc, el.fElement);
					}
					return newXmlEl;
				}
			}
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (FactoryConfigurationError e) {
			throw ExceptionFactory.createCoreException(e);
		}

	}