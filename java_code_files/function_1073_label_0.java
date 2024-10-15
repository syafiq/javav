    void setUp(ServiceExtensionContext context) {
        context.registerService(HttpRequestParamsProvider.class, paramsProvider);
    }