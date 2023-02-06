package bigdata;

import java.util.ArrayList;
import java.util.List;

import java.util.*;  

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;


import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.input_file_name;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.callUDF;


public class CityTrafficCleaner {
        static String user="nalves";
        static List<String> paths;
        static String path="/user/auber/data_ple/citytraffic/ResultatCSV";
        static String pathResult="/user/"+user+"/cityTraffic/Result";

        static private void initPaths(){
                paths=new ArrayList<String>();
                //VIKINGS
                paths.add("P4");
                paths.add("P5");
                paths.add("P17");
                //MIXTRA
                paths.add("P9");
                paths.add("P19");
                paths.add("P23");
                paths.add("P24");
                paths.add("P26");
                //CAMERA
                paths.add("P1");
                paths.add("P10");
                paths.add("P12");
                paths.add("P13");

        }
        public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
	System.out.println("Entrer votre login:");
	String login = sc.nextLine();
	user = login;
        path="/user/auber/data_ple/citytraffic/ResultatCSV";
        pathResult="/user/"+user+"/cityTraffic/Result";
	sc.close();

        initPaths();
        
        SparkSession spark = SparkSession.builder().appName("CityTrafficProject").config("spark.master", "local").getOrCreate();
        spark.udf().register("get_only_file_name", (String fullPath) -> {
                if(fullPath.contains("Sortie") || fullPath.contains("Talence") || fullPath.contains("Talence") || fullPath.contains("S") || fullPath.contains("2")){
                     return "2";
                }
                else{
                     return "1";
                }
        }, DataTypes.StringType);
        spark.udf().register("changeFormatHour", (String time) -> {
                try{
                        if(time.length()<=2){
                                return "0";
                        }
                        return time.substring(0,time.length()-2);

                }catch(IndexOutOfBoundsException e){
                        return "0";
                }
                
        }, DataTypes.StringType);

        

        spark.udf().register("getSecond", (String time) -> {
                try{
                        return ":"+time.substring(0,time.length()-2);

                }catch(IndexOutOfBoundsException e){
                        return "0";
                }
                
        }, DataTypes.StringType);

        spark.udf().register("horodateToDay", (String time) -> {
                try{
                        return time.split(" ")[0].replace("/","-");

                }catch(IndexOutOfBoundsException e){
                        return "2022-1-1";
                }
                
    

        }, DataTypes.StringType);


        spark.udf().register("horodateToHour", (String time) -> {
                try{
                        String hour=time.split(" ")[1].split(":")[0];
                        if(hour.charAt(0)=='0'){
                                if(hour.length()>1){
                                        return hour.charAt(1);
                                }
                        }
                        return hour;


                }catch(IndexOutOfBoundsException e){
                        return "0";
                }
                
    

        }, DataTypes.StringType);

        

        spark.udf().register("speedClean", (String speed) -> {
                        return speed.replace("V=", "");
        }, DataTypes.StringType);

        spark.udf().register("changeDateFormat", (String time) -> {
                        if(time.contains("/")){
                                try{
                                        String[] listDate=time.split("/");
                                        return "20"+listDate[2]+"-"+listDate[1]+"-"+listDate[0];
                                }catch(IndexOutOfBoundsException e){
                                        return "2022-1-1";
                                }
                        }
                        else{
                                return "2022-10-"+time;
                        }


                        
        }, DataTypes.StringType);

        spark.udf().register("heureMinuteToHeure", (String time) -> {
                try{
                        return time.split(":")[0];

                }catch(IndexOutOfBoundsException e){
                        return "0";
                }
                
        }, DataTypes.StringType);

        

        spark.udf().register("typeUnification", (String time) -> {
                //Velo ou trottinette
                if(time.equals("VELO") || time.equals("Vélo")||time.equals("Trottinette")||time.equals("V�lo �lectrique")||time.equals("EDPM")||time.equals("V�lo Electrique")){
                        return "VELO";
                }
                else if(time.equals("PL")||time.equals("PL_2")||time.equals("PL_1")){
                        return "PL";
                }
                else if(time.equals("VL")){
                        return "VL";
                }
                else if(time.equals("MOTO")||time.equals("2RM")||time.equals("2R")){
                        return "MOTO";
                }
                else if(time.equals("PL/Bus") || time.equals("BUS")||time.equals("Bus articul�")||time.equals("Bus ")||time.equals("Bus")){
                        return "BUS";
                }
                else if(time.equals("UT")){
                        return "UT";
                }
                return "INCONNU";
        }, DataTypes.StringType);

        Dataset<Row> dfFinal = spark.emptyDataFrame();

        for (String currPath : paths) {
                Dataset<Row> df = spark.read().option("pathGlobFilter","*.csv").option("recursiveFileLookup","true").option("header","true").csv(path+"/"+currPath+"/");
                long totalCount=df.count();
                Boolean sensExist=false;
                Boolean sensIsVers=false;
                Boolean typeExist=false;
                Boolean vitesseExist=false;
                for (String name : df.schema().names()) {

                        //enléve les accents et mets toute les noms de colonnes en majuscules
                        String newName=name.replace("�","e");
                        newName=StringUtils.stripAccents(newName);
                        newName=newName.toUpperCase();
                        df=df.withColumnRenamed(name,newName);
                        name=newName;

                         //Gestion de la direction
                        if(name.contains("VERS ") || name.contains("ENTRANT")){
                                if(!sensIsVers){
                                        sensIsVers=true;
                                        sensExist=true;
                                        df=df.withColumn("SENS",functions.when(df.col(name).isNotNull(),1).when(df.col(name).isNull(), 2) );
                                }
                               df= df.drop(name);
                               

                        }
                        
                        else if(name.contains("DETECTION ")){
                                if(!sensIsVers){
                                        sensIsVers=true;
                                        sensExist=true;
                                        df=df.withColumn("SENS",callUDF("get_only_file_name",col(name)));
                                }
                               df= df.drop(name);
                        }
                        else if(name.equals("SENS")){
                                if(df.select(name).first().get(0).getClass().equals(String.class)){
                                        df=df.withColumn("SENS", callUDF("get_only_file_name",col(name)));
                                }
                                sensExist=true;
                        }

                        //Gestion de la date et du temps
                        else if(name.equals("HORODATE")){
                                df=df.withColumn("JOUR", callUDF("horodateToDay",col(name)));
                                df=df.withColumn("HEURE", callUDF("horodateToHour",col(name)));


                                df=df.drop(name);
                                
                                
                        }
                        else if(name.equals("JOUR")){
                                df=df.withColumn("JOUR", callUDF("changeDateFormat",col(name)));

                        }
                        else if(name.equals("HEURE")){
                                df=df.withColumn("HEURE", callUDF("heureMinuteToHeure",col(name)));

                        }
                        else if(name.equals("SECONDE")){
                                df=df.drop(name);
                        }

                        else if(name.equals("HEURE/MINUTE")){
                                df=df.withColumn("HEURE", callUDF("changeFormatHour",col(name)));
                                df=df.drop(name);

                        }
                       
                        else if(name.equals("SECONDE/CENTIEME")){
                                df=df.drop(name);

                        }
                        //Si la vitesse existe on ajoute 
                        else if(name.equals("VITESSE")){
                              vitesseExist=true;
                              df=df.withColumn(name, callUDF("speedClean",col(name)));
                        }
                        //Gestion du type de vehicule
                        else if(name.equals("TYPE VEHICULES") || name.equals("TYPE VEHICULE") || (name.equals("CATEGORIE")&& !typeExist ) ){
                                df=df.withColumnRenamed(name,"TYPE");
                                name="TYPE";
                                typeExist=true;
                        }
                       
                        else if(name.equals("CATEGORIE")  || name.equals("CENTIEME") || name.equals("ID") || name.equals("SER") ||name.equals("INTER-ESSIEUX")){
                                df=df.drop(name);
                        }


                        
                        else{
                                long nb=df.filter(df.col(name).isNull()).count();
                                if((double)nb/totalCount>0.5){
                                        df=df.drop(df.col(name));

                                }
                        }

                }
                if(!sensExist){
                        df=df.withColumn("SENS", callUDF("get_only_file_name",input_file_name()));

                }
                df=df.withColumn("TYPE", callUDF("typeUnification", df.col("TYPE")));

                df=df.withColumn("RADAR", functions.lit(currPath));
                if(!vitesseExist){
                        df=df.withColumn("VITESSE", functions.lit(null));
                }
                if(dfFinal.isEmpty()){
                        dfFinal=df;
                }
                else{
                        dfFinal=dfFinal.unionByName(df);
                }
        }
        dfFinal=dfFinal.select(col("JOUR"),col("HEURE"),col("TYPE"),col("VITESSE"),col("SENS"),col("RADAR"));
        JavaPairRDD<Text, Text> rddFinal =dfFinal.toJavaRDD().mapToPair(row ->new Tuple2<Text,Text>(new Text(row.getString(0)+","+row.getString(1)+","+row.getString(5)),new Text(row.getString(2)+","+row.getString(3)+","+row.getString(4))));
        rddFinal.saveAsHadoopFile(pathResult, Text.class, Text.class, SequenceFileOutputFormat.class);
       
        
        
	}
}
