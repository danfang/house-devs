var express = require('express');
var bodyParser = require('body-parser');
var winston = require('winston');

var logger = new (winston.Logger)({
        transports: [
            new (winston.transports.Console)(),
            new (winston.transports.File)({ filename: '/var/log/houses/api.log' })
        ]
});

var PORT = 3000;
var debug = true;

var app = express();
var router = express.Router();

app.use(bodyParser.json());

router.all('*', function(req, res, next) {
    logger.info(req.method + ' ' + req.hostname + req.path +
                    ' with: ' + JSON.stringify(req.body));
    next();
});

///////////////////////////////
//// Client Calls
//////////////////////////////

router.route('/user/:userId')
.get(function(req, res, next) {

})
.post(function(req, res, next) {

});

router.get('/saved/:userId', function(req, res) {

});

router.get('/rejected/:userId', function(req, res) {

});

router.get('/history/:userId', function(req, res) {

});

router.get('/rec/:userId', function(req, res) {

});

router.put('/rec/:recId', function(req, res) {

});

////////////////////////////////
//// Algorithm Calls
///////////////////////////////

router.post('/rec/', function(req, res) {

});

router.get('/history', function(req, res) {

});

////////////////////////////
//// Running the server 
////////////////////////////

app.use('/api', router);

app.listen(PORT, function() {
        logger.info('API server is listening on port ' + PORT);
        logger.info('Debug mode is ' + (debug ? 'ON': 'OFF'));
});
