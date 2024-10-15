Document getDocument(String xmlPath) {
	try (InputStream is = createInputStream(xmlPath))  {
		if (is != null) {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
		}
	} catch (Exception e) {
		//ignored
	}
	return null;
}