var express = require('express');
var router = express.Router();
import * as filter from "../utilities/filter.js" 
const client = new hbase.Client({host: 'nalves@147.210.117.54', port: 3000})

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});


module.exports = router;
