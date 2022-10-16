package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.ListedCompanyMapper;
import com.abiao.data.model.ListedCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ListedCompanyDao implements CommonDao<ListedCompany>{
    SqlSession sqlSession = SqlSessionBuilder.build();
    ListedCompanyMapper listedCompanyMapper = sqlSession.getMapper(ListedCompanyMapper.class);


    @Override
    public List<ListedCompany> selectAll() {
        return listedCompanyMapper.selectListCompany();
    }

    @Override
    public List<ListedCompany> selectByKey(String id) {
        return null;
    }

    @Override
    public ListedCompany selectByUniqueKey(String id) {
        return listedCompanyMapper.selectListCompanyByStockCode(id);
    }


    public List<ListedCompany> selectByDate(String date) {
        return listedCompanyMapper.selectListCompanyByDate(date);
    }


    //获取所有上市公司(不包含退市),只包上交所和深交所的股票
    public List<ListedCompany> selectAllListed() {
        return listedCompanyMapper.selectAllListed();
    }


    @Override
    public void batchUpdate(List<ListedCompany> updates) {
        listedCompanyMapper.batchInsert(updates);
        sqlSession.commit();
    }
}
