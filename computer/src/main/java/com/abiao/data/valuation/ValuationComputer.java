package com.abiao.data.valuation;

import com.abiao.data.model.IndexValuation;
import com.abiao.data.model.StockIndicator;

import java.util.List;

public interface ValuationComputer {

    IndexValuation compute(List<StockIndicator> stockIndicators);
}
