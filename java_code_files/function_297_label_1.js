    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        List<Class<?>> loadedClasses = new ArrayList<Class<?>>(interfaces.length);
        
        for (String name : interfaces) {
            Class<?> clazz = Class.forName(name, false, classLoader);
            loadedClasses.add(clazz);
        }
        
        return Proxy.getProxyClass(classLoader, loadedClasses.toArray(new Class[loadedClasses.size()]));
    }