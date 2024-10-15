    public void decodeResources(File outDir) throws AndrolibException {
        if (!mApkInfo.hasResources()) {
            return;
        }

        mResTable.loadMainPkg(mApkInfo.getApkFile());

        ResStreamDecoderContainer decoders = new ResStreamDecoderContainer();
        decoders.setDecoder("raw", new ResRawStreamDecoder());
        decoders.setDecoder("9patch", new Res9patchStreamDecoder());

        AXmlResourceParser axmlParser = new AXmlResourceParser(mResTable);
        decoders.setDecoder("xml", new XmlPullStreamDecoder(axmlParser, getResXmlSerializer()));

        ResFileDecoder fileDecoder = new ResFileDecoder(decoders);
        Directory in, out, outRes;

        try {
            out = new FileDirectory(outDir);
            in = mApkInfo.getApkFile().getDirectory();
            outRes = out.createDir("res");
        } catch (DirectoryException ex) {
            throw new AndrolibException(ex);
        }

        ExtMXSerializer xmlSerializer = getResXmlSerializer();
        for (ResPackage pkg : mResTable.listMainPackages()) {

            LOGGER.info("Decoding file-resources...");
            for (ResResource res : pkg.listFiles()) {
                fileDecoder.decode(res, in, outRes, mResFileMapping);
            }

            LOGGER.info("Decoding values */* XMLs...");
            for (ResValuesFile valuesFile : pkg.listValuesFiles()) {
                generateValuesFile(valuesFile, outRes, xmlSerializer);
            }
            generatePublicXml(pkg, outRes, xmlSerializer);
        }

        AndrolibException decodeError = axmlParser.getFirstError();
        if (decodeError != null) {
            throw decodeError;
        }
    }