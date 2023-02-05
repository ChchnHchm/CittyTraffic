var express = require('express');
const hbase = require('hbase');
const krb5 =require('krb5')
const username='nalves'
var router = express.Router();
//import  { filterFunctions } from "../utilities/filter";
const filterFunctions = require("../utilities/filter");
const client = new hbase.Client({host: 'lsd-prod-namenode-0.lsd.novalocal', port: 8080,protocol: 'https',
krb5:{service_principal: 'HTTP/lsd-prod-namenode-0.lsd.novalocal',principal: username+"@LSD.NOVALOCAL"}
});
const table= client.table(username+':CittyTrafficHbase');
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
    },(error, cells) => {
        assert.ifError(error);
        console.log(cells)
        res.status(200).json(cells);
      })

    
    // res.status(200).json(); //rajouter fonction
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
    console.log(filterFunctions.filterDateAndHour(client.table(username+':CittyTrafficHbase'),req.query.date,req.query.hours));

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
    console.log(filterFunctions.filterDateAndRadar(client.table(username+':CittyTrafficHbase'),req.query.date,req.query.radar));
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
    console.log(filterFunctions.filterHourAndRadar(client.table(username+':CittyTrafficHbase'),req.query.date,req.query.hours,req.query.radar));

    res.status(200).json(); //rajouter fonction
  } catch (error) {
    console.error(error);
    await res.status(424).json({error});
  }
});

module.exports = router;

 

function resultScan(error,result){
  if(error){
      throw error;
  }
  return result;
}