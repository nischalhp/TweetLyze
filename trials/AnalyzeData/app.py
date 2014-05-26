from flask import Flask
app = Flask(__name__,instance_relative_config=True)
app.config.from_pyfile('config.py')


@app.route('/')
def hello_world():
	return 'Hello World!'


if __name__ == '__main__':
	app.run(app.config["DEBUG"])

