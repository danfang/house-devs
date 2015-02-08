var express = require('express');
var bodyParser = require('body-parser');
var winston = require('winston');
var request = require('request');
var util = require('./util.js');

// Logging
var logger = new (winston.Logger)({
        transports: [
            new (winston.transports.Console)(),
            new (winston.transports.File)({ filename: '/var/log/houses/api.log' })
        ]
});

util.setLogger(logger);

var PORT = 3000;
var debug = true;

var REJECT_REASONS = {
    'price': 'price_weight',
    'accessible': 'amentities_weight',
    'education': 'education_weight',
    'transportation': 'transportation_weight',
    'high': -10.0,
    'low': +10.0
};

var app = express();
var router = express.Router();

app.use(bodyParser.json());

// Write all requests to log
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
        res.status(400).json({'error': msg });
        return true;
    }
    return false;
};


///////////////////////////////
//// Client Calls
//////////////////////////////

/**
 * Creating and checking for user account creation
 */
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


/**
 * Get the list of all saved suggested properties
 */
router.get('/saved/:userId', function(req, res) {
   util.pgCall('getRecStatus', [req.params.userId, true], function(err, result) {
        if (handlePgCallError(err, res)) return;

        res.json({'saved': result.rows});
        return;
    });
});

/**
 * Get the list of recently rejected suggested properties
 */
router.get('/rejected/:userId', function(req, res) {
   util.pgCall('getRecStatus', [req.params.userId, false], function(err, result) {
        if (handlePgCallError(err, res)) return;

        res.json({'rejected': result.rows});
        return;
    });
});

/**
 * Get the entire user history (atypical client call)
 */
router.get('/history/:userId', function(req, res) {
   util.pgCall('getRecs', [req.params.userId], function(err, result) {
        if (handlePgCallError(err, res)) return;

        res.json({'history': result.rows});
        return;
    });
});

/**
 * Retrieving a new recommendation from the algorithm engine, 
 * adding it to the database, and returning it to the client
 */
router.get('/rec/:userId', function(req, res) {
    getRec(req.params.userId, res);
});

/**
 * Updating user recommendations, marking it as 'saved' or 'unsaved',
 * then refiltering based on reject reason.
 */
router.put('/rec/:recId', function(req, res) {
    data = req.body;
    if (data.hasOwnProperty("save")) {
        accept = data['save'];
        if (accept) {

            // Mark this recommendation as saved
            util.pgCall('saveRec', [req.params.recId], function(err, result) {
                if (handlePgCallError(err, res)) return;
                res.json({'success': true});
            });

        } else if (data.hasOwnProperty('reject_reason')) {
            updateRec(req.params.recId, res, data['reject_reason']);
        } else {
            return res.status(400).json({'error': 'missing reject reason'});
        }
    }
});

/**
 * Retrieves user data, then updates it according to new weighting scheme
 */
var updateRec = function(recId, res, reason) {

    util.pgCall('getUserFromRec', [recId], function(err, result) {
        if (handlePgCallError(err, res)) return;

        var userData = result.rows[0];
        var tokens = reason.split('_');
        var attr = tokens[0];
        var direction = tokens[1];

        // Adjust weighting
        userData[REJECT_REASONS[attr]] += REJECT_REASONS[direction];
        userFields = ['price_weight', 'amenities_weight', 'education_weight', 'transportation_weight'];
        userValues = [];

        for (index in userFields) {
            field = reqFields[index];
            values.push(userData[field]);
        }

        userValues.push(userData.user_id);

        // If no errors, update the new user data
        util.pgCall('updateUser', userValues, function(err, result) {
            if (handlePgCallError(err, res)) return;
            res.json({'success': true});
        });

    });
};


/**
 * Gets a recommendation from the algorithm engine
 */
var getRec = function(userId, res) {
    logger.info('getting recommendation from algo');
    util.pgCall('getUserData', [userId], function(err, result) {
        if (handlePgCallError(err, res)) return;

        if (result.rowCount !== 1) {
            logger.error('Failed to retrieve user data');
            res.status(500).json({'error': 'user does not exist'});
            return;
        }

        data = result.rows[0];
        data['user_id'] = userId;

        options = {
            url: 'http://localhost:5000/getrec',
            json: true,
            body: data
        } 

        request(options, function(error, response, body) {
            if (!error && response.statusCode == 200) {
                var info = body;

                //var infoFields = ['', '', ''];
                //var infoValues = [];

                //for (index in infoFields) {
                //    var field = infoFields[index];
                //    infoValues.push(field);
               // }

               // util.pgCall('insertRec', [infoValues], function(err, result) {
                 //   if (handlePgCallError(err, res)) return;

                   // info['rec_id'] = result.rows[0].rec_id;

                    res.json(info);
               // });

            } else {
                res.status(404).json({'error': 'Could not retrieve recommendation'});
            }
        });
    });
};


////////////////////////////////
//// Algorithm HTTP Calls
///////////////////////////////

router.post('/rec', function(req, res) {

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
