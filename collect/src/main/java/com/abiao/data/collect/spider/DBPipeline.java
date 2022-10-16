package com.abiao.data.collect.spider;

import com.abiao.data.connect.SqlSessionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;



@Slf4j
public class DBPipeline implements Pipeline {

    SqlSession sqlSession = SqlSessionBuilder.build();

    @Override
    public void process(ResultItems resultItems, Task task) {
//        IndexContentMapper indexContentMapper = sqlSession.getMapper(IndexContentMapper.class);
//        List<IndexContent> indexContents = resultItems.get("indexContents");
//        for (IndexContent indexContent : indexContents) {
//            indexContentMapper.insert(indexContent);
//        }
//        sqlSession.commit();
//        log.info("commit success");
    }
}
