import os
from recommend import *
from flask import Flask, request, Response, jsonify
app = Flask(__name__)

@app.route("/")
def hello():
        return "Hello from Python!"

@app.route('/getrec', methods = ['GET','POST'])
def getRecommendations():
	try:
		if request is not None:
			print 'request received', request
			jsonDat = request.json
			print jsonDat
			jsonDat1 = get_rec(jsonDat)
			print '\n', jsonDat1
			resp = jsonify(jsonDat1)
			resp.status_code = 200
			return resp
	except:
		print 'Invalid Request, Try Again'

@app.route('/updaterec', methods = ['POST','GET'])
def updateRecommendations():
	try:
		if request is not None:
			print 'updaterec', request
			jsonDat = request.json
			print 'Json Got', jsonDat
			resp = jsonify(jsonDat)
			resp.status_code = 200
			return resp
	except:
		return 'Invalid Request, Try Again'

if __name__ == "__main__":
	port = int(os.environ.get("PORT", 5000))
        app.run(host='0.0.0.0', port=port, debug=True, use_reloader=False)
