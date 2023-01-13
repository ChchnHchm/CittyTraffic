!/bin/bash

mvn clean; mvn compile; mvn package; scp /net/cremi/hchouchane/espaces/travail/prog\ large\ echelle/Projet/CittyTraffic/backend/target/CittyTrafficProject-0.0.1.jar hchouchane@147.210.117.54:/home/hchouchane/Project