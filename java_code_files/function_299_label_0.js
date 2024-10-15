    static Stream<List<String>> allowedSpacesProvider()
    {
        return Stream.of(
            List.of("allowedSpace</div>"),
            List.of("allowedSpace1</div>", "allowedSpace2</div>")
        );
    }