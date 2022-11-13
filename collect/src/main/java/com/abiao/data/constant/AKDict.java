package com.abiao.data.constant;

public class AKDict {


    //A股所有股票代码和名称,http://127.0.0.1:8080/api/public/stock_info_a_code_name
    public static String stockInfoACodeName = "stock_info_a_code_name";

    //A股个股基本信息,http://127.0.0.1:8080/api/public/stock_individual_info_em?symbol=000001
    public static String stockIndividualInfo = "stock_individual_info_em";

    //股票指标信息采集, http://127.0.0.1:8080/api/public/stock_a_lg_indicator?symbol=000001
    public static String stockIndicator = "stock_a_lg_indicator";

    //暂停/终止上市-上证
    public static String sseDelisting = "stock_info_sh_delist";

    //终止(暂停)上市-深证
    public static String szseDelisting = "stock_info_sz_delist";

    //指数历史行情数据,http://127.0.0.1:8080/api/public/index_zh_a_hist?symbol=000990&period=daily
    public static String indexZhAHist = "index_zh_a_hist";





}
