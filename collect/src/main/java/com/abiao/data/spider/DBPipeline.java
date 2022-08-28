package com.abiao.data.spider;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.IndexMapper;
import com.abiao.data.model.IndexContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Slf4j
public class DBPipeline implements Pipeline {

    SqlSession sqlSession = SqlSessionBuilder.build();

    @Override
    public void process(ResultItems resultItems, Task task) {
        IndexMapper indexMapper = sqlSession.getMapper(IndexMapper.class);
        List<IndexContent> indexContents = resultItems.get("indexContents");
        for (IndexContent indexContent : indexContents) {
            indexMapper.insertIndexContext(indexContent);
        }
        sqlSession.commit();
        log.info("commit success");
    }
}
