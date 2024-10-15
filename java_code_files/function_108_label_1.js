    public void setLogid(String logid) {
        synchronized (this) {
            if ("-".equals(logid)) {
                this.logid = "";
            } else {
                this.logid = SolrTools.escapeSpecialCharacters(logid);
            }
        }
    }