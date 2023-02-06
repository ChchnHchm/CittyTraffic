package bigdata;

import java.util.*;  
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.spark.sql.SparkSession;
import bigdata.utilties.CityTrafficValue;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.hadoop.io.Text;
import scala.Tuple2;
import bigdata.utilties.*;


public class CityTrafficProcessing extends Configured implements Tool,Serializable{
	public static String user="nalves";
    public  static  byte[] TABLE_NAME = null;
    private static  String[]  FAMILYS = {"key","type","direction","measure"}; 
	public  static String pathResult=null;

		public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
			if (admin.tableExists(table.getTableName())) {
				admin.disableTable(table.getTableName());
				admin.deleteTable(table.getTableName());
			}
			admin.createTable(table);
		}

		public static void createTable(Connection connect) {
			try {
				final Admin admin = connect.getAdmin();
				HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
                for(int i = 0 ; i < FAMILYS.length ; i++){
					tableDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(FAMILYS[i])));
				} 
				createOrOverwrite(admin, tableDescriptor);
				admin.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		private void addRow(String key,CityTrafficValue values,Table table){
			try {
				// String[] keyString=key.split(","); //Date =0 Heure=1 Radar=2
				//Instantiating Put
				Put p = new Put(Bytes.toBytes(key));

				//Adding value
				//key
				// p.addColumn(Bytes.toBytes("key"),Bytes.toBytes("date"),Bytes.toBytes(keyString[0]));
				// p.addColumn(Bytes.toBytes("key"),Bytes.toBytes("hour"),Bytes.toBytes(keyString[1]));
				// p.addColumn(Bytes.toBytes("key"),Bytes.toBytes("radar"),Bytes.toBytes(keyString[2]));
				
				//type
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("bus"),Bytes.toBytes(Integer.toString(values.getCountBUS())));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("moto"),Bytes.toBytes(Integer.toString(values.getCountMOTO())));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("velo"),Bytes.toBytes(Integer.toString(values.getCountVELO())));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("vl"),Bytes.toBytes(Integer.toString(values.getCountVL())));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("pl"),Bytes.toBytes(Integer.toString(values.getCountPL())));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("ut"),Bytes.toBytes(Integer.toString(values.getCountUT())));
				
				//direction
				p.addColumn(Bytes.toBytes("direction"),Bytes.toBytes("exit"),Bytes.toBytes(Integer.toString( values.getSensSortie())));
				p.addColumn(Bytes.toBytes("direction"),Bytes.toBytes("enter"),Bytes.toBytes(Integer.toString(values.getSensEntree())));

				//measure
				p.addColumn(Bytes.toBytes("measure"),Bytes.toBytes("speed"),Bytes.toBytes(Integer.toString(values.getSpeed())));


				//Inserting data
				table.put(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int run(String[] args) throws IOException {
			Connection connection = ConnectionFactory.createConnection(getConf());
			createTable(connection);
			Table table =  connection.getTable(TableName.valueOf(TABLE_NAME)) ;
			//add the lines from the rdd
            SparkSession spark = SparkSession.builder().appName("CityTrafficProject").config("spark.some.config.option", "some-value").getOrCreate();
            JavaSparkContext sc =new JavaSparkContext(spark.sparkContext());
            //get RDD
            JavaPairRDD<Text, Text> rddWritable= sc.sequenceFile(pathResult, Text.class, Text.class);
            //change the type of the rdd
            JavaPairRDD<String,CityTrafficValue>rddCountTypes =rddWritable.mapToPair(tuple->{
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
                CityTrafficValue values=new CityTrafficValue(speed, sens,listValues[0]);
                return new Tuple2<String,CityTrafficValue>(keyString,values); 
            });
            rddCountTypes= rddCountTypes.reduceByKey((x,y)->{
                x.sumTwoValues(y);
                return x;
            });
			
			rddCountTypes.collect().forEach(tuple->addRow(tuple._1(),tuple._2(), table));
			return 0;
		}

	

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrer votre login:");
		String login = sc.nextLine();
		user = login;
		TABLE_NAME = Bytes.toBytes(user+":CityTrafficHbase");
		pathResult="/user/"+user+"/cityTraffic/Result";
		sc.close();
		int exitCode = ToolRunner.run(HBaseConfiguration.create(), new CityTrafficProcessing(), args);
		System.exit(exitCode);
	}
}
