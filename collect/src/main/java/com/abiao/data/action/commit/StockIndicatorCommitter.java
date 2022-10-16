package com.abiao.data.action.commit;
import com.abiao.data.dao.CommonDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.StockIndicator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


public class StockIndicatorCommitter extends DbCommitter<StockIndicator>{

    @Parameter(names = { "--input"}, description = "commit file path", required = true)
    String input = "";

    public StockIndicatorCommitter(CommonDao<StockIndicator> dao) {
        super(dao);
    }

    @Override
    protected String commitPath() {
        return input;
    }

    @Override
    protected Class<StockIndicator> getCls() {
        return StockIndicator.class;
    }


    public static void main(String[] args) {
        CommonDao<StockIndicator> dao = new StockIndicatorDao();
        StockIndicatorCommitter committer = new StockIndicatorCommitter(dao);
        JCommander.newBuilder()
                .addObject(committer)
                .build()
                .parse(args);
        committer.run();
    }
}
