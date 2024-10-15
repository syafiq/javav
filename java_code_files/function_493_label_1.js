		public void store(ContentAssistHistory history, StreamResult result) throws CoreException {
			try {
				DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
				DocumentBuilder builder= factory.newDocumentBuilder();
				Document document= builder.newDocument();

				Element rootElement = document.createElement(NODE_ROOT);
				rootElement.setAttribute(ATTRIBUTE_MAX_LHS, Integer.toString(history.fMaxLHS));
				rootElement.setAttribute(ATTRIBUTE_MAX_RHS, Integer.toString(history.fMaxRHS));
				document.appendChild(rootElement);

				for (Entry<String, MRUSet<String>> entry : history.fLHSCache.entrySet()) {
					String lhs = entry.getKey();
					Element lhsElement= document.createElement(NODE_LHS);
					lhsElement.setAttribute(ATTRIBUTE_NAME, lhs);
					rootElement.appendChild(lhsElement);
					for (String rhs : entry.getValue()) {
						Element rhsElement= document.createElement(NODE_RHS);
						rhsElement.setAttribute(ATTRIBUTE_NAME, rhs);
						lhsElement.appendChild(rhsElement);
					}
				}

				Transformer transformer=TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.INDENT, "no"); //$NON-NLS-1$
				DOMSource source = new DOMSource(document);

				transformer.transform(source, result);
			} catch (TransformerException | ParserConfigurationException e) {
				throw createException(e, JavaTextMessages.ContentAssistHistory_serialize_error);
			}
		}