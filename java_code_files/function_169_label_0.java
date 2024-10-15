    protected Pair<String, Map<String, OfficeDocumentArtifact>> buildPresentationHTML(
        OfficeConverterResult officeConverterResult, String nameSpace) throws IOException
    {
        Map<String, OfficeDocumentArtifact> artifactFiles = new HashMap<>();
        // Iterate all the slides.
        Set<File> conversionOutputFiles = officeConverterResult.getAllFiles();
        Map<Integer, String> filenames = new HashMap<>();
        for (File conversionOutputFile : conversionOutputFiles) {
            Matcher matcher = SLIDE_FORMAT.matcher(conversionOutputFile.getName());
            if (matcher.matches()) {
                String number = matcher.group("number");
                String slideImageName = String.format("%s-slide%s.jpg", nameSpace, number);
                artifactFiles.put(slideImageName, new FileOfficeDocumentArtifact(slideImageName, conversionOutputFile));
                // Append slide image to the presentation HTML.
                String slideImageURL = null;
                try {
                    // We need to encode the slide image name in case it contains special URL characters.
                    slideImageURL = URLEncoder.encode(slideImageName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // This should never happen.
                }
                // We do not want to encode the spaces in '+' since '+' will be then reencoded in
                // ImageFilter to keep it and not consider it as a space when decoding it.
                // This is link to a bug in libreoffice that does not convert properly the '+', so we cannot distinguish
                // them from spaces in filenames. This should be removed once
                // https://github.com/sbraconnier/jodconverter/issues/125 is fixed.
                slideImageURL = slideImageURL.replace('+', ' ');

                filenames.put(Integer.parseInt(number), slideImageURL);
            }
        }
        // We sort by number so that the filenames are ordered by slide number.
        String presentationHTML = filenames.entrySet().stream().sorted(Map.Entry.comparingByKey())
            .map(entry -> String.format("<p><img src=\"%s\"/></p>", XMLUtils.escapeAttributeValue(entry.getValue())))
            .collect(Collectors.joining());
        return Pair.of(presentationHTML, artifactFiles);
    }