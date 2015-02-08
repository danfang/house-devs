import os
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
			print 'Json Got', jsonDat
			resp = jsonify(jsonDat)
			resp.status_code = 200
			return resp
	except:
		print 'Nothing posted'

@app.route('/updaterec', methods = ['POST','GET'])
def updateRecommendations():
	jsonData = request.json()
	print jsonData
	return 'UpdateReco'

if __name__ == "__main__":
        port = int(os.environ.get("PORT", 5000))
	app.debug = True
        app.run(host='0.0.0.0', port=port)
