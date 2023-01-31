package bigdata;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.lang.model.element.Element;

import org.apache.hadoop.fs.FSDataInputStream;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CittyTrafficHbase extends Configured {
        private static final String[]  FAMILYS = {"key","type","direction","measure"}; 
		private static final byte[] TABLE_NAME = Bytes.toBytes("hchouchane:CittyTrafficHbase");
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

		public int addRow(String familly, String colName, String value){
			try {
				//Instantiating Put
				Put p = new Put(Bytes.toBytes("row"));

				//Adding value
				p.addColumn(Bytes.toBytes(familly),Bytes.toBytes(colName),Bytes.toBytes(value));

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