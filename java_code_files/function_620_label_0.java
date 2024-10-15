Document getDocument(String xmlPath) {
	try (InputStream is = createInputStream(xmlPath))  {
		if (is != null) {
			@SuppressWarnings("restriction")
			Document d = org.eclipse.core.internal.runtime.XmlProcessorFactory.parseWithErrorOnDOCTYPE(new InputSource(is));
			return d;
		}
	} catch (Exception e) {
		//ignored
	}
	return null;
}