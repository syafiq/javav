    ResponseEntity<?> doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final String filename = getFileName(request);
        if (filename == null || filename.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Just to make sure we don't allow access to arbitrary resources
        if (UNSAFE_RESOURCE.matcher(filename).find()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        URL resource = getResource(filename);
        if (resource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String[] filenameParts = filename.split("\\.");
        String extension = filenameParts[filenameParts.length - 1];

        MimeType mime = null;
        try {
            mime = MimeType.createFromExtension(extension);
        } catch (MimeException e) {
            return new ResponseEntity<Object>(
                    "Unable to create MimeType for " + extension, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // TODO write ByteArrayOutputStream ResponseEntity

        response.setContentType(mime.getFormat());
        try (InputStream inputStream = resource.openStream();
                ServletOutputStream outputStream = response.getOutputStream(); ) {
            StreamUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            return new ResponseEntity<Object>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }