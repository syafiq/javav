    public Connection getConnection(DatabaseConfiguration databaseConfiguration, boolean forceNewConnection)
            throws DatabaseServiceException {
        try {

            if (connection != null && !forceNewConnection) {
                // logger.info("connection closed::{}", connection.isClosed());
                if (!connection.isClosed()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Returning existing connection::{}", connection);
                    }

                    return connection;
                }
            }
            String dbURL = databaseConfiguration.toURI().toString();
            Class.forName(type.getClassPath());

            // logger.info("*** type.getClassPath() ::{}, {}**** ", type.getClassPath());

            DriverManager.setLoginTimeout(10);

            connection = DriverManager.getConnection(dbURL, databaseConfiguration.getDatabaseUser(),
                    databaseConfiguration.getDatabasePassword());

            if (logger.isDebugEnabled()) {
                logger.debug("*** Acquired New  connection for ::{} **** ", dbURL);
            }

            return connection;

        } catch (ClassNotFoundException e) {
            logger.error("Jdbc Driver not found", e);
            throw new DatabaseServiceException(e.getMessage());
        } catch (SQLException e) {
            logger.error("SQLException::Couldn't get a Connection!", e);
            throw new DatabaseServiceException(true, e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
    }