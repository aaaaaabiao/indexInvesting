package com.abiao.data.spider.akshare;

import java.util.List;
import java.util.Map;

public abstract class AKShareSource<T> {

    private String method;
    private Map<String, Object> params;

    public AKShareSource(String method) {
        this.method = method;
    }

    public AKShareSource(String method, Map<String, Object> params) {
        this.method = method;
        this.params = params;
    }

    private static String api = "http://127.0.0.1:8080/api/public/";

    protected List<Map<String, Object>> pull() {
        return null;
    }
}
