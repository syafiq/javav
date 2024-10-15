    private Set<File> relocateArtifacts(WikiDocument sectionDoc, XDOMOfficeDocument officeDocument)
    {
        Set<File> artifacts = officeDocument.getArtifactsFiles();
        Set<File> result = new HashSet<>();
        List<ImageBlock> imageBlocks =
            sectionDoc.getXdom().getBlocks(new ClassBlockMatcher(ImageBlock.class), Axes.DESCENDANT);
        if (!imageBlocks.isEmpty()) {
            Map<String, File> fileMap = new HashMap<>();
            artifacts.forEach(item -> fileMap.put(item.getName(), item));
            for (ImageBlock imageBlock : imageBlocks) {
                String imageReference = imageBlock.getReference().getReference();
                File file = fileMap.get(imageReference);
                result.add(file);
                artifacts.remove(file);
            }
        }
        return result;
    }