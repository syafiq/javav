    private String imageExtension(String type) {
        switch (type) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/webp":
                return "webp";
            case "image/svg+xml":
                return "svg";
            default:
                throw new IllegalArgumentException("Unsupported image type");
        }
    }