# CittyTraffic

1 - Compiler et envoyer dans la gateway :
    !! LOGIN A CHANGER DANS backend/compil.sh !!
    cd backend/
    ./compile.sh
2 - Depuis la gateway exécuter le code : 
    spark-submit --num-executors 4 --executor-cores 2 --executor-memory 512M --master yarn --class bigdata.CittyTrafficProject CittyTrafficProject-0.0.1.jar