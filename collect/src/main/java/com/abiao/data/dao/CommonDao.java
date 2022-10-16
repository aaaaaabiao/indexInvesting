package com.abiao.data.dao;

import java.util.List;

public interface CommonDao<T> {

    /**
     * 查询全库所有
     * */
    List<T> selectAll();


    /**
     * 根据Id查询
     * */
    List<T> selectByKey(String id);


    /**
     * 根据唯一ID查询
     * */
    T selectByUniqueKey(String id);


    /**
     * 批量更新
     * */
    void batchUpdate(List<T> updates);
}
