    String getFileName(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (path.indexOf("/rest/web") != 0) {
            path = path.substring(path.indexOf("/rest/web"));
        }
        return path.substring("/rest/web/".length());
    }