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
            List<T> commit = new ArrayList<>();
            int count = 0;
            for (String line : lines) {
                commit.addAll(JSON.parseArray(line, getCls()));
                count++;
                //todo::分批commit
                if (count == 1000) {
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
