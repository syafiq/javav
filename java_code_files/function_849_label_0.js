    public Http2FrameListener frameListener() {
        Http2FrameListener frameListener = frameListener0();
        // Unwrap the original Http2FrameListener as we add this decoder under the hood.
        if (frameListener instanceof Http2MaxRstFrameListener) {
            return ((Http2MaxRstFrameListener) frameListener).listener;
        }
        return frameListener;
    }