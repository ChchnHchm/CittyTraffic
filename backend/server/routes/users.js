var express = require('express');
const hbase = require('hbase');
var assert = require('assert');
const krb5 =require('krb5')

var router = express.Router();
//import  { filterFunctions } from "../utilities/filter";
const filterFunctions = require("../utilities/filter");
const client = new hbase.Client({host: 'lsd-prod-namenode-0.lsd.novalocal', port: 8080,protocol: 'https',
krb5:{service_principal: 'HTTP/lsd-prod-namenode-0.lsd.novalocal',principal:"nalves@LSD.NOVALOCAL"}
});
/* GET users listing. */
router.get('/', async function(req, res, next) {
    res.status(200);
});

/*
 return all data for a specified  day
*/
router.get('/getDate', async function(req, res, next) {
  res.setHeader('Content-Type',"application/json");
  try {
    // date :  req.query.date
    client.table('nalves:CittyTraficHbase').schema(function(error, schema){
      console.info(schema)
      console.info(error)
      });
    res.status(200).json(); //rajouter fonction
  } catch (error) {
    console.error(error);
    await res.status(424).json({error});
  }
});

/*
 return all data for  a specified hour
*/
router.get('/getHours', async function(req, res, next) {
  res.setHeader('Content-Type',"application/json");
  try {
    // date :  req.query.date
    // hours :  req.query.hours
    res.status(200).json(); //rajouter fonction
  } catch (error) {
    console.error(error);
    await res.status(424).json({error});
  }
});

/*
return all data for a radar and specified hour
*/
router.get('/getRadarDate', async function(req, res, next) {
  res.setHeader('Content-Type',"application/json");
  try {
    // radar :  req.query.radar
    // date :  req.query.date
    res.status(200).json(); //rajouter fonction
  } catch (error) {
    console.error(error);
    await res.status(424).json({error});
  }
});

/*
return all data for specified hour
*/
router.get('/getRadarHours', async function(req, res, next) {
  res.setHeader('Content-Type',"application/json");
  try {
    // radar :  req.query.radar
    // date :  req.query.date
    // hours :  req.query.hours
    console.log(filterFunctions.filterHourAndRadar(client.table('nalves:CittyTrafficHbase'),"2022-10-12","8","P4"))
    res.status(200).json(); //rajouter fonction
  } catch (error) {
    console.error(error);
    await res.status(424).json({error});
  }
});

module.exports = router;

 function filterHourAndRadar (table,date,hour,radar){
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

function resultScan(error,result){
  if(error){
      throw error;
  }
  return result;
}