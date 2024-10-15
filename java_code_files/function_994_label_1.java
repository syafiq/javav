	public String getXmlContent(VFSLeaf vfsLeaf) {
		String xml = null;

		String suffix = FileUtils.getFileSuffix(vfsLeaf.getName());
		if ("svg".equalsIgnoreCase(suffix)) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(vfsLeaf.getInputStream());
				String xpathExpression = "//svg/@content";
				XPathFactory xpf = XPathFactory.newInstance();
				XPath xpath = xpf.newXPath();
				XPathExpression expression = xpath.compile(xpathExpression);
				NodeList svgPaths = (NodeList)expression.evaluate(document, XPathConstants.NODESET);
				xml = svgPaths.item(0).getNodeValue();
			} catch (Exception e) {
				log.warn("Cannot extract xml from svg file {}", vfsLeaf.getRelPath());
				log.warn("", e);
			}
		} else {
			xml = FileUtils.load(vfsLeaf.getInputStream(), "utf-8");
		}
		
		return xml;
	}