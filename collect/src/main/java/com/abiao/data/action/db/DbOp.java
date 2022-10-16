package com.abiao.data.action.db;

import java.util.List;

public interface DbOp<T> {
    List<T> select();

    void batchUpdate(List<T> update);
}
