	public static TestRunSession importTestRunSession(String url, IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask(ModelMessages.JUnitModel_importing_from_url, IProgressMonitor.UNKNOWN);
		final String trimmedUrl= url.trim().replaceAll("\r\n?|\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
		final TestRunHandler handler= new TestRunHandler(monitor);

		final CoreException[] exception= { null };
		final TestRunSession[] session= { null };

		Thread importThread= new Thread("JUnit URL importer") { //$NON-NLS-1$
			@Override
			public void run() {
				try {
					SAXParserFactory parserFactory= XmlProcessorFactoryJdtJunit.createSAXFactoryWithErrorOnDOCTYPE();
//					parserFactory.setValidating(true); // TODO: add DTD and debug flag
					SAXParser parser= parserFactory.newSAXParser();
					parser.parse(trimmedUrl, handler);
					session[0]= handler.getTestRunSession();
				} catch (OperationCanceledException e) {
					// canceled
				} catch (ParserConfigurationException | SAXException e) {
					storeImportError(e);
				} catch (IOException e) {
					storeImportError(e);
				} catch (IllegalArgumentException e) {
					// Bug in parser: can throw IAE even if URL is not null
					storeImportError(e);
				}
			}
			private void storeImportError(Exception e) {
				exception[0]= new CoreException(new org.eclipse.core.runtime.Status(IStatus.ERROR,
						JUnitCorePlugin.getPluginId(), ModelMessages.JUnitModel_could_not_import, e));
			}
		};
		importThread.start();

		while (session[0] == null && exception[0] == null && !monitor.isCanceled()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// that's OK
			}
		}
		if (session[0] == null) {
			if (exception[0] != null) {
				throw new InvocationTargetException(exception[0]);
			} else {
				importThread.interrupt(); // have to kill the thread since we don't control URLConnection and XML parsing
				throw new InterruptedException();
			}
		}

		JUnitCorePlugin.getModel().addTestRunSession(session[0]);
		monitor.done();
		return session[0];
	}