package bigdata.utilties;

import java.io.Serializable;

import org.apache.hadoop.hbase.client.Put;


public class PutSerializable extends Put implements Serializable {

    public PutSerializable(byte[] row) {
        super(row);
    }
    
}
