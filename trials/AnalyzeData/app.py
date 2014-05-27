from flask import Flask,send_file
app = Flask(__name__,instance_relative_config=True)
app.config.from_pyfile('config.py')


@app.route('/')
def hello_world():
	return send_file('static/index.html')


if __name__ == '__main__':
	app.run(debug=app.config["DEBUG"])

