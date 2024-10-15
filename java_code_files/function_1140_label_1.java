  static void main(String[] args) throws Exception {
    // Send two sample queries using a standard DNSJAVA resolver
    SimpleResolver sr = new SimpleResolver("8.8.8.8");
    System.out.println("Standard resolver:");
    sendAndPrint(sr, "www.dnssec-failed.org.");
    sendAndPrint(sr, "www.isc.org.");

    // Send the same queries using the validating resolver with the
    // trust anchor of the root zone
    // http://data.iana.org/root-anchors/root-anchors.xml
    ValidatingResolver vr = new ValidatingResolver(sr);
    vr.loadTrustAnchors(new ByteArrayInputStream(ROOT.getBytes("ASCII")));
    vr.loadTrustAnchors(new ByteArrayInputStream(ROOT.getBytes(StandardCharsets.US_ASCII)));
    System.out.println("\n\nValidating resolver:");
    sendAndPrint(vr, "www.dnssec-failed.org.");
    sendAndPrint(vr, "www.isc.org.");
  }