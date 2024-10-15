    private String getFileName(Attr source) throws MimeTypeException
    {
        String value = source.getValue();
        String fileName = null;
        @SuppressWarnings("unchecked")
        Map<String, byte[]> embeddedImages =
            (Map<String, byte[]>) source.getOwnerDocument().getUserData(EMBEDDED_IMAGES);
        if (embeddedImages != null && value.startsWith("data:")) {
            // An image embedded using the Data URI scheme.
            DataUri dataURI = DataUri.parse(value, Charset.forName(UTF_8));
            fileName = dataURI.getFilename();
            if (StringUtils.isEmpty(fileName)) {
                fileName = String.valueOf(Math.abs(dataURI.hashCode()));
                if (!StringUtils.isEmpty(dataURI.getMime())) {
                    String extension = MimeTypes.getDefaultMimeTypes().forName(dataURI.getMime()).getExtension();
                    fileName += extension;
                }
            }
            embeddedImages.put(fileName, dataURI.getData());
        } else if (!value.contains("://")) {
            // A relative path.
            int separator = value.lastIndexOf('/');
            fileName = separator < 0 ? value : value.substring(separator + 1);
            try {

                /*
                We need to manage here several special cases because of both jodconverter/libreoffice behaviour
                when processing image URL and HtmlCleaner when processing XML attributes.
                First of all, jodconverter/libreoffice does not currently process well the "+" character: it does not
                encode it when creating the URL which might lead to an error when decoding the URL. An issue has been
                opened there: https://github.com/sbraconnier/jodconverter/issues/125

                Then HtmlCleaner escape all characters it finds in Html attribute which lead to an error when it
                concerns "&" character.
                Finally '@' is used in XWiki Syntax so it needs to be escaped to build the link properly.
                 */
                fileName = fileName.replaceAll("\\+", "%2B");
                // We have to decode the image file name in case it contains URL special characters.
                fileName = URLDecoder.decode(fileName, UTF_8);

                // '@' must also be escaped in order to resolve properly the path in XWiki
                fileName = fileName.replaceAll("@", "\\\\@");
            } catch (Exception e) {
                // This shouldn't happen. Use the encoded image file name.
            }
        }
        return fileName;
    }