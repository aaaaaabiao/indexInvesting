package com.abiao.data.spider;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.StockMapper;
import com.abiao.data.model.IndexContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 股票所属的指数
 * https://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpXiangGuan/stockid/688599.phtml
 */

@Slf4j
public class SinaStockIndexContentPageProcess implements PageProcessor {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private Site site = Site.me().setRetrySleepTime(3).setSleepTime(1000).setTimeOut(10000);

    public void process(Page page) {
        Selectable selectable = page.getHtml().xpath("//*[@id=\"Table2\"]/tbody/tr");
        Request request = page.getRequest();
        List<Selectable> nodes = selectable.nodes();
        String stockCode = request.getExtra("stockCode") + "";
        List<IndexContent> indexContents = new ArrayList<>();
        for (Selectable node : nodes) {
            List<String> texts = node.xpath("//div/text()").all();
            String indexName = texts.get(0).trim();
            String indexCode = texts.get(1).trim();
            String inDateStr = StringEscapeUtils.escapeHtml3(texts.get(2)).trim();
            String outDateStr = StringEscapeUtils.escapeHtml3(texts.get(3)).trim();
            if ("&nbsp;".equals(inDateStr)) inDateStr = null;
            if ("&nbsp;".equals(outDateStr) || StringUtils.isBlank(outDateStr)) outDateStr = null;
            try {
                if (StringUtils.isNotBlank(indexCode) && StringUtils.isNotBlank(inDateStr)) {
                    //validator
                    Date inDate = df.parse(inDateStr);
                    if (StringUtils.isNotBlank(outDateStr)) {
                        Date outDate = df.parse(outDateStr);
                    }
                    IndexContent indexcontent = new IndexContent(indexCode, indexName, stockCode, inDateStr, outDateStr);
                    indexContents.add(indexcontent);
                }
            }catch (Exception e) {
                log.error("inDateStr:{}, outDateStr:{}, exception:{}", inDateStr, outDateStr, e);
                e.printStackTrace();
            }
        }
        log.info("stockCode:{}, size:{}", stockCode, indexContents.size());
        page.putField("indexContents", indexContents);
    }

    public Site getSite() {
        return site;
    }



    public static Request[] generateRequests(List<String> stockCodes) {
        Request[] requests = new Request[stockCodes.size()];
        for (int i = 0; i < stockCodes.size(); i++) {
            String stockCode = stockCodes.get(i);
            String url = String.format("https://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpXiangGuan/stockid/%s.phtml", stockCode);
            Request request = new Request(url);
            request.putExtra("stockCode", stockCode);
            requests[i] = request;
        }

        return requests;
    }

    public static Request[] createRequests() {
        SqlSession sqlSession = new SqlSessionBuilder().build();
        StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
        List<String> stockCodes = stockMapper.getAllStockCode();
        return generateRequests(stockCodes);
    }

    public static Request[] createTestRequests() {
        List<String> stockCodes = Arrays.asList("000423");
        return generateRequests(stockCodes);
    }

    public static void main(String[] args) {
//        Request[] requests = createRequests();

        Request[] requests = createTestRequests();
        Spider.create(new SinaStockIndexContentPageProcess())
                .addRequest(requests)
                .addPipeline(new DBPipeline())
                .thread(1).run();
    }
}
