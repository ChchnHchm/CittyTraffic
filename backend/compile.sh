# !/bin/bash
readonly LOGIN=nalves
mvn clean; mvn compile; mvn package; scp ./target/CittyTraffic-Data-CittyTrafficData.jar $LOGIN@147.210.117.54:/home/$LOGIN/ ;scp ./target/CittyTraffic-Cleaner-CittyTrafficCleaner.jar $LOGIN@147.210.117.54:/home/$LOGIN/ ;