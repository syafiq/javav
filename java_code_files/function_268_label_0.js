    private Set search(String attributeName, String attributeValue,
                       String[] attrs) throws CertStoreException
    {
        String filter = attributeName + "=" + filterEncode(attributeValue);
        System.out.println(filter);
        if (attributeName == null)
        {
            filter = null;
        }
        DirContext ctx = null;
        Set set = new HashSet();
        try
        {

            ctx = connectLDAP();

            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            constraints.setCountLimit(0);
            for (int i = 0; i < attrs.length; i++)
            {
                String temp[] = new String[1];
                temp[0] = attrs[i];
                constraints.setReturningAttributes(temp);

                String filter2 = "(&(" + filter + ")(" + temp[0] + "=*))";
                if (filter == null)
                {
                    filter2 = "(" + temp[0] + "=*)";
                }
                NamingEnumeration results = ctx.search(params.getBaseDN(),
                    filter2, constraints);
                while (results.hasMoreElements())
                {
                    SearchResult sr = (SearchResult)results.next();
                    // should only be one attribute in the attribute set with
                    // one
                    // attribute value as byte array
                    NamingEnumeration enumeration = ((Attribute)(sr
                        .getAttributes().getAll().next())).getAll();
                    while (enumeration.hasMore())
                    {
                        Object o = enumeration.next();
                        set.add(o);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new CertStoreException(
                "Error getting results from LDAP directory " + e);

        }
        finally
        {
            try
            {
                if (null != ctx)
                {
                    ctx.close();
                }
            }
            catch (Exception e)
            {
            }
        }
        return set;
    }