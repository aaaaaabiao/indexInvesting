package com.abiao.data.action.update;

import com.abiao.data.action.db.DbOp;
import com.abiao.data.action.db.IndexDailyDataDbOp;
import com.abiao.data.action.db.StockIndicatorDbOp;
import com.abiao.data.collect.Collect;
import com.abiao.data.collect.IndexDailyDataCollect;
import com.abiao.data.collect.StockIndicatorCollect;
import com.abiao.data.collect.spider.XueQiuKinePageProcess;
import com.abiao.data.dao.FollowIndexDao;
import com.abiao.data.dao.IndexDailyDataDao;
import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.FollowIndex;
import com.abiao.data.model.IndexDailyData;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.model.StockIndicator;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Spider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 股票指标实时更新
 * */
@Slf4j
public class IndexDailyDataAutoUpdateAction extends AutoUpdateAction<IndexDailyData> {

    private String commitFilePath;

    public IndexDailyDataAutoUpdateAction(Collect<IndexDailyData> collect, DbOp<IndexDailyData> dbOp, String commitFilePath) {
        super(collect, dbOp);
        this.commitFilePath = commitFilePath;
    }


    @Override
    protected List<IndexDailyData> diff(List<IndexDailyData> collectData, List<IndexDailyData> dbData) {
        Map<String, IndexDailyData> m1 = collectData.stream().collect(Collectors.toMap(IndexDailyData::getTradeDate, Function.identity()));
        Map<String, IndexDailyData> m2 = dbData.stream().collect(Collectors.toMap(IndexDailyData::getTradeDate, Function.identity()));
        List<IndexDailyData> updateValue = diff(m1, m2);
        log.info("diff count:{}", updateValue.size());
        return updateValue;
    }

    @Override
    protected String commitPath() {
        return commitFilePath;
    }


    public static void main(String[] args) {
        String commitPath = args[0];
        FollowIndexDao followIndexDao = new FollowIndexDao();
        IndexDailyDataDao indexDailyDataDao = new IndexDailyDataDao();
        List<FollowIndex> followIndices = followIndexDao.selectAll().stream()
                .filter(item -> !item.getIndexCode().equals("000000")).collect(Collectors.toList());


        int count = 1;
        log.info("start update index_daily_data info, follow index count:{}", followIndices.size());
        for (FollowIndex followIndex : followIndices) {
            log.info("index:{}, followIndex:{}", count++, followIndex);
            String indexCode = followIndex.getIndexCode();
            Collect<IndexDailyData> indexDailyDataCollect = new IndexDailyDataCollect(indexCode);
            DbOp<IndexDailyData> indexDailyDataDbOp = new IndexDailyDataDbOp(indexCode, indexDailyDataDao);
            try {
                new IndexDailyDataAutoUpdateAction(indexDailyDataCollect, indexDailyDataDbOp, commitPath).action();
            }catch (Exception e) {
                log.error("indexCode:{}, e:{}", indexCode ,e);
            }
        }

    }
}
