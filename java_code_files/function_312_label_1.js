	private static LicenseInfo setIfValid(LicenseInfo value, LicenseInfo def) {
		if (value.isValid() || def.isNone()) {
			return value;
		}
		return def;
	}