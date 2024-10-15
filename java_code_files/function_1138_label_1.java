  private int verify(
      Message m, byte[] messageBytes, TSIGRecord requestTSIG, boolean fullSignature, Mac hmac) {
    m.tsigState = Message.TSIG_FAILED;
    TSIGRecord tsig = m.getTSIG();
    if (tsig == null) {
      return Rcode.FORMERR;
    }

    if (!tsig.getName().equals(name) || !tsig.getAlgorithm().equals(alg)) {
      log.debug(
          "BADKEY failure on message id {}, expected: {}/{}, actual: {}/{}",
          m.getHeader().getID(),
          name,
          alg,
          tsig.getName(),
          tsig.getAlgorithm());
      return Rcode.BADKEY;
    }

    if (hmac == null) {
      hmac = initHmac();
    }

    if (requestTSIG != null && tsig.getError() != Rcode.BADKEY && tsig.getError() != Rcode.BADSIG) {
      hmacAddSignature(hmac, requestTSIG);
    }

    m.getHeader().decCount(Section.ADDITIONAL);
    byte[] header = m.getHeader().toWire();
    m.getHeader().incCount(Section.ADDITIONAL);
    if (log.isTraceEnabled()) {
      log.trace(hexdump.dump("TSIG-HMAC header", header));
    }
    hmac.update(header);

    int len = m.tsigstart - header.length;
    if (log.isTraceEnabled()) {
      log.trace(hexdump.dump("TSIG-HMAC message after header", messageBytes, header.length, len));
    }
    hmac.update(messageBytes, header.length, len);

    byte[] tsigVariables = getTsigVariables(fullSignature, tsig);
    hmac.update(tsigVariables);

    byte[] signature = tsig.getSignature();
    int badsig = verifySignature(hmac, signature);
    if (badsig != Rcode.NOERROR) {
      return badsig;
    }

    // validate time after the signature, as per
    // https://www.rfc-editor.org/rfc/rfc8945.html#section-5.4
    int badtime = verifyTime(tsig);
    if (badtime != Rcode.NOERROR) {
      return badtime;
    }

    m.tsigState = Message.TSIG_VERIFIED;
    return Rcode.NOERROR;
  }