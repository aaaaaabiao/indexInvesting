#!/bin/bash
set -e
set -x

work_space=$(dirname $0)

dt=$(date +"%Y%m%d")
jar="${work_space}/collect-1.0-SNAPSHOT-jar-with-dependencies.jar"
data_dir="${work_space}/data"

listed_company_data="${data_dir}/listed_company/${dt}"
stock_indicator_data="${data_dir}/stock_indicator/${dt}"

rm -rf $listed_company_data
rm -rf $stock_indicator_data


# 更新上市公司
java -cp $jar com.abiao.data.action.update.ListedCompanyAutoUpdateAction ${listed_company_data}

# 更新指标
java -cp $jar com.abiao.data.action.update.StockIndicatorAutoUpdateAction ${stock_indicator_data}



