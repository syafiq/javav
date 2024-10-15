    void send() throws IOException {
      if (sendDone) {
        return;
      }

      verboseLog(
          "TCP write: transaction id=" + query.getHeader().getID(),
          channel.socket().getLocalSocketAddress(),
          channel.socket().getRemoteSocketAddress(),
          queryData);

      // combine length+message to avoid multiple TCP packets
      // https://tools.ietf.org/html/rfc7766#section-8
      ByteBuffer buffer = ByteBuffer.allocate(queryData.length + 2);
      buffer.put((byte) (queryData.length >>> 8));
      buffer.put((byte) (queryData.length & 0xFF));
      buffer.put(queryData);
      buffer.flip();
      while (buffer.hasRemaining()) {
        long n = channel.write(buffer);
        if (n == 0) {
          throw new EOFException(
              "Insufficient room for the data in the underlying output buffer for transaction "
                  + query.getHeader().getID());
        } else if (n < queryData.length) {
          throw new EOFException(
              "Could not write all data for transaction " + query.getHeader().getID());
        }
      }

      sendDone = true;
    }