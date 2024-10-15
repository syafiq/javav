	private static String getAttributeNewValue(Object attributeOldValue) {
		StringBuilder strNewValue = new StringBuilder(FILE_STRING);
		if (attributeOldValue instanceof String && ((String) attributeOldValue).length() != 0) {
			String strOldValue = (String) attributeOldValue;
			boolean exists = Arrays.asList(strOldValue.split(",")).stream().anyMatch(x -> x.trim().equals(FILE_STRING)); //$NON-NLS-1$
			if (!exists) {
				strNewValue.append(", ").append(strOldValue); //$NON-NLS-1$
			} else {
				strNewValue = new StringBuilder(strOldValue);
			}
		}
		return strNewValue.toString();
	}