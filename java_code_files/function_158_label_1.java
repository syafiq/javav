    private Set<File> handleArtifacts(Document xhtmlDoc, OfficeConverterResult officeConverterResult)
        throws OfficeImporterException
    {
        Set<File> artifacts = new HashSet<>(officeConverterResult.getAllFiles());
        artifacts.remove(officeConverterResult.getOutputFile());

        @SuppressWarnings("unchecked")
        Map<String, byte[]> embeddedImages = (Map<String, byte[]>) xhtmlDoc.getUserData("embeddedImages");
        if (embeddedImages != null) {
            File outputDirectory = officeConverterResult.getOutputDirectory();
            for (Map.Entry<String, byte[]> embeddedImage : embeddedImages.entrySet()) {
                File outputFile = new File(outputDirectory, embeddedImage.getKey());
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    IOUtils.write(embeddedImage.getValue(), fos);
                } catch (IOException e) {
                    throw new OfficeImporterException(
                        String.format("Error when writing embedded image file [%s]", outputFile.getAbsolutePath()), e);
                }
                artifacts.add(outputFile);
            }
        }
        return artifacts;
    }