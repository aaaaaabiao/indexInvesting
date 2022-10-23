#!/bin/bash
set -e
set -x

work_space=$(dirname $0)

dt=$(date +"%Y%m%d")
collect_jar="${work_space}/collect-1.0-SNAPSHOT-jar-with-dependencies.jar"
computer_jar="${work_space}/computer-1.0-SNAPSHOT-jar-with-dependencies.jar"
data_dir="${work_space}/data"

listed_company_data="${data_dir}/listed_company/${dt}"
stock_indicator_data="${data_dir}/stock_indicator/${dt}"
index_valuation_data="${data_dir}/index_valuation/${dt}"

rm -rf $listed_company_data
rm -rf $stock_indicator_data
rm -rf $index_valuation_data


# 更新上市公司
java -cp $collect_jar com.abiao.data.action.update.ListedCompanyAutoUpdateAction ${listed_company_data}
java -cp $collect_jar com.abiao.data.action.commit.CommonCommitter --source listed_company --input ${listed_company_data}


# 更新指标
java -cp $collect_jar com.abiao.data.action.update.StockIndicatorAutoUpdateAction ${stock_indicator_data}
java -cp $collect_jar com.abiao.data.action.commit.CommonCommitter --source stock_indicator --input ${stock_indicator_data}


#计算估值
#java -cp $computer_jar com.abiao.data.valuation.IndexValuationAction --commitMode local --localOutput ${index_valuation_data}
#java -cp $collect_jar com.abiao.data.action.commit.CommonCommitter --source index_valuation --input ${stock_indicator_data}




