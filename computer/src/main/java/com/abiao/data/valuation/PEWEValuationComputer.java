package com.abiao.data.valuation;

import com.abiao.data.model.IndexValuation;
import com.abiao.data.model.StockIndicator;
import com.abiao.data.util.AverageUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 计算等权PE
 * */
@Slf4j
public class PEWEValuationComputer implements ValuationComputer {
    @Override
    public IndexValuation compute(List<StockIndicator> stockIndicators) {
        IndexValuation indexValuation = new IndexValuation();


        List<Double> peTTMList = stockIndicators.stream()
                .filter(item -> item.getPeTTM() != null)
                .map(item -> item.getPeTTM())
                .collect(Collectors.toList());
        double harmonicMeanPETTM = AverageUtil.harmonicMean(peTTMList);

        indexValuation.setPeEw(harmonicMeanPETTM);

        return indexValuation;
    }
}
