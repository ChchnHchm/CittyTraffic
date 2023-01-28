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


public class CittyTrafficData {
        static List<String> paths;
        // static String path="C:/Users/mcabi/Desktop/m2/ple/CittyTraffic/data/brute";
        static String pathResult="/user/nalves/cittyTrafic/Result";

        
        public static void main(String[] args) throws Exception {
            SparkSession spark = SparkSession.builder().appName("CittyTrafficProject").config("spark.master", "local").getOrCreate();
            JavaSparkContext sc =new JavaSparkContext(spark.sparkContext());

            JavaPairRDD<NullWritable, Text> rddWritable= sc.sequenceFile(pathResult, NullWritable.class, Text.class);
            JavaRDD<String> rddString=rddWritable.mapValues(text-> text.toString()).values();
            rddString.take(20).forEach(t->System.out.println(t));
    //     JavaPairRDD<NullWritable, Text> rddFinal =dfFinal.toJavaRDD().mapToPair(row ->new Tuple2<NullWritable,Text>(NullWritable.get(),new Text(row.toString())));
    //     rddFinal.saveAsHadoopFile(pathResult, NullWritable.class, Text.class, SequenceFileOutputFormat.class);

    //    JavaSparkContext sc =new JavaSparkContext(spark.sparkContext());
    //    JavaPairRDD<NullWritable, Text> rddWritable= sc.sequenceFile(pathResult, NullWritable.class, Text.class);

    //    JavaRDD<String> rddString=rddWritable.mapValues(text-> text.toString()).values();
       
    //    rddString.take(20).forEach(t->System.out.println(t));
        
        
	}
}
