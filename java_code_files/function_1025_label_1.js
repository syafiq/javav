    public FileCollectSource(NodeContext nodeCtx,
                             ClusterService clusterService,
                             Map<String, FileInputFactory> fileInputFactoryMap,
                             ThreadPool threadPool) {
        this.fileInputFactoryMap = fileInputFactoryMap;
        this.nodeCtx = nodeCtx;
        this.inputFactory = new InputFactory(nodeCtx);
        this.clusterService = clusterService;
        this.threadPool = threadPool;
    }