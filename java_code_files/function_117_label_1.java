    private Set<File> processImages(XDOM xdom, Set<File> artifactFiles, DocumentReference ownerDocumentReference,
        Map<String, ?> parameters)
    {
        // Process all image blocks.
        Set<File> temporaryFiles = new HashSet<>();
        List<ImageBlock> imgBlocks = xdom.getBlocks(new ClassBlockMatcher(ImageBlock.class), Block.Axes.DESCENDANT);
        if (!imgBlocks.isEmpty()) {
            Map<String, File> fileMap = new HashMap<>();
            for (File file : artifactFiles) {
                fileMap.put(file.getName(), file);
            }

            for (ImageBlock imgBlock : imgBlocks) {
                String imageReference = imgBlock.getReference().getReference();

                // Check whether there is a corresponding artifact.
                if (fileMap.containsKey(imageReference)) {
                    try {
                        List<String> resourcePath =
                            Arrays.asList(String.valueOf(parameters.hashCode()), imageReference);
                        TemporaryResourceReference temporaryResourceReference =
                            new TemporaryResourceReference(MODULE_NAME, resourcePath, ownerDocumentReference);

                        // Write the image into a temporary file.
                        File artifact = fileMap.get(imageReference);

                        File tempFile = this.temporaryResourceStore.createTemporaryFile(temporaryResourceReference,
                            new FileInputStream(artifact));

                        // Create a URL image reference which links to above temporary image file.
                        String temporaryResourceURL =
                            this.resourceReferenceSerializer.serialize(temporaryResourceReference).serialize();
                        ResourceReference urlImageReference =
                            new ResourceReference(temporaryResourceURL, ResourceType.PATH);
                        urlImageReference.setTyped(true);

                        // Replace the old image block with a new one that uses the above URL image reference.
                        Block newImgBlock = new ImageBlock(urlImageReference, false, imgBlock.getParameters());
                        imgBlock.getParent().replaceChild(Arrays.asList(newImgBlock), imgBlock);

                        // Make sure the new image block is not inside an ExpandedMacroBlock whose's content syntax
                        // doesn't support relative path resource references (we use relative paths to refer the
                        // temporary files).
                        maybeFixExpandedMacroAncestor(newImgBlock);

                        // Collect the temporary file so that it can be cleaned up when the view is disposed from cache.
                        temporaryFiles.add(tempFile);
                    } catch (Exception ex) {
                        String message = "Error while processing artifact image [%s].";
                        this.logger.error(String.format(message, imageReference), ex);
                    }
                }
            }
        }
        return temporaryFiles;
    }