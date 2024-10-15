	public static String xmlToString(Document model) {
		StringWriter s = new StringWriter();
		DOMSource domSource = new DOMSource(model);
		StreamResult streamResult = new StreamResult(s);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
			serializer.transform(domSource, streamResult);
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(s));
		}
		return s.toString();
	}