#connect to postgres db
import Configurator
import psycopg2

class PostgresConnector:


	# method to read data from config and 
	# connect to postgres server
	# and returns the connection object
	def __init__(self):
		Config = Configurator.Configurator()
		ConfiguredObject = Config.read_configuration()
		Section = "DB Section"
		parameter_dict = Config.get_parameters(ConfiguredObject,Section)
		self.host = parameter_dict['host']
		self.dbname = parameter_dict['dbname']
		self.user = parameter_dict['user']
		self.pwd = parameter_dict['password']


	def get_connection(self):
		conn = psycopg2.connect(dbname=self.dbname,host=self.host,password=self.pwd,user=self.user) 
		return conn




#Connector = PostgresConnector() 
#Connector.connect_to_postgres()



