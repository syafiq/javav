	private boolean checkModel(List<TypeElement> rootElements, String expected, String name) throws Exception {
		Document actualModel = XMLConverter.convertModel(rootElements);

    	InputSource source = new InputSource(new StringReader(expected));
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document expectedModel = factory.newDocumentBuilder().parse(source);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuilder summary = new StringBuilder();
        summary.append("Test ").append(name).append(" failed; see console for details.  ");
        boolean success = XMLComparer.compare(actualModel, expectedModel, out, summary, _ignoreJavacBugs);
        if (!success) {
        	System.out.println("Test " + name + " failed.  Detailed output follows:");
        	System.out.print(out.toString());
        	System.out.println("Cut and paste:");
        	System.out.println(XMLConverter.xmlToCutAndPasteString(actualModel, 0, false));
        	System.out.println("=============== end output ===============");
        	reportError(summary.toString());
        }
        return success;
	}