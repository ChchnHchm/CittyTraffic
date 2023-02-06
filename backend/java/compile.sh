# !/bin/bash
read -p "Entrer votre login:" LOGIN
mvn clean; mvn compile; mvn package; scp ./target/Exec-CityTrafficProcessing.jar $LOGIN@147.210.117.54:/home/$LOGIN/ ;scp ./target/Exec-CityTrafficCleaner.jar $LOGIN@147.210.117.54:/home/$LOGIN/ ;