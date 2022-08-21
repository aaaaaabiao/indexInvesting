package com.abiao.data.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * 股票所属的指数
 */

public class SinaStockIndexContentPageProcess implements PageProcessor {

    private Site site = Site.me().setRetrySleepTime(3).setSleepTime(1000).setTimeOut(10000);

    public void process(Page page) {
        Selectable selectable = page.getHtml().xpath("//*[@id=\"Table2\"]/tbody/tr");
        Request request = page.getRequest();
        List<Selectable> nodes = selectable.nodes();

        for (Selectable node : nodes) {
            List<String> texts = node.xpath("//div/text()").all();
            if (texts.contains("沪深300") && texts.contains("000300")) {
                String stockCode = request.getExtra("stockCode") + "";
                String indexName = texts.get(0);
                String indexCode = texts.get(1);
                String inDate = texts.get(2);
                String outDate = texts.get(3);
                System.out.println(stockCode + ":" + texts);
            }

        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String[] stockCodes = new String[]{
                "688981",
                "688599",
                "605499",
                "603195",
                "601012",
                "600900",
                "600795",
                "600585",
                "600519",
                "600362",
                "600309",
                "600196",
                "600161",
                "600104",
                "600085",
                "600009",
                "600000",
                "300759",
                "300750",
                "300529",
                "300413",
                "002709",
                "002414",
                "000963"
        };
        Request[] requests = new Request[stockCodes.length];
        for (int i = 0; i < stockCodes.length; i++) {
            String url = String.format("https://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpXiangGuan/stockid/%s.phtml", stockCodes[i]);
            Request request = new Request(url);
            request.putExtra("stockCode", stockCodes[i]);
            requests[i] = request;
        }

        Spider.create(new SinaStockIndexContentPageProcess())
                .addRequest(requests)
                .thread(5).run();
    }
}
