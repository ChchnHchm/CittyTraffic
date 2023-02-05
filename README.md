# CittyTraffic

1 - Compiler et envoyer dans la gateway :
    !! LOGIN A CHANGER DANS backend/compil.sh !!
    cd backend/
    ./compile.sh
2 - Depuis la gateway exécuter le code : 
    1 - kinit 
    2 - export HADOOP_CLASSPATH=`hadoop classpath`:`hbase mapredcp`:/etc/hbase/conf:/usr/hdp/3.0.0.0-1634/hbase/lib/*
    3 - time spark-submit --num-executors 4 --executor-cores 2 --executor-memory 512M --master yarn --class bigdata.CittyTrafficCleaner Exec-CittyTrafficCleaner.jar
    4 - time spark-submit --num-executors 4 --executor-cores 2 --executor-memory 512M --master yarn --class bigdata.CittyTrafficProcessing Exec-CittyTrafficProcessing.jar
    
