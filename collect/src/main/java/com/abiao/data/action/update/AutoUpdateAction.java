package com.abiao.data.action.update;

import com.abiao.data.collect.Collect;
import com.abiao.data.action.db.DbOp;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Slf4j
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


    protected abstract String commitPath();


    protected List<T> diff(Map<String, T> left, Map<String, T> right) {
        MapDifference<String, T> mapDifference = Maps.difference(left, right);
        Map<String, T> update = mapDifference.entriesOnlyOnLeft();
        Collection<T> updateValue = update.values();
        return new ArrayList<>(updateValue);
    }


    protected void commit(List<T> commit) throws IOException {
        String commitSerialization = JSON.toJSONString(commit);
        FileUtils.write(new File(commitPath()), commitSerialization + "\n", true);
        log.info("commit file success");
    }

    /**
     * 通用更新逻辑
     * */
    public void action() throws IOException {
        List<T> collectData = collect.collect();
        List<T> dbData = dbOp.select();
        List<T> diff = diff(collectData, dbData);
        commit(diff);

    }
}
