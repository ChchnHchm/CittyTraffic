# !/bin/bash
readonly LOGIN=nalves
mvn clean; mvn compile; mvn package; scp ./target/Exec-CittyTrafficProcessing.jar $LOGIN@147.210.117.54:/home/$LOGIN/ ;scp ./target/Exec-CittyTrafficCleaner.jar $LOGIN@147.210.117.54:/home/$LOGIN/ ;