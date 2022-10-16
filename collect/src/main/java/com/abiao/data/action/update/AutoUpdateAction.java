package com.abiao.data.action.update;

import com.abiao.data.collect.Collect;
import com.abiao.data.action.db.DbOp;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public abstract class AutoUpdateAction<T> {

    private Collect<T> collect;
    private DbOp<T> dbOp;

    public AutoUpdateAction(Collect<T> collect, DbOp<T> dbOp) {
        this.collect = collect;
        this.dbOp = dbOp;
    }

    /**
     *
     * 采集的数据与库里的数据进行diff
     * */
    protected abstract List<T> diff(List<T> collectData, List<T> dbData);


    protected List<T> diff(Map<String, T> left, Map<String, T> right) {
        MapDifference<String, T> mapDifference = Maps.difference(left, right);
        Map<String, T> update = mapDifference.entriesOnlyOnLeft();
        Collection<T> updateValue = update.values();
        return new ArrayList<>(updateValue);
    }


    protected void commit(List<T> commit) {
        if (commit.size() > 0) {
            dbOp.batchUpdate(commit);
        }
    }

    /**
     * 通用更新逻辑
     * */
    public void action() {

        List<T> collectData = collect.collect();
        List<T> dbData = dbOp.select();
        List<T> diff = diff(collectData, dbData);
        commit(diff);

    }
}
