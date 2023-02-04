var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

/*
 return all data for a specified  day
*/
router.get('/getDate', function(req, res, next) {
  res.send('respond with a resource');
});

/*
 return all data for  a specified hour
*/
router.get('/getHours', function(req, res, next) {
  res.send('respond with a resource');
});

/*
return all data for a radar and specified hour
*/
router.get('/getRadarDate', function(req, res, next) {
  res.send('respond with a resource');
});

/*
return all data for specified hour
*/
router.get('/getRadarHours', function(req, res, next) {
  res.send('respond with a resource');
});

module.exports = router;
