    public boolean shouldExecute(XWikiDBVersion startupVersion)
    {
        XWikiDBVersion ltsVersion = new XWikiDBVersion(141015000);
        XWikiDBVersion afterLTSVersion = new XWikiDBVersion(150000000);
        // Execute the migration if the version is either before the LTS version or equal to or larger than the
        // afterLTSVersion and before the version of this migration.
        // We only need to execute this migration once on the main wiki.
        return getXWikiContext().isMainWiki() && (startupVersion.compareTo(ltsVersion) < 0
            || (startupVersion.compareTo(afterLTSVersion) >= 0 && startupVersion.compareTo(getVersion()) < 0));
    }