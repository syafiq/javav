	protected void buildANTScript(IPath antScriptLocation, String projectName, IPath absJarfile, String mainClass, SourceInfo[] sourceInfos) throws IOException {
		try (OutputStream outputStream = new FileOutputStream(antScriptLocation.toFile())) {
			String absJarname= absJarfile.toString();

			DocumentBuilder docBuilder= null;
			DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			try {
				docBuilder= factory.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				throw new IOException(FatJarPackagerMessages.FatJarPackageAntScript_error_couldNotGetXmlBuilder);
			}
			Document document= docBuilder.newDocument();

			Node comment;

			// Create the document
			Element project= document.createElement("project"); //$NON-NLS-1$
			project.setAttribute("name", "Create Runnable Jar for Project " + projectName); //$NON-NLS-1$ //$NON-NLS-2$
			project.setAttribute("default", "create_run_jar"); //$NON-NLS-1$ //$NON-NLS-2$
			document.appendChild(project);
			comment= document.createComment("this file was created by Eclipse Runnable JAR Export Wizard"); //$NON-NLS-1$
			project.appendChild(comment);
			comment= document.createComment("ANT 1.7 is required                                        "); //$NON-NLS-1$
			project.appendChild(comment);

			addBaseDirProperties(document, project);

			Element target= document.createElement("target"); //$NON-NLS-1$
			target.setAttribute("name", "create_run_jar"); //$NON-NLS-1$ //$NON-NLS-2$
			project.appendChild(target);

			Element jar= document.createElement("jar"); //$NON-NLS-1$
			jar.setAttribute("destfile", substituteBaseDirs(absJarname)); //$NON-NLS-1$s
			jar.setAttribute("filesetmanifest", "mergewithoutmain"); //$NON-NLS-1$ //$NON-NLS-2$
			target.appendChild(jar);

			Element manifest= document.createElement("manifest"); //$NON-NLS-1$
			jar.appendChild(manifest);

			Element attribute= document.createElement("attribute"); //$NON-NLS-1$
			attribute.setAttribute("name", "Main-Class"); //$NON-NLS-1$ //$NON-NLS-2$s
			attribute.setAttribute("value", mainClass); //$NON-NLS-1$
			manifest.appendChild(attribute);

			attribute= document.createElement("attribute"); //$NON-NLS-1$
			attribute.setAttribute("name", "Class-Path"); //$NON-NLS-1$ //$NON-NLS-2$s
			attribute.setAttribute("value", "."); //$NON-NLS-1$ //$NON-NLS-2$
			manifest.appendChild(attribute);

			for (SourceInfo sourceInfo : sourceInfos) {
				if (sourceInfo.isJar) {
					Element zipfileset= document.createElement("zipfileset"); //$NON-NLS-1$
					zipfileset.setAttribute("src", substituteBaseDirs(sourceInfo.absPath)); //$NON-NLS-1$
					zipfileset.setAttribute("excludes", "META-INF/*.SF"); //$NON-NLS-1$ //$NON-NLS-2$
					jar.appendChild(zipfileset);
				} else {
					Element fileset= document.createElement("fileset"); //$NON-NLS-1$
					fileset.setAttribute("dir", substituteBaseDirs(sourceInfo.absPath)); //$NON-NLS-1$
					jar.appendChild(fileset);
				}
			}

			try {
				// Write the document to the stream
				Transformer transformer= TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //$NON-NLS-1$ //$NON-NLS-2$
				DOMSource source= new DOMSource(document);
				StreamResult result= new StreamResult(outputStream);
				transformer.transform(source, result);
			} catch (TransformerException e) {
				throw new IOException(FatJarPackagerMessages.FatJarPackageAntScript_error_couldNotTransformToXML);
			}
		}
	}