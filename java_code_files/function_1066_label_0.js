    public HttpPostStandardRequestDecoder offer(HttpContent content) {
        checkDestroyed();

        if (content instanceof LastHttpContent) {
            isLastChunk = true;
        }

        ByteBuf buf = content.content();
        if (undecodedChunk == null) {
            undecodedChunk =
                    // Since the Handler will release the incoming later on, we need to copy it
                    //
                    // We are explicit allocate a buffer and NOT calling copy() as otherwise it may set a maxCapacity
                    // which is not really usable for us as we may exceed it once we add more bytes.
                    buf.alloc().buffer(buf.readableBytes()).writeBytes(buf);
        } else {
            undecodedChunk.writeBytes(buf);
        }
        parseBody();
        if (maxBufferedBytes > 0 && undecodedChunk != null && undecodedChunk.readableBytes() > maxBufferedBytes) {
            throw new TooLongFormFieldException();
        }
        if (undecodedChunk != null && undecodedChunk.writerIndex() > discardThreshold) {
            if (undecodedChunk.refCnt() == 1) {
                // It's safe to call discardBytes() as we are the only owner of the buffer.
                undecodedChunk.discardReadBytes();
            } else {
                // There seems to be multiple references of the buffer. Let's copy the data and release the buffer to
                // ensure we can give back memory to the system.
                ByteBuf buffer = undecodedChunk.alloc().buffer(undecodedChunk.readableBytes());
                buffer.writeBytes(undecodedChunk);
                undecodedChunk.release();
                undecodedChunk = buffer;
            }
        }
        return this;
    }