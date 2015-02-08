var express = require('express');
var bodyParser = require('body-parser');
var winston = require('winston');
var util = require('./util.js');

var logger = new (winston.Logger)({
        transports: [
            new (winston.transports.Console)(),
            new (winston.transports.File)({ filename: '/var/log/houses/api.log' })
        ]
});

util.setLogger(logger);

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

/**
 * Processes an error object returned by pgCall by sending HTTP responses
 * given the error type.
 *
 * @param queryErr: string to send instead of the generic query error.
 */
var handlePgCallError = function(err, res, queryErr) {
    if (err && err.type === 'internal') {
        res.status(500).json({'error': msg });
        return true;
    } else if (err) {
        var msg;
        msg = queryErr ? queryErr : 'There was an issue with your query.';
        res.json({'error': msg });
        return true;
    }
    return false;
};


///////////////////////////////
//// Client Calls
//////////////////////////////

router.route('/user/:userId')
.get(function(req, res, next) {
    util.pgCall('getUser', [req.params.userId], function(err, result) {
        if (handlePgCallError(err, res)) return;

        if (result.rowCount === 1) {
            res.status(200).json({'success': true});
            return;
        } 

        res.status(404).json({'success': false});
        return;
    });
})
.post(function(req, res, next) {
    data = req.body;

    reqFields = ['locale', 'first_home', 'category', 'beds', 'voucher', 'subsidy', 'income',
                 'price_weight', 'amenities_weight', 'education_weight', 'transportation_weight',
                 'prop_type', 'age_range']

    values = [];
    errors = [];

    values.push(req.params.userId);

    for (index in reqFields) {
        field = reqFields[index];

        if (!data.hasOwnProperty(field)) {
            errors.push(field + ' field missing');
        }

        values.push(data[field]);
    }

    if (errors.length > 0) {
        res.status(400).json({'errors': errors});
        return;
    }

    util.pgCall('createUser', values, function(err, result) {
        if (handlePgCallError(err, res)) return;
        res.json({'success': true});
    });

});

router.get('/saved/:userId', function(req, res) {
   util.pgCall('getRecStatus', [req.params.userId, true], function(err, result) {
        if (handlePgCallError(err, res)) return;

        res.json({'saved': result.rows});
        return;
    });
});

router.get('/rejected/:userId', function(req, res) {
   util.pgCall('getRecStatus', [req.params.userId, false], function(err, result) {
        if (handlePgCallError(err, res)) return;

        res.json({'rejected': result.rows});
        return;
    });
});

router.get('/history/:userId', function(req, res) {
   util.pgCall('getRecs', [req.params.userId], function(err, result) {
        if (handlePgCallError(err, res)) return;

        res.json({'rejected': result.rows});
        return;
    });
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
