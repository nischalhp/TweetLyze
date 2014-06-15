from flask import Flask,send_file,jsonify,make_response
from flask import request
from Pipeline import Pipeline
app = Flask(__name__,instance_relative_config=True)
app.config.from_pyfile('config.py')


@app.route('/')
def hello_world():
	return send_file('static/index.html')

@app.route('/locations',methods=['GET'])
def get_locations():
	pipeline_obj = Pipeline();
	locations_list = pipeline_obj.get_locations()
	json_dict = {}
	json_dict['data'] = locations_list	
	print jsonify(json_dict)
	return jsonify(json_dict)

@app.route('/trends',methods=['GET'])
def get_trends():
	location_id = request.args.get('locationid')
	min_date = request.args.get('min_date')
	max_date = request.args.get('max_date')
	pipeline_obj = Pipeline()
	trends_list = pipeline_obj.get_trends(location_id,min_date,max_date)
	json_dict = {}
	json_dict['data'] = trends_list
	return jsonify(json_dict)

@app.route('/dates/<location_id>',methods=['GET'])
def get_dates(location_id):
	pipeline_obj = Pipeline()
	dates_list = pipeline_obj.get_dates_location(location_id)
	json_dict = {}
	json_dict['data'] = dates_list 
	print json_dict
	return jsonify(json_dict)

@app.route('/tfidf',methods=['GET'])
def get_tfidf():
	location_id = request.args.get('locationid')
	trend = request.args.get('trend')
	print location_id,trend
	pipeline_obj = Pipeline()
	tfidf_list = pipeline_obj.get_tfidf(location_id,trend)
	json_dict = {}
	print tfidf_list
	json_dict['data'] = tfidf_list 
	print json_dict
	return jsonify(json_dict)


if __name__ == '__main__':
	app.run(debug=app.config["DEBUG"])

