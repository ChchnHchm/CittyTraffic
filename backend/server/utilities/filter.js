var filters = require('hbase-client').filters;
const client = new hbase.Client({})

function filterDateAndHour(table,date,hour){
    var stringFilter=date+","+hour+".*";
    table.scan({
        filter: {
            "op":"MUST_PASS_ALL","type":"FilterList","filters":[{
                "op":"EQUAL",
                "type":"RowFilter",
                "comparator":{"value":stringFilter,"type":"RegexStringComparator"}
              }
            ]
        }
    }, (error, cells) => {
        assert.ifError(error)
      })

}

function filterDate(table,date){
    var stringFilter=date+".*";
    table.scan({
        filter: {
            "op":"MUST_PASS_ALL","type":"FilterList","filters":[{
                "op":"EQUAL",
                "type":"RowFilter",
                "comparator":{"value":stringFilter,"type":"RegexStringComparator"}
              }
            ]
        }
    }, (error, cells) => {
        assert.ifError(error)
      })

}

function filterDateAndRadar(table,date,radar){
    var filterBigin=date+".*";
    var filterEnd=".+"+radar;
    table.scan({
        filter: {
            "op":"MUST_PASS_ALL","type":"FilterList","filters":[{
                "op":"EQUAL",
                "type":"RowFilter",
                "comparator":{"value":filterBigin,"type":"RegexStringComparator"}
              },{
                "op":"EQUAL",
                "type":"RowFilter",
                "comparator":{"value":filterEnd,"type":"RegexStringComparator"}
              }
            ]
        }
    }, (error, cells) => {
        assert.ifError(error)
      })

}

function filterHourAndRadar(table,date,hour,radar){
    var stringFilter=date+","+hour+","+radar;
    return table.scan({
        filter: {
            "op":"MUST_PASS_ALL","type":"FilterList","filters":[{
                "op":"EQUAL",
                "type":"RowFilter",
                "comparator":{"value":stringFilter,"type":"RegexStringComparator"}
              }
            ]
        }
    }, (error, cells) => {
        assert.ifError(error)
      }).get(resultScan)
}

function resultScan(error,result){
    if(error){
        throw error;
    }
    return result;
}