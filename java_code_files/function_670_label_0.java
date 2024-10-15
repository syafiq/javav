	public static void main(String[] args) {
		Test test = new Test();
		try {
			XMLDumper dumper = new XMLDumper(test);
			Document document = dumper.getDocument();
			StringWriter writer = new StringWriter();

			Transformer transformer = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE()
					.newTransformer();
			transformer.transform(new DOMSource(document), new StreamResult(writer));

			System.out.println("STRXML = " + writer.toString()); //Spit out DOM as a String //$NON-NLS-1$
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}