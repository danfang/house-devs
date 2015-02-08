var pg = require('pg');
var conString = "postgres://api:UFZxjow070ddTzHzVWYHtJYXvidf1uNl@localhost/housing";

var sql = {
    'getUser': 'SELECT 1 FROM users WHERE user_id=$1',
    'getUserData': 'SELECT * FROM users WHERE user_id=$1',
    'getUserFromRec': 'SELECT u.* FROM users AS u JOIN recs AS r ON r.user_id=u.user_id WHERE r.rec_id=$1',
    'createUser': 'INSERT INTO users VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)',
    'updateUser': 'UPDATE users SET price_weight=$1, amenities_weight=$2, education_weight=$3, transportation_weight=$4 WHERE user_id=$5',
    'insertRec': 'INSERT INTO recs(user_id, prop_type, prop_id) VALUES ($1, $2, $3) RETURNING rec_id',
    'rejectRec': 'UPDATE recs SET saved=false WHERE rec_id=$1',
    'getRecStatus': 'SELECT * from recs WHERE user_id=$1 AND save=$2',
    'getRecs': 'SELECT * from recs WHERE user_id=$1',
    'saveRec': 'UPDATE recs SET saved=true WHERE rec_id=$1'
}

var logger;

var setLogger = function(newLogger) { logger = newLogger; };

/**
 * Logging of database errors as well as closing the pgConnection
 *
 * @return true if an error was found, false otherwise
 */
var handleDBError = function(err, done) {
    if (!err) return false;
    done();
    logger.error('DBError: ' + err);
    return true;
};

/**
 * Connects to the db and runs the specified SQL query
 *
 * @param dbFunction: the name of the SQL query (specified in util.js)
 * @param params: an array of the necessary query parameters
 * @param callback: a function which will be called with either:
 *      (error, null), if either a query or internal error occurred
 *      (false, result), if the query succeeds, the result is passed on
 *
 * Guarantees the the db call will close by calling done()
 */
var pgCall = function(dbFunction, params, callback) {
    pg.connect(conString, function(err, client, done) {
        if (handleDBError(err, done)) {
            callback({'type': 'internal'}, null);
            return;
        }
        client.query(sql[dbFunction], params, function(err, result) {
            if (handleDBError(err, done)) {
                callback({'type': 'query'}, null);
                return;
            }
            callback(false, result);
            done();
        });
    });
};

exports.pgCall = pgCall;
exports.setLogger = setLogger;
