package com.abiao.data.util;

import com.abiao.data.constant.Constant;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    public static void parseCommitPath(String source, String commitPath) {
        Map<String, Map<String, String>> pathMap = ThreadLocalUtil.commitDataPath.get();
        Map<String, String> path = new HashMap<>();
        path.put("data", commitPath + "commit_data");
        path.put("error", commitPath + "error_data");
        pathMap.put(source, path);
    }


    public static String getFullStockCode(String stockCode) {
        if (stockCode.startsWith("6")) {
            return "SH" + stockCode;
        }else if (stockCode.startsWith("0")) {
            return "SZ" + stockCode;
        }else if (stockCode.startsWith("3")) {
            return "SZ" + stockCode;
        }else if (stockCode.startsWith("8")) {
            return "BJ" + stockCode;
        }else if (stockCode.startsWith("4")) {
            return "BJ" + stockCode;
        }

        return "";
    }
}
