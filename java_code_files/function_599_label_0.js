	static private List<String> getOutputDirectories(String installLocation) {
		List<String> ret = outputDirectories.get(installLocation);
		if (ret == null) {
			ret = new ArrayList<>();
			outputDirectories.put(installLocation, ret);
			try {
				File file = new File(installLocation + File.separator + ".classpath"); //$NON-NLS-1$
				final Document doc = XmlProcessorFactory.parseWithErrorOnDOCTYPE(file);
				final XPath xp = XPathFactory.newInstance().newXPath();
				final NodeList list = (NodeList) xp.evaluate(
						"//classpathentry[@kind='output']/@path", doc, XPathConstants.NODESET); //$NON-NLS-1$
				for (int i = 0; i < list.getLength(); i++) {
					final String value = list.item(i).getNodeValue();
					ret.add(value);
				}
			} catch (final Exception e) {
			}
		}
		return ret;
	}