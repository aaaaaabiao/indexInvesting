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
public abstract class DbCommitter<T> implements Committer {


    private String commitFilePath;

    private CommonDao<T> dao;

    public DbCommitter(String commitFilePath, CommonDao<T> dao) {
        this.commitFilePath = commitFilePath;
        this.dao = dao;
    }

    protected abstract Class<T> getCls();

    @Override
    public void run() {
        try {
            List<String> lines = FileUtils.readLines(new File(commitFilePath), Charset.defaultCharset());
            List<T> allCommit = new ArrayList<>();
            for (String line : lines) {
                allCommit.addAll(JSON.parseArray(line, getCls()));
            }
            if (allCommit.size() == 0) return;
            int count = 0;
            List<T> commit = new ArrayList<>();
            for (T data : allCommit) {
                commit.add(data);
                count++;
                if (count == 10000) {
                    dao.batchUpdate(commit);
                    log.info("commit success! class:{}, count:{}", getCls().getSimpleName(), commit.size());
                    count = 0;
                    commit.clear();
                }
            }
            dao.batchUpdate(commit);
            log.info("commit success! class:{}, count:{}", getCls().getSimpleName(), commit.size());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("commit failed! class:{}", getCls().getSimpleName());
            throw new RuntimeException(e);
        }
    }
}
