    public void initialize(ServiceExtensionContext context) {
        var requestFactory = new Oauth2CredentialsRequestFactory(privateKeyResolver, clock, vault, context.getMonitor());
        var oauth2ParamsDecorator = new Oauth2HttpRequestParamsDecorator(requestFactory, oauth2Client);

        paramsProvider.registerSinkDecorator(oauth2ParamsDecorator);
        paramsProvider.registerSourceDecorator(oauth2ParamsDecorator);
    }