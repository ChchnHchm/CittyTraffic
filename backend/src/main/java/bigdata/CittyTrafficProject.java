package bigdata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import static org.apache.spark.sql.functions.input_file_name;
import org.apache.commons.io.FilenameUtils;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.callUDF;
import static org.apache.spark.sql.functions.avg;
import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.desc;

public class CittyTrafficProject {
        static String pathP3="C:/Users/mcabi/Desktop/m2/ple/CittyTraffic/data/fichiersTraitBs/P3/";
	public static void main(String[] args) throws Exception {

       /*         
        Dataset<Row> df = spark.read().option("header", true).option("inferSchema", true).csv("/user/auber/data_ple/citytraffic/Data_tube_example.csv");
        df.show();
        df.printSchema();
        df.write().format("sequenceFile");
        */
        //spark initialization part

        
        SparkSession spark = SparkSession.builder().appName("CittyTrafficProject").config("spark.master", "local").getOrCreate();

        spark.udf().register("get_only_file_name", (String fullPath) -> {
           int lastIndex = fullPath.lastIndexOf("/");
           return fullPath.substring(lastIndex, fullPath.length() - 1);
          }, DataTypes.StringType);
      
        
        Dataset<Row> df = spark.read().option("pathGlobFilter","*.csv").option("recursiveFileLookup","true").option("header","true").csv(pathP3);
        df.withColumn("filename", callUDF("get_only_file_name",input_file_name())).show();
        df.show();
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
