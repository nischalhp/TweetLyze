#connect to postgres db
import Configuration
import psycopg2
class PostgresConnector:


	# method to read data from config and 
	# connect to postgres server
	# and returns the connection object
	def connect_to_postgres(self):
		Config = Configuration.configurator()
		ConfiguredObject = Config.read_configuration()
		Section = "DB Section"
		parameter_dict = Config.get_parameters(ConfiguredObject,Section)
		host = parameter_dict['host']
		dbname = parameter_dict['dbname']
		user = parameter_dict['user']
		pwd = parameter_dict['password']
		conn = psycopg2.connect(dbname=dbname,host=host,password=pwd,user=user) 
		return conn




#Connector = PostgresConnector() 
#Connector.connect_to_postgres()



