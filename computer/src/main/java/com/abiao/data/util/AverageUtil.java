package com.abiao.data.util;

import java.util.List;

public class AverageUtil {

    public static double harmonicMean(Double[] x) {
        int len = x.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum += 1 / x[i];
        }
        return len / sum;
    }

    public static double harmonicMean(List<Double> x) {
        Double[] p = x.toArray(new Double[x.size()]);
        return harmonicMean(p);
    }
}
