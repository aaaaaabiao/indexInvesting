package com.abiao.data.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;


import org.apache.hc.client5.http.fluent.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class AKUtil {

    private static String api = "http://127.0.0.1:8080/api/public/";
    public static List<Map<String, Object>> get(String method, Map<String, Object> params) throws IOException {
        String request = api + method;
        List<NameValuePair> nvps = new ArrayList<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
            }
            request +=  "?" + URLEncodedUtils.format(nvps, "utf-8");
        }
        String resp = Request.get(request).execute().returnContent().asString(Charset.defaultCharset());
        JSONArray array = JSON.parseArray(resp);
        List<Map<String, Object>> ret = new ArrayList<>();
        for (Object obj : array) {
            JSONObject jsonObj = (JSONObject) obj;
            ret.add(jsonObj);
        }

        log.info("ak get req:{}, param:{}", request, params);
        return ret;
    }
}
