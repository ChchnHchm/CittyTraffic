const filterFunctions =  {

 filterDateAndHour: function (table,date,hour){
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

},

 filterDate: function(table,date){
  console.log(stringFilter);

    var stringFilter=date+".*";
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
      }).on('end', rows=> console.log(rows))

},

 filterDateAndRadar :function(table,date,radar){
    var filterBigin=date+".*";
    var filterEnd=".+"+radar;
    return table.scan({
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
      }).get(resultScan);

},

 filterHourAndRadar:function(table,date,hour,radar){
    var stringFilter=date+","+hour+","+radar;
    console.log(stringFilter);
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
}
function resultScan(error,result){
    if(error){
        throw error;
    }
    return result;
}

module.exports = filterFunctions;