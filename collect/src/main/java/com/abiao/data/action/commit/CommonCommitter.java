package com.abiao.data.action.commit;
import com.abiao.data.dao.CommonDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.StockIndicator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.sqlite.core.DB;

import javax.xml.transform.Source;


public class CommonCommitter implements Committer{


    @Parameter(names = { "--input"}, description = "commit file path", required = true)
    String input = "";

    @Parameter(names = { "--source"}, description = "source", required = true)
    String source = "";


    @Override
    public void run() {
        CommitterFactory factory = new CommitterFactory();
        Committer committer = factory.getCommitter(source, input);
        committer.run();
    }

    public static void main(String[] args) {

        CommonCommitter commonCommitter = new CommonCommitter();
        JCommander.newBuilder()
                .addObject(commonCommitter)
                .build()
                .parse(args);
        commonCommitter.run();

    }
}
