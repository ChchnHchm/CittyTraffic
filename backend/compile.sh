!/bin/bash
readonly LOGIN = hchouchane
mvn clean; mvn compile; mvn package; scp /net/cremi/LOGIN/espaces/travail/prog\ large\ echelle/Projet/CittyTraffic/backend/target/CittyTrafficProject-0.0.1.jar LOGIN@147.210.117.54:/home/LOGIN/