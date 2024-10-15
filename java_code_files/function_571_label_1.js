	public static boolean test() throws Exception {
		final String XML_FRAMEWORK_TEST_MODEL =
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
			"<model>\n" +
			" <type-element kind=\"CLASS\" qname=\"pa.A\" sname=\"A\">\n" +
			"  <superclass>\n" +
			"   <type-mirror kind=\"DECLARED\" to-string=\"java.lang.Object\"/>\n" +
			"  </superclass>\n" +
			"  <variable-element kind=\"FIELD\" sname=\"f\" type=\"java.lang.String\">\n" +
			"   <annotations>\n" +
			"    <annotation sname=\"Anno1\">\n" +
			"     <annotation-values>\n" +
			"      <annotation-value member=\"value\" type=\"java.lang.String\" value=\"spud\"/>\n" +
			"     </annotation-values>\n" +
			"    </annotation>\n" +
			"   </annotations>\n" +
			"  </variable-element>\n" +
			" </type-element>\n" +
			"</model>\n";

		// create "actual" model
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document actualModel = factory.newDocumentBuilder().newDocument();
		Element modelNode = actualModel.createElement(MODEL_TAG);
		// primary type
		Element typeNode = actualModel.createElement(TYPE_ELEMENT_TAG);
		typeNode.setAttribute(KIND_TAG, "CLASS");
		typeNode.setAttribute(SNAME_TAG, "A");
		typeNode.setAttribute(QNAME_TAG, "pa.A");
		// superclass
		Element scNode = actualModel.createElement(SUPERCLASS_TAG);
		Element tmNode = actualModel.createElement(TYPE_MIRROR_TAG);
		tmNode.setAttribute(KIND_TAG, "DECLARED");
		tmNode.setAttribute(TO_STRING_TAG, "java.lang.Object");
		scNode.appendChild(tmNode);
		typeNode.appendChild(scNode);
		// field
		Element variableNode = actualModel.createElement(VARIABLE_ELEMENT_TAG);
		variableNode.setAttribute(KIND_TAG, "FIELD");
		variableNode.setAttribute(SNAME_TAG, "f");
		variableNode.setAttribute(TYPE_TAG, "java.lang.String");
		// annotation on field
		Element annotationsNode = actualModel.createElement(ANNOTATIONS_TAG);
		Element annoNode = actualModel.createElement(ANNOTATION_TAG);
		annoNode.setAttribute(SNAME_TAG, "Anno1");
		Element valuesNode = actualModel.createElement(ANNOTATION_VALUES_TAG);
		Element valueNode = actualModel.createElement(ANNOTATION_VALUE_TAG);
		valueNode.setAttribute(MEMBER_TAG, "value");
		valueNode.setAttribute(TYPE_TAG, "java.lang.String");
		valueNode.setAttribute(VALUE_TAG, "spud");
		valuesNode.appendChild(valueNode);
		annoNode.appendChild(valuesNode);
		annotationsNode.appendChild(annoNode);
		variableNode.appendChild(annotationsNode);
		typeNode.appendChild(variableNode);
		modelNode.appendChild(typeNode);
		actualModel.appendChild(modelNode);

		// load reference model
    	InputSource source = new InputSource(new StringReader(XML_FRAMEWORK_TEST_MODEL));
        Document expectedModel = factory.newDocumentBuilder().parse(source);

        // compare actual and reference
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuilder summary = new StringBuilder();
        summary.append("testXMLFramework failed; see console for details.  ");
        boolean success = compare(actualModel, expectedModel, out, summary, false /* ignoreJavacBugs */);
        if (!success) {
        	System.out.println("testXMLFramework failed.  Detailed output follows:");
        	System.out.print(out.toString());
        	System.out.println("=============== end output ===============");
        }
        return success;
	}