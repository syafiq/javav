    public FileCollectSource(NodeContext nodeCtx,
                             ClusterService clusterService,
                             Map<String, FileInputFactory> fileInputFactoryMap,
                             ThreadPool threadPool,
                             Roles roles) {
        this.fileInputFactoryMap = fileInputFactoryMap;
        this.nodeCtx = nodeCtx;
        this.inputFactory = new InputFactory(nodeCtx);
        this.clusterService = clusterService;
        this.threadPool = threadPool;
        this.roles = roles;
    }