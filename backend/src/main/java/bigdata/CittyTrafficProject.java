package bigdata;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Null;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;


import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.api.java.JavaRDD;

import static org.apache.spark.sql.functions.input_file_name;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.callUDF;


public class CittyTrafficProject {
        static List<String> paths;
        // static String path="C:/Users/mcabi/Desktop/m2/ple/CittyTraffic/data/brute";
        static String path="/user/auber/data_ple/citytraffic/ResultatCSV";
        static String pathResult="/user/nalves/cittyTrafic/Result";

        static private void initPaths(){
                paths=new ArrayList<String>();
                //VIKINGS
                paths.add("P4");
                // paths.add("P5");
                // paths.add("P17");
                // //MIXTRA
                // paths.add("P9");
                // paths.add("P19");
                // paths.add("P23");
                // paths.add("P24");
                // paths.add("P26");
                // //CAMERA
                // paths.add("P1");
                // paths.add("P10");
                // paths.add("P12");
                // paths.add("P13");

        }
        public static void main(String[] args) throws Exception {

       /*         
        Dataset<Row> df = spark.read().option("header", true).option("inferSchema", true).csv("/user/auber/data_ple/citytraffic/Data_tube_example.csv");
        df.show();
        df.printSchema();
        df.write().format("sequenceFile");
        */
        //spark initialization part
        initPaths();
        
        SparkSession spark = SparkSession.builder().appName("CittyTrafficProject").config("spark.master", "local").getOrCreate();
        spark.udf().register("get_only_file_name", (String fullPath) -> {
                if(fullPath.contains("Sortie") || fullPath.contains("Talence") || fullPath.contains("Talence") || fullPath.contains("S") || fullPath.contains("2")){
                     return 2;
                }
                else{
                     return 1;
                }
        }, DataTypes.IntegerType);
        spark.udf().register("changeFormat", (String time) -> {
                try{
                        return time.substring(0,time.length()-2)+":"+time.substring(time.length()-2);

                }catch(IndexOutOfBoundsException e){
                        return "00";
                }
                
        }, DataTypes.StringType);

        spark.udf().register("getSecond", (String time) -> {
                try{
                        return ":"+time.substring(0,time.length()-2);

                }catch(IndexOutOfBoundsException e){
                        return "00";
                }
                
        }, DataTypes.StringType);

        spark.udf().register("horodateToDay", (String time) -> {
                try{
                        return time.split(" ")[0];

                }catch(IndexOutOfBoundsException e){
                        return "0/0/0";
                }
                
    

        }, DataTypes.StringType);

        spark.udf().register("horodateToHour", (String time) -> {
                try{
                        return time.split(" ")[1];


                }catch(IndexOutOfBoundsException e){
                        return "00";
                }
                
    

        }, DataTypes.StringType);

        spark.udf().register("horodateToHour", (String time) -> {
                try{
                        return time.split(" ")[1].split(":")[0]+time.split(":")[1];


                }catch(IndexOutOfBoundsException e){
                        return "00";
                }
                
    

        }, DataTypes.StringType);

        spark.udf().register("speedClean", (String time) -> {
                try{
                        return time.replace("V=", "");


                }catch(IndexOutOfBoundsException e){
                        return "00";
                }
                
    

        }, DataTypes.StringType);

        Dataset<Row> dfFinal = spark.emptyDataFrame();

        for (String currPath : paths) {
                Dataset<Row> df = spark.read().option("pathGlobFilter","*.csv").option("recursiveFileLookup","true").option("header","true").csv(path+"/"+currPath+"/");
                long totalCount=df.count();
                Boolean sensExist=false;
                Boolean sensIsVers=false;
                Boolean typeExist=false;
                Boolean vitesseExist=false;
                df.show();
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
                                df=df.withColumn("TEMPS", callUDF("horodateToHour",col(name)));

                                df=df.drop(name);
                                name=null;
                                
                                
                        }
                        else if(name.equals("HEURE")){
                                df=df.withColumnRenamed(name, "TEMPS");

                        }
                        else if(name.equals("SECONDE")){
                                df=df.drop(name);
                        }

                        else if(name.equals("HEURE/MINUTE")){
                                df=df.withColumn("TEMPS", callUDF("changeFormat",col(name)));
                                df=df.drop(name);
                                name=null;

                        }
                       
                        else if(name.equals("SECONDE/CENTIEME")){
                                df=df.drop(name);
                                name=null;

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
                                name=null;
                        }


                        
                        else{
                                long nb=df.filter(df.col(name).isNull()).count();
                                if((double)nb/totalCount>0.5){
                                        df=df.drop(df.col(name));
                                        name=null;

                                }
                        }

                }
                if(!sensExist){
                        df=df.withColumn("SENS", callUDF("get_only_file_name",input_file_name()));

                }
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
       dfFinal.show();
        List<String> list=dfFinal.select("RADAR").distinct().as(Encoders.STRING()).collectAsList();
        List<Long> coutnList=new ArrayList<Long>();
        for (String s : list) {
               coutnList.add(dfFinal.filter(col("RADAR").$eq$eq$eq(s)).count());
                
        }
        int i=0;
        for (Long long1 : coutnList) {

                System.out.println(list.get(i)+" "+long1);
                i++;

        }

        JavaPairRDD<NullWritable, Text> rddFinal =dfFinal.toJavaRDD().mapToPair(row ->new Tuple2<NullWritable,Text>(NullWritable.get(),new Text(row.toString())));
        rddFinal.saveAsHadoopFile(pathResult, NullWritable.class, Text.class, SequenceFileOutputFormat.class);

       JavaSparkContext sc =new JavaSparkContext(spark.sparkContext());
       JavaPairRDD<NullWritable, Text> rddWritable= sc.sequenceFile(pathResult, NullWritable.class, Text.class);
       
       JavaRDD<String> rddString=rddWritable.mapValues(tuple-> tuple.toString()).values();
       
       rddString.take(20).forEach(t->System.out.println(t));
       
        /*
        //read the csv input file into a dataframe
        List<Dataset<Row>> list=new ArrayList<Dataset<Row>>();
        //System.out(pathP3);
        File folder= null;
        try {
         folder = new File(pathP3);
         System.out.println(folder.getName());
        } catch (Exception e) {
                System.err.println(e);
        }
        if(!folder.exists()){
                System.out.println("FOLDER NULL");
                return;
        } 
        System.out.println("name :"+folder.getName());
        
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles == null){
                System.out.println("PATH NULL");
                return;
        } 
        for (File file : listOfFiles) {
                if(FilenameUtils.getExtension(file.getName()).equals("csv")){
                        list.add(spark.read().option("header", true).option("inferSchema", true).csv(pathP3+file.getName()));
                        System.out.println(file.getName());
                }
        }
        for (Dataset<Row> dataset : list) {
                dataset.show();
        }
        for (Dataset<Row> dataset : list) {
                dataset.show();
        }
        // //jobs of user 7
        // Dataset<Row> seven = df.filter("user_id = 7");
        // Dataset<Row> proc_seven = seven.select(avg("nb_proc"));
        // Dataset<Row> proc_all = df.select(avg("nb_proc"));
        // System.out.println("Average for all users:\n");
        // proc_all.show();
        // System.out.println("Average for user 7:\n");
        // proc_seven.show();
        // //the answer : user 7 asked on average for 8807.812413793103 processes, while the global average was 5062.713338652354

        // //find the resources used by jobs in core-hours
        // System.out.println("calculating core-hours:\n");
        // Dataset<Row> core_hours = df.withColumn("hours", expr("running_time / 360.0")).selectExpr("job_id", "nb_proc * hours as `core-hours`");
        // core_hours.show(10);

        // //show statistics of core-hours
        // System.out.println("Core-hours statistics:\n");
        // core_hours.select("`core-hours`").describe().show();

        // //less resources than asked for?
        // System.out.println("How many jobs received less resources than asked for?\n");
        // Dataset<Row> less_res = df.where("nb_asked_proc > nb_proc");
        // System.out.printf("Answer: %d\n", less_res.count());
        // less_res.show();
        // //yes, eleven times

        // //resources and waiting time per queue
        // System.out.println("Resource usage and wait time per queue:\n");
        // Dataset<Row> per_queue = df.withColumn("core_hours", expr("nb_proc * running_time / 360.0")).
        //                             groupBy("queue_number").
        //                             agg(expr("sum(core_hours) as resources"),
        //                             expr("max(wait_time) as max_wait"),
        //                             expr("count(wait_time) as nb_jobs")).
        //                             orderBy(desc("queue_number"));
        // per_queue.show(20);

        System.out.println("done\n");
        */
	}
}
