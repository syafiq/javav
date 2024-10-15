	public TemplateDescriptor(URL descriptorURL, String pluginId) throws TemplateInitializationException {
		String msg = NLS.bind(Messages.TemplateCore_init_failed, descriptorURL.toString());
		try {
			this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(descriptorURL.openStream());
		} catch (ParserConfigurationException pce) {
			throw new TemplateInitializationException(msg, pce);
		} catch (IOException ioe) {
			throw new TemplateInitializationException(msg, ioe);
		} catch (SAXException se) {
			throw new TemplateInitializationException(msg, se);
		}
		this.rootElement = document.getDocumentElement();
		this.persistVector = new ArrayList<>();
		this.pluginId = pluginId;
	}