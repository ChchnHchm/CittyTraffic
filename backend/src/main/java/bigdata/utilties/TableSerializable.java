package bigdata.utilties;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;

public class TableSerializable  implements Serializable,Table {

    private TableName name;
    private Configuration conf;
    private TableDescriptor desc;
    private RegionLocator regLoc;

    public TableSerializable(Table sup){
        name=sup.getName();
        conf=sup.getConfiguration();
        try {
            desc=sup.getDescriptor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public TableName getName() {
        return name;
    }

    @Override
    public Configuration getConfiguration() {
        return conf;
    }

    @Override
    public TableDescriptor getDescriptor() throws IOException {
        return desc;
    }

    @Override
    public RegionLocator getRegionLocator() throws IOException {
        return null;
    }
   
}
