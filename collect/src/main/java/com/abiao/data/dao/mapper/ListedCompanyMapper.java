package com.abiao.data.dao.mapper;

import com.abiao.data.model.ListedCompany;
import com.abiao.data.model.StockIndicator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 上市公司mapper
 * */
public interface ListedCompanyMapper {


    /**
     * 获取A股所有股票代码
     * */
    @Select("select stock_code from listed_company")
    List<String> selectStockCode();




    /**
     * 获取A股所有股票代码
     * */
    @Select("select * from listed_company")
    List<ListedCompany> selectListCompany();


    /**
     * 获取所有上市公司(不包含退市),只包上交所和深交所的股票
     * */
    @Select("select * from listed_company where out_market_date is null and (stock_code like '6%' or stock_code like '0%' or stock_code like '3%')")
    List<ListedCompany> selectAllListed();

    /**
     * 获取A股所有股票代码
     * */
    @Select("select * from listed_company where stock_code = #{stockCode}")
    ListedCompany selectListCompanyByStockCode(@Param("stockCode") String stockCode);



    /**
     * 获取某一个A股所有代码
     * */
    @Select("select * from listed_company where in_market_date < #{date} and (out_market_date > #{date} or out_market_date is null)")
    List<ListedCompany> selectListCompanyByDate(@Param("date") String date);


    /**
     * 插入指数成分股
     * */
    @Insert("<script>"
            + "insert into listed_company (stock_code, stock_name, industry, in_market_date, out_market_date, update_time) values "
            + "<foreach item='item' index='index' collection='listedCompanies' separator=','>"
            +      "(#{item.stockCode}, #{item.stockName}, #{item.industry}, #{item.inMarketDate}, #{item.outMarketDate}, #{item.updateTime})"
            + "</foreach>"
            + "</script>")
    void batchInsert(@Param("listedCompanies") List<ListedCompany> listedCompanies);
}
