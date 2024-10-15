    String getFileName(HttpServletRequest request) throws IOException {
        String path =
                URLDecoder.decode(request.getRequestURI(), "UTF-8")
                        .substring(request.getContextPath().length())
                        .replace(File.separatorChar, '/');
        int index = path.indexOf("/rest/web/");
        return index < 0 ? null : path.substring(index + "/rest/web/".length());
    }