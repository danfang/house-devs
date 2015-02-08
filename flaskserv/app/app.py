import os
from recommend import *
from flask import Flask, request, Response, jsonify
app = Flask(__name__)
import json

algores = {"CraigID":{"0":4745056806},"area":{"0":" (Greenwood, Seattle)"},"baths":{"0":3.0},"beds":{"0":3},"buyerseller":{"0":1.785714286},"coord":{"0":"47.714266, -122.355809"},"link":{"0":"\/see\/apa\/4745056806.html"},"longitude":{"0":-122.355809},"price":{"0":3499},"size":{"0":2500},"title":{"0":"3 bed, 3 bath, 2 parking fully furnished Seattle townhome"},"zipcode":{"0":98133}}

@app.route("/")
def hello():
        return "Hello from Python!"

@app.route('/test', methods = ['GET', 'POST'])
def test():
	print algores
	resp = jsonify(algores)
	resp.status_code = 200
	return resp

@app.route('/getrec', methods = ['GET','POST'])
def getRecommendations():
	try:
		if request is not None:
			print 'request received', request
			jsonDat = request.json
			print jsonDat
			algores1 = get_rec(jsonDat)
			print 'Response From Algo \n', algores1
			responseString = json.dumps(algores1)
			print responseString
			resp = Response(responseString, status=200,  mimetype="application/json")
			return resp
	except:
		resp = Response('No Results Found', status=404)
		return resp

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
