# CityTraffic

## 1 - Compiler et envoyer dans la gateway :  
    `1. cd backend/java`    
    `2. ./compile.sh`  Le compile.sh compile le code java et l'envoie sur la gateway avec scp  
    
## 2 - Depuis la gateway exécuter le code :   
    `1.   kinit`     
    `2.   export HADOOP_CLASSPATH=`hadoop classpath`:`hbase mapredcp`:/etc/hbase/conf:/usr/hdp/3.0.0.0-1634/hbase/lib/*`    
    `3.   time spark-submit --num-executors 4 --executor-cores 2 --executor-memory 512M --master yarn --class bigdata.CityTrafficCleaner Exec-CityTrafficCleaner.jar`    
    `4.   time spark-submit --num-executors 4 --executor-cores 2 --executor-memory 512M --master yarn --class bigdata.CityTrafficProcessing Exec-CityTrafficProcessing.jar`    
## 4 - Allumer le backend :  
  `1. ssh -L 1369:127.0.0.1:3000 hchouchane@147.210.117.54`  
  `!! Le reste est a faire depuis la gateway !!`   
  `2. kinit`   
  `3. export HADOOP_CLASSPATH=`hadoop classpath`:`hbase mapredcp`:/etc/hbase/conf:/usr/hdp/3.0.0.0-1634/hbase/lib/*`   
  `4. cd backend/server`  
  `5. npm ci`  
  `6. npm run start`  

## 4 - Allumer le frontend :  
`!! apres avoir allumé le backend !! `   
      `1. cd frontend`  
      `2. npm ci`   
      `3. npm run dev` 
