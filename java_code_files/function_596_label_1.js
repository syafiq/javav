	private void loadSettings() {
		final IEclipsePreferences pref = InstanceScope.INSTANCE.getNode(Plugin.ID);

		final boolean restoreColumns = pref.getBoolean(ModelEditorPreferences.LIST_TAB_REMEMBER_COLUMNS, false);
		final boolean restoreFilters = pref.getBoolean(ModelEditorPreferences.LIST_TAB_REMEMBER_FILTERS, false);
		if (!restoreColumns && !restoreFilters) {
			return;
		}

		final String xml = pref.get("list-tab-xml", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (E.notEmpty(xml)) {
			try {
				final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.parse(new InputSource(new StringReader(xml)));
				final XPath xpath = XPathFactory.newInstance().newXPath();
				NodeList list;
				if (restoreColumns) {
					// restore columns and column widths
					list = (NodeList) xpath.evaluate("//columns/column", doc, XPathConstants.NODESET); //$NON-NLS-1$
					for (int i = 0; i < list.getLength(); i++) {
						final Element ele = (Element) list.item(i);
						TableColumn col;
						final String colName = xpath.evaluate("attribute/text()", ele); //$NON-NLS-1$
						if (colName.isEmpty()) {
							continue;
						}
						col = requiredColumns.get(colName);
						if (col == null) {
							col = addColumn(colName).getTableViewerColumn().getColumn();
						}

						// move it to the end of the list.
						final int currentIndex = TableViewerUtil.getVisibleColumnIndex(tvResults, col);
						final int[] order = tvResults.getTable().getColumnOrder();
						for (int idx = 0; idx < order.length; idx++) {
							if (order[idx] > currentIndex) {
								order[idx]--;
							} else if (order[idx] == currentIndex) {
								order[idx] = order.length - 1;
							}
						}
						tvResults.getTable().setColumnOrder(order);

						//					if ("Item".equals(colName)) { //$NON-NLS-1$
						// col = colItem;
						//					} else if ("Item".equals(colName)) { //$NON-NLS-1$
						// col = colItem;
						// }

						final String sWidth = xpath.evaluate("width/text()", ele); //$NON-NLS-1$
						try {
							col.setWidth(Integer.parseInt(sWidth));
						} catch (final Exception e) {
						}
					}
				}

				if (restoreFilters) {
					// restore filters
					list = (NodeList) xpath.evaluate("//filters/filter", doc, XPathConstants.NODESET); //$NON-NLS-1$
					for (int i = 0; i < list.getLength(); i++) {
						final Element ele = (Element) list.item(i);
						final String type = xpath.evaluate("type/text()", ele); //$NON-NLS-1$
						final String condition = xpath.evaluate("condition/text()", ele); //$NON-NLS-1$
						final String emptyOption = xpath.evaluate("emptyOption/text()", ele); //$NON-NLS-1$
						if ("item".equals(type)) { //$NON-NLS-1$
							filterByItem(condition);
						} else if ("attribute".equals(type)) { //$NON-NLS-1$
							EmptyFilterOption emptyFilterOption;
							try {
								emptyFilterOption = EmptyFilterOption.valueOf(emptyOption);
							} catch (final Exception e) {
								emptyFilterOption = EmptyFilterOption.INCLUDE;
							}
							filterByAttribute(condition, emptyFilterOption);
						}
					}
				}
			} catch (final Exception e) {
			}
		}
	}