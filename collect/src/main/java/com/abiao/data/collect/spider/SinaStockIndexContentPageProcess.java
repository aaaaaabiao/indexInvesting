package com.abiao.data.collect.spider;
import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.ListedCompanyMapper;
import com.abiao.data.model.IndexContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.*;
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

    private Site site = Site
            .me()
            .setRetrySleepTime(3)
            .setSleepTime(1000)
            .setTimeOut(10000)
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
            .addHeader("cookie", "device_id=0667a293ccc5e4278186e6537c064958; Hm_lvt_1db88642e346389874251b5a1eded6e3=1664696087; xq_a_token=dd874a8d08edaeb9cdc493d9239c447738426b96; xqat=dd874a8d08edaeb9cdc493d9239c447738426b96; xq_r_token=7a97f1ad16a7e6074c744f820621875ef03d3f3d; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTY2ODI5ODQ3NywiY3RtIjoxNjY1ODMxOTYzMjU0LCJjaWQiOiJkOWQwbjRBWnVwIn0.RZH26cpd-zM-hksNid5o0MrmxvIADkbZ__yRmkz5nb8_ii5FfJdGPQ9D_z-6Dq3G3SqlKPI1KU4VIIHQTs1Dnyt5H7LCMyaMIzTraJsy5B5swAFDNTVtewBb9RE1TpCU61klThStSCp607RxkiXB8qahtufIRFaMxgub5j9ouDq5MFVdqHk1UGdLg5aY-tYaD1XUKo6TupA4WEeEqvep1fL3MFfmGgETFG_DGs_Hxy9JgeFvUF6daXWfZx-N_1ioSmOIhf6ACASAWkFfV4tKRU8t38dU3pJnHIDYNoHpoJQrjz84aJh0n36CtIXx1Q772MACZQ9_nQi0BFujag2w4g; u=851665831995554; s=dc1548rf55; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1665912587");

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
        ListedCompanyMapper listedCompanyMapper = sqlSession.getMapper(ListedCompanyMapper.class);
        List<String> stockCodes = listedCompanyMapper.selectStockCode();
        return generateRequests(stockCodes);
    }

    public static Request[] createTestRequests() {
        List<String> stockCodes = Arrays.asList("000423");
        return generateRequests(stockCodes);
    }

    public static void main(String[] args) {
//        Request[] requests = createRequests();

//        Request[] requests = createTestRequests();
//        Spider.create(new SinaStockIndexContentPageProcess())
//                .addRequest(requests)
//                .addPipeline(new DBPipeline())
//                .thread(1).run();


        ResultItems resultItems = Spider.create(new SinaStockIndexContentPageProcess())
                .get("https://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpXiangGuan/stockid/000423.phtml");

        List<IndexContent> indexContents = resultItems.get("indexContents");
        int a = 0;
    }
}
