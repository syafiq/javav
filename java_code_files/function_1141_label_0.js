  NioUdpClient() {
    // https://datatracker.ietf.org/doc/html/rfc6335#section-6
    int ephemeralStartDefault = 49152;
    int ephemeralEndDefault = 65535;

    // Linux usually uses 32768-60999
    if (System.getProperty("os.name").toLowerCase().contains("linux")) {
      ephemeralStartDefault = 32768;
      ephemeralEndDefault = 60999;
    }

    ephemeralStart = Integer.getInteger("dnsjava.udp.ephemeral.start", ephemeralStartDefault);
    int ephemeralEnd = Integer.getInteger("dnsjava.udp.ephemeral.end", ephemeralEndDefault);
    ephemeralRange = ephemeralEnd - ephemeralStart;

    if (Boolean.getBoolean("dnsjava.udp.ephemeral.use_ephemeral_port")) {
      prng = null;
    } else {
      prng = new SecureRandom();
    }
    setRegistrationsTask(this::processPendingRegistrations, false);
    setTimeoutTask(this::checkTransactionTimeouts, false);
    setCloseTask(this::closeUdp, false);
  }