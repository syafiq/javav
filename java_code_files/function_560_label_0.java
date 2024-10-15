    private boolean matchesCookieDomain(String host, HttpServletRequest request)
    {
        String serverName = request.getServerName();
        // Add a leading dot to avoid matching domains that are longer versions of the cookie domain and to ensure
        // that the cookie domain itself is matched as the cookie domain also contains the leading dot. Always add
        // the dot as two dots will still match.
        String prefixedServerName = COOKIE_DOMAIN_PREFIX + serverName;

        Optional<String> cookieDomain =
            this.authenticationConfiguration.getCookieDomains().stream()
                .filter(prefixedServerName::endsWith)
                .findFirst();

        // If there is a cookie domain, check if the host also matches it.
        return cookieDomain.map((COOKIE_DOMAIN_PREFIX + host)::endsWith)
            // If no cookie domain is configured, check for an exact match with the server name as no domain is sent in
            // this case and thus the cookie isn't valid for subdomains.
            .orElseGet(() -> host.equals(serverName));
    }