package bigdata.utilties;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;




import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;


public class CittyTrafficHbase extends Configured {
        private static final String[]  FAMILYS = {"key","type","direction","measure"}; 
		private static final byte[] TABLE_NAME = Bytes.toBytes("nalves:CittyTrafficHbase");
		private Configuration conf;
		private Connection connect;
		private Table table;


		public CittyTrafficHbase(){
			try{
				//Instantiating conf
				this.conf = HBaseConfiguration.create();

				//Instantiating admin class
				//HBaseAdmin admin = new HBaseAdmin(con);
				this.connect = ConnectionFactory.createConnection(getConf());
				final Admin admin = this.connect.getAdmin();

				//Instantiating table descriptor
				HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME));

				//Instantiating colum families
				for(int i = 0 ; i < FAMILYS.length ; i++){
					tableDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(FAMILYS[i])));
				} 
				createOrOverwrite(admin, tableDescriptor);

				this.table = this.connect.getTable(TableName.valueOf(TABLE_NAME));
				admin.close();
			}  catch (Exception e) {
				e.printStackTrace();
				//System.exit(-1);
			}
		}

		private static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
			if (admin.tableExists(table.getTableName())) {
				admin.disableTable(table.getTableName());
				admin.deleteTable(table.getTableName());
			}
			admin.createTable(table);
		}

		public int addRow(String key,CittyTrafficValue values){
			try {

				String[] keyString=key.split(","); //Date =0 Heure=1 Radar=2
				//Instantiating Put
				Put p = new Put(Bytes.toBytes("row"));

				//Adding value
				//key
				p.addColumn(Bytes.toBytes("key"),Bytes.toBytes("date"),Bytes.toBytes(keyString[0]));
				p.addColumn(Bytes.toBytes("key"),Bytes.toBytes("hour"),Bytes.toBytes(keyString[1]));
				p.addColumn(Bytes.toBytes("key"),Bytes.toBytes("radar"),Bytes.toBytes(keyString[2]));
				
				//type
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("bus"),Bytes.toBytes(values.getCountBUS()));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("moto"),Bytes.toBytes(values.getCountMOTO()));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("velo"),Bytes.toBytes(values.getCountVELO()));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("vl"),Bytes.toBytes(values.getCountVL()));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("pl"),Bytes.toBytes(values.getCountPL()));
				p.addColumn(Bytes.toBytes("type"),Bytes.toBytes("ut"),Bytes.toBytes(values.getCountUT()));
				
				//direction
				p.addColumn(Bytes.toBytes("direction"),Bytes.toBytes("exit"),Bytes.toBytes(values.getSensSortie()));
				p.addColumn(Bytes.toBytes("direction"),Bytes.toBytes("enter"),Bytes.toBytes(values.getSensEntree()));

				//measure
				p.addColumn(Bytes.toBytes("measure"),Bytes.toBytes("speed"),Bytes.toBytes(values.getSpeed()));


				//Inserting data
				this.table.put(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}


		public void close(){
			try {
				this.table.close();
				this.connect.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}