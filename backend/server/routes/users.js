var express = require('express');
const hbase = require('hbase')
var router = express.Router();
//import  { filterFunctions } from "../utilities/filter";
const filterFunctions = require("../utilities/filter");
const client = new hbase.Client({host: 'nalves@147.210.117.54', port: 3000});
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
    console.log(filter.filterHourAndRadar(client.getTable('nalves:CittyTrafficHbase'),"2022-10-12","8","P4"))

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
    console.log(filter.filterHourAndRadar(client.getTable('nalves:CittyTrafficHbase'),"2022-10-12","8","P4"))
    res.status(200).json(); //rajouter fonction
  } catch (error) {
    console.error(error);
    await res.status(424).json({error});
  }
});

module.exports = router;
