    protected void addHttpData(InterfaceHttpData data) {
        if (data == null) {
            return;
        }
        if (maxFields > 0 && bodyListHttpData.size() >= maxFields) {
            throw new HttpPostRequestDecoder.TooManyFormFieldsException();
        }
        List<InterfaceHttpData> datas = bodyMapHttpData.get(data.getName());
        if (datas == null) {
            datas = new ArrayList<InterfaceHttpData>(1);
            bodyMapHttpData.put(data.getName(), datas);
        }
        datas.add(data);
        bodyListHttpData.add(data);
    }