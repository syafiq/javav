    public String getJdbc() {
        if (StringUtils.isEmpty(extraParams.trim())) {
            return "jdbc:mysql://HOSTNAME:PORT/DATABASE"
                    .replace("HOSTNAME", getHost().trim())
                    .replace("PORT", getPort().toString().trim())
                    .replace("DATABASE", getDataBase().trim());
        } else {
            for (String illegalParameter : illegalParameters) {
                if (getExtraParams().toLowerCase().contains(illegalParameter.toLowerCase()) || URLDecoder.decode(getExtraParams()).contains(illegalParameter.toLowerCase())) {
                    DEException.throwException("Illegal parameter: " + illegalParameter);
                }
            }

            return "jdbc:mysql://HOSTNAME:PORT/DATABASE?EXTRA_PARAMS"
                    .replace("HOSTNAME", getHost().trim())
                    .replace("PORT", getPort().toString().trim())
                    .replace("DATABASE", getDataBase().trim())
                    .replace("EXTRA_PARAMS", getExtraParams().trim());
        }
    }