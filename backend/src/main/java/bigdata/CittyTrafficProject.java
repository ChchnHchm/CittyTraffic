package bigdata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import com.ctc.wstx.util.StringUtil;

import scala.Function1;
import scala.Tuple2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import static org.apache.spark.sql.functions.input_file_name;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.shell.Count;
import org.apache.hadoop.io.NullWritable;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.callUDF;
import static org.apache.spark.sql.functions.avg;
import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.desc;

public class CittyTrafficProject {
        static List<String> paths;
        static String path="C:/Users/mcabi/Desktop/m2/ple/CittyTraffic/data/fichiersTraitBs";
        // static String pathP3="/user/auber/data_ple/citytraffic/Premiers\\ résultats/Fichiers\\ traitВs/P3/";

        static private void initPaths(){
                paths=new ArrayList<String>();
                paths.add("/P4/");
                paths.add("/P5/");
                paths.add("/P17/");
                paths.add("/P9/");
                paths.add("/P19/");
                paths.add("/P23/");
                paths.add("/P24/");
                paths.add("/P26/");

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
                if(fullPath.contains("Sortie")){
                     return 2;
                }
                else{
                     return 1;
                }
        }, DataTypes.IntegerType);
        spark.udf().register("separateTimeFirst", (String time) -> {
                try{
                        return Integer.parseInt(time)/100;

                }catch(NumberFormatException e){
                        System.err.println(e.getMessage());
                }
                return 0;

        }, DataTypes.IntegerType);
        spark.udf().register("separateTimeSecond", (String time) -> {
                try{
                        return Integer.parseInt(time)%100;

                }catch(NumberFormatException e){
                        System.err.println(e.getMessage());
                }
                return 0;

        }, DataTypes.IntegerType);
        for (String currPath : paths) {
                Dataset<Row> df = spark.read().option("pathGlobFilter","*.csv").option("recursiveFileLookup","true").option("header","true").csv(path+currPath);
                long totalCount=df.count();
                Boolean sensExist=false;
                for (String name : df.schema().names()) {
                        long nb=df.filter(df.col(name).isNull()).count();
                        if((double)nb/totalCount>0.5){
                                df=df.drop(df.col(name));
                        }
                        if(name.equals("HEURE/MINUTE")){
                                df=df.withColumn("HEURE", callUDF("separateTimeFirst",col(name)));
                                df=df.withColumn("MINUTE", callUDF("separateTimeSecond",col(name)));
                                df=df.drop(name);
                        }
                        if(name.equals("SECONDE/CENTIEME")){
                                df=df.withColumn("SECONDE", callUDF("separateTimeFirst",col(name)));
                                df=df.withColumn("CENTIEME", callUDF("separateTimeSecond",col(name)));
                                df=df.drop(name);
                        }
                        else if(name.equals("Type Véhicules") || name.equals("Type Véhicule") ){
                                df=df.withColumnRenamed(name,"TYPE");
                        }
                        else{
                                String newName=StringUtils.stripAccents(name);
                                newName=newName.toUpperCase();
                                df=df.withColumnRenamed(name,newName);
                                name=newName;
                        }
                        if(name.equals("CATEGORIE")){
                                df=df.drop(name);
                        }
                        else if(name.equals("INTER-ESSIEUX")){
                                df=df.withColumnRenamed(name, "SER");
                        }
                        else if(name.equals("SENS")){
                                if(df.select(name).first().get(0).getClass().equals(String.class)){
                                        df=df.withColumn("SENS", callUDF("get_only_file_name",col(name)));
                                }
                                sensExist=true;
                        }
                }
                if(!sensExist){
                        df=df.withColumn("SENS", callUDF("get_only_file_name",input_file_name()));

                }
                df.write().mode(SaveMode.Overwrite).option("header",true).parquet(path+"/../result"+currPath);   
        }
        
        Dataset<Row> df=spark.read().option("recursiveFileLookup","true").option("header","true").parquet(path+"/../result/");
        df.show();
        df.write().mode(SaveMode.Overwrite).option("header",true).parquet(path+"/../result/SUPER");   

        
       
      
        
        
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
