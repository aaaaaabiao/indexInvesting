package com.abiao.data.valuation;

import com.abiao.data.model.IndexValuation;
import com.abiao.data.model.StockIndicator;
import com.abiao.data.util.AverageUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 计算等权PB
 * */
@Slf4j
public class PBWEValuationComputer implements ValuationComputer {
    @Override
    public IndexValuation compute(List<StockIndicator> stockIndicators) {
        IndexValuation indexValuation = new IndexValuation();


        List<Double> peTTMList = stockIndicators.stream()
                .filter(item -> item.getPb() != null)
                .map(item -> item.getPb())
                .collect(Collectors.toList());
        double harmonicMeanPETTM = AverageUtil.harmonicMean(peTTMList);

        indexValuation.setPbEw(harmonicMeanPETTM);

        return indexValuation;
    }
}
