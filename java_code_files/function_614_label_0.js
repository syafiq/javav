    void setUp() throws Exception
    {
        // Load the macros page so it can be included.
        loadPage(CONFIGURABLE_CLASS_MACROS);

        // Mock the query.
        this.oldcore.getMocker().registerComponent(ScriptService.class, "query", this.queryService);
        when(this.queryService.hql(anyString())).thenReturn(this.query);
        when(this.query.addFilter(anyString())).thenReturn(this.query);
        when(this.query.setLimit(anyInt())).thenReturn(this.query);
        when(this.query.setOffset(anyInt())).thenReturn(this.query);
        when(this.query.bindValues(any(Map.class))).thenReturn(this.query);
        when(this.query.bindValues(any(List.class))).thenReturn(this.query);
    }