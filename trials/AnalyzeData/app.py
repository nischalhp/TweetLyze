from flask import Flask,send_file,jsonify,make_response
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

@app.route('/trends/<location_id>',methods=['GET'])
def get_trends(location_id):
	print type(location_id)
	pipeline_obj = Pipeline()
	trends_list = pipeline_obj.get_trends(location_id)
	json_dict = {}
	json_dict['data'] = trends_list
	print jsonify(json_dict)
	return jsonify(json_dict)


if __name__ == '__main__':
	app.run(debug=app.config["DEBUG"])

