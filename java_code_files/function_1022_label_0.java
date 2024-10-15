    public void test_copy_from_local_file_is_only_allowed_for_superusers() {
        execute("CREATE TABLE quotes (id INT PRIMARY KEY, " +
            "quote STRING INDEX USING FULLTEXT) WITH (number_of_replicas = 0)");
        execute("CREATE USER test_user");
        execute("GRANT ALL TO test_user");

        var roles = cluster().getInstance(Roles.class);
        Role user = roles.findUser("test_user");
        Sessions sqlOperations = cluster().getInstance(Sessions.class);
        try (var session = sqlOperations.newSession(null, user)) {
            assertThatThrownBy(() -> execute("COPY quotes FROM ?", new Object[]{copyFilePath + "test_copy_from.json"}, session))
                .isExactlyInstanceOf(UnauthorizedException.class)
                .hasMessage("Only a superuser can read from the local file system");
        }
    }