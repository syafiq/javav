    public SnappyOutputStream(OutputStream out, int blockSize, BufferAllocatorFactory bufferAllocatorFactory)
    {
        this.out = out;
        this.blockSize = Math.max(MIN_BLOCK_SIZE, blockSize);
        if (this.blockSize > MAX_BLOCK_SIZE){
            throw new IllegalArgumentException(String.format("Provided chunk size %,d larger than max %,d", this.blockSize, MAX_BLOCK_SIZE));
        }
        int inputSize = blockSize;
        int outputSize = SnappyCodec.HEADER_SIZE + 4 + Snappy.maxCompressedLength(blockSize);

        this.inputBufferAllocator = bufferAllocatorFactory.getBufferAllocator(inputSize);
        this.outputBufferAllocator = bufferAllocatorFactory.getBufferAllocator(outputSize);

        inputBuffer = inputBufferAllocator.allocate(inputSize);
        outputBuffer = outputBufferAllocator.allocate(outputSize);
    }