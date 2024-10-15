		public ContentAssistHistory load(InputSource source) throws CoreException {
			Element root;
			try {
				DocumentBuilder parser = XmlProcessorFactoryJdtUi.createDocumentBuilderFactoryWithErrorOnDOCTYPE().newDocumentBuilder();
				parser.setErrorHandler(new DefaultHandler());
				root = parser.parse(source).getDocumentElement();
			} catch (SAXException | ParserConfigurationException | IOException e) {
				throw createException(e, JavaTextMessages.ContentAssistHistory_deserialize_error);
			}

			if (root == null || !NODE_ROOT.equalsIgnoreCase(root.getNodeName()))
				return null;

			int maxLHS= parseNaturalInt(root.getAttribute(ATTRIBUTE_MAX_LHS), DEFAULT_TRACKED_LHS);
			int maxRHS= parseNaturalInt(root.getAttribute(ATTRIBUTE_MAX_RHS), DEFAULT_TRACKED_RHS);

			ContentAssistHistory history= new ContentAssistHistory(maxLHS, maxRHS);

			NodeList list= root.getChildNodes();
			int length= list.getLength();
			for (int i= 0; i < length; ++i) {
				Node lhsNode= list.item(i);
				if (lhsNode.getNodeType() == Node.ELEMENT_NODE) {
					Element lhsElement= (Element) lhsNode;
					if (NODE_LHS.equalsIgnoreCase(lhsElement.getNodeName())) {
						String lhs= lhsElement.getAttribute(ATTRIBUTE_NAME);
						if (lhs != null) {
							Set<String> cache= history.getCache(lhs);
							NodeList children= lhsElement.getChildNodes();
							int nRHS= children.getLength();
							for (int j= 0; j < nRHS; j++) {
								Node rhsNode= children.item(j);
								if (rhsNode.getNodeType() == Node.ELEMENT_NODE) {
									Element rhsElement= (Element) rhsNode;
									if (NODE_RHS.equalsIgnoreCase(rhsElement.getNodeName())) {
										String rhs= rhsElement.getAttribute(ATTRIBUTE_NAME);
										if (rhs != null) {
											cache.add(rhs);
										}
									}
								}
							}
						}
					}
				}
			}

			return history;
		}