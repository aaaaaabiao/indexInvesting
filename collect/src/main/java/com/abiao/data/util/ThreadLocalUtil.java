package com.abiao.data.util;

import java.util.Map;

public class ThreadLocalUtil {

    public static ThreadLocal<Map<String, Map<String, String>>> commitDataPath = new ThreadLocal<>();
}
