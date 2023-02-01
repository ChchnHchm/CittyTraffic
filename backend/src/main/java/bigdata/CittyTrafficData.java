package bigdata;


import java.util.List;

import scala.Tuple2;
import org.apache.spark.sql.SparkSession;
import bigdata.utilties.CittyTrafficValue;
import bigdata.utilties.CittyTrafficHbase;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.hadoop.io.Text;

public class CittyTrafficData {
        static List<String> paths;
        // static String path="C:/Users/mcabi/Desktop/m2/ple/CittyTraffic/data/brute";
        static String pathResult="/user/nalves/cittyTrafic/Result";

        
        public static void main(String[] args) throws Exception {
            SparkSession spark = SparkSession.builder().appName("CittyTrafficProject").config("spark.some.config.option", "some-value").getOrCreate();
            JavaSparkContext sc =new JavaSparkContext(spark.sparkContext());
            CittyTrafficHbase hbase=new CittyTrafficHbase();
            JavaPairRDD<Text, Text> rddWritable= sc.sequenceFile(pathResult, Text.class, Text.class);

            // comptes les différents type de véhicule en fonction de la date , l'heure et le radar 
            JavaPairRDD<String,CittyTrafficValue>rddCountTypes =rddWritable.mapToPair(tuple->{
                String keyString=tuple._1().toString();
                //VALEUR
                String[] listValues=tuple._2().toString().split(",");
                int speed=0;
                int sens=0;
                try{
                    speed=Integer.parseInt(listValues[1]);
                }
                catch(Exception e){

                }
                try{
                    sens=Integer.parseInt(listValues[2]);
                }
                catch(Exception e){

                }
                CittyTrafficValue values=new CittyTrafficValue(speed, sens,listValues[0]);
                return new Tuple2<String,CittyTrafficValue>(keyString,values); 
            });
            
            rddCountTypes= rddCountTypes.reduceByKey((x,y)->{
                x.sumTwoValues(y);
                return x;
            });
            rddCountTypes.map(t->{
                CittyTrafficValue val=t._2();
                val.meanOfSpeed();
                hbase.addRow(t._1(),val);
                return t;
            });
            hbase.close();
            // JavaPairRDD<String,CittyTrafficValue> rddString=rddWritable.mapToPair(tuple->{
            //     String valueString=tuple._2().toString();
            //     String[] listValString=valueString.split(",");
            //     int speed=0;
            //     int sens=0;
            //     try{
            //         speed=Integer.parseInt(listValString[0]);
            //     }
            //     catch(Exception e){

            //     }
            //     try{
            //         sens=Integer.parseInt(listValString[1]);
            //     }
            //     catch(Exception e){

            //     }
            //     CittyTrafficValue values=new CittyTrafficValue(speed, sens);
            //     return new Tuple2<String,CittyTrafficValue>(tuple._1().toString(),values);
            // });
            // rddString.countByKey().forEach((t, l) -> System.out.println(t+" "+l));
            
            // On transforme le PairRDD en dataset
            // JavaRDD<Row> rddRow=rddWritable.mapValues(text->{
            //     String[] stringsOfRow=text.toString().replace("[", "").replace("]", "").split(",");
            //     int speed=0;
            //     int sens=0;
            //     Date day=new Date(0);
            //     int hour=0;
            //     int minute=0;
            //     try{
            //         speed=Integer.parseInt(stringsOfRow[4]);

            //     }catch(Exception e){

            //     }
            //     try{
            //         day=Date.valueOf(stringsOfRow[0]);

            //     }catch(Exception e){

            //     }
            //     try{
            //          hour=Integer.parseInt(stringsOfRow[1]);
            //          minute=Integer.parseInt(stringsOfRow[2]);
            //          sens=Integer.parseInt(stringsOfRow[5]);
                     
                     
            //     }catch(Exception e){
            //     }
            //     return RowFactory.create(day,hour,minute,stringsOfRow[3],speed,sens,stringsOfRow[6] );

            // }).values();
            // StructType schemaCitty=DataTypes.createStructType(new StructField[]{
            //     DataTypes.createStructField("JOUR", DataTypes.DateType, false),
            //     DataTypes.createStructField("HEURE", DataTypes.IntegerType, false),
            //     DataTypes.createStructField("MINUTE", DataTypes.IntegerType, false),
            //     DataTypes.createStructField("TYPE", DataTypes.StringType, false),
            //     DataTypes.createStructField("VITESSE", DataTypes.IntegerType, false),
            //     DataTypes.createStructField("SENS", DataTypes.IntegerType, false),
            //     DataTypes.createStructField("RADAR", DataTypes.StringType, false),

                

            // });
            // Dataset dataset=spark.createDataFrame(rddRow, schemaCitty);
            // RelationalGroupedDataset groupedHour=dataset.groupBy("JOUR","HEURE","RADAR");
            // RelationalGroupedDataset groupedType=dataset.groupBy("TYPE");
            // groupedType.count().show();
            // Dataset hourDataset=groupedHour.count();
            // Dataset speedDataset=groupedHour.agg(functions.avg("VITESSE"));
            // speedDataset.show();
            // speedDataset.filter(speedDataset.col("avg(VITESSE)").$greater(0)).show();
            // hourDataset=hourDataset.sort("JOUR","HEURE");
            // hourDataset.show();
            
    //     JavaPairRDD<NullWritable, Text> rddFinal =dfFinal.toJavaRDD().mapToPair(row ->new Tuple2<NullWritable,Text>(NullWritable.get(),new Text(row.toString())));
    //     rddFinal.saveAsHadoopFile(pathResult, NullWritable.class, Text.class, SequenceFileOutputFormat.class);

    //    JavaSparkContext sc =new JavaSparkContext(spark.sparkContext());
    //    JavaPairRDD<NullWritable, Text> rddWritable= sc.sequenceFile(pathResult, NullWritable.class, Text.class);

    //    JavaRDD<String> rddString=rddWritable.mapValues(text-> text.toString()).values();
       
    //    rddString.take(20).forEach(t->System.out.println(t));
        
        
	}
}
