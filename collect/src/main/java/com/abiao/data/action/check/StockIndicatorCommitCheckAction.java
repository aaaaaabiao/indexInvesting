package com.abiao.data.action.check;

import com.abiao.data.model.StockIndicator;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 指标数据质检任务
 * */
public class StockIndicatorCommitCheckAction {

    @Parameter(names = { "--input"}, description = "commit file path", required = true)
    String input = "";

    public void check() {
        try {
            List<List<StockIndicator>> data = new ArrayList<>();
            List<String> lines = FileUtils.readLines(new File(input), Charset.defaultCharset());
            for (String line : lines) {
                data.add(JSON.parseArray(line, StockIndicator.class));
            }

            if (!amountCheck(data)) System.exit(-1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 量级检查
     * */
    public boolean amountCheck(List<List<StockIndicator>> data) {
//        Map<Integer, List<List<StockIndicator>>> ret = data.stream().collect(Collectors.groupingBy(List::size));
//
//        if (ret.size() != 1) {
//
//        }else {
//            return true;
//        }

        return true;
    }


    public static void main(String[] args) {

        StockIndicatorCommitCheckAction action = new StockIndicatorCommitCheckAction();

        JCommander.newBuilder()
                .addObject(action)
                .build()
                .parse(args);
        action.check();

    }
}
