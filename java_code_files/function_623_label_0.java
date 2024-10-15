    public void migrate() throws DataMigrationException
    {
        try {
            this.solrInstance.deleteByQuery("*:*");
            this.solrInstance.commit();
        } catch (SolrServerException | IOException e) {
            throw new DataMigrationException("Error while performing Solr query to empty the search core", e);
        }
    }