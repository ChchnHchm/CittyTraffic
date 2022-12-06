package bigdata;

import java.io.IOException;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.api.java.JavaRDD;


public class CittyTrafficProject {
	public static void main(String[] args) throws Exception {
        //spark initialization part
        SparkSession spark = SparkSession.builder().appName("CittyTrafficProject").config("spark.some.config.option", "some-value").getOrCreate();
        //read the csv input file into a dataframe
        Dataset<Row> df = spark.read().option("header", true).option("inferSchema", true).csv("/user/auber/data_ple/citytraffic/Data_tube_example.csv");
        df.show();
        df.printSchema();
        df.write().format("sequenceFile");

	}
}
