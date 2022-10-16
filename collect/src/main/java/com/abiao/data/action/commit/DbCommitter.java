package com.abiao.data.action.commit;
import com.abiao.data.dao.CommonDao;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class DbCommitter<T> implements Committer<T> {

    private CommonDao<T> dao;

    public DbCommitter(CommonDao<T> dao) {
        this.dao = dao;
    }

    protected abstract String commitPath();

    protected abstract Class<T> getCls();

    @Override
    public void run() {
        try {
            List<String> lines = FileUtils.readLines(new File(commitPath()), Charset.defaultCharset());
            List<T> commit = new ArrayList<>();
            for (String line : lines) {
                commit.addAll(JSON.parseArray(line, getCls()));
            }
            dao.batchUpdate(commit);
            log.info("commit success! class:{}, count:{}", getCls().getSimpleName(), commit.size());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
