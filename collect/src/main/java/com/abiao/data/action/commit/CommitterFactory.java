package com.abiao.data.action.commit;

import com.abiao.data.constant.Constant;
import com.abiao.data.dao.*;
import com.abiao.data.model.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CommitterFactory {


    static class ListedCompanyCommitter extends DbCommitter<ListedCompany> {
        public ListedCompanyCommitter(String commitFilePath, CommonDao<ListedCompany> dao) {
            super(commitFilePath, dao);
        }
        @Override
        protected Class<ListedCompany> getCls() {
            return ListedCompany.class;
        }
    }

    static class StockIndicatorCommitter extends DbCommitter<StockIndicator> {
        public StockIndicatorCommitter(String commitFilePath, CommonDao<StockIndicator> dao) {
            super(commitFilePath, dao);
        }
        @Override
        protected Class<StockIndicator> getCls() {
            return StockIndicator.class;
        }
    }


    static class IndexContentCommitter extends DbCommitter<IndexContent> {
        public IndexContentCommitter(String commitFilePath, CommonDao<IndexContent> dao) {
            super(commitFilePath, dao);
        }
        @Override
        protected Class<IndexContent> getCls() {
            return IndexContent.class;
        }
    }


    static class IndexValuationCommitter extends DbCommitter<IndexValuation> {
        public IndexValuationCommitter(String commitFilePath, CommonDao<IndexValuation> dao) {
            super(commitFilePath, dao);
        }
        @Override
        protected Class<IndexValuation> getCls() {
            return IndexValuation.class;
        }
    }



    static class IndexDailyDataCommitter extends DbCommitter<IndexDailyData> {
        public IndexDailyDataCommitter(String commitFilePath, CommonDao<IndexDailyData> dao) {
            super(commitFilePath, dao);
        }
        @Override
        protected Class<IndexDailyData> getCls() {
            return IndexDailyData.class;
        }
    }

    public Committer getCommitter(String source, String input) {

        Committer committer = null;

        switch (source) {
            case Constant.listedCompany:
                committer = new ListedCompanyCommitter(input, new ListedCompanyDao());
                break;
            case Constant.stockIndicator:
                committer = new StockIndicatorCommitter(input, new StockIndicatorDao());
                break;
            case Constant.indexContent:
                committer = new IndexContentCommitter(input, new IndexContentDao());
                break;
            case Constant.indexValuation:
                committer = new IndexValuationCommitter(input, new IndexValuationDao());
                break;
            case Constant.indexDailyData:
                committer = new IndexDailyDataCommitter(input, new IndexDailyDataDao());
                break;
            default:
                log.error("error source:{}", source);
        }
        return committer;
    }
}
