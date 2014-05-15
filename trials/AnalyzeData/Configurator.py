import os
import ConfigParser

class Configurator:

	# read configuration and returns a config object
	def read_configuration(self):
		Config = ConfigParser.ConfigParser() 
		workingDirectory = os.getcwd()
		fileName = os.path.join(workingDirectory,"properties/properties.ini")
		Config.read(fileName)
		return Config


	# returns a dictionary of parameters that belong to that section
	def get_parameters(self,configuredObject,section):
		options = configuredObject.options(section)
		parameters_dict = {}
		for option in options:
			try:
				parameters_dict[option] = configuredObject.get(section,option)
				if parameters_dict[option] == -1:
					print ("skip: %s" % option)
			except:
				print("exception on %s"  % option)
		return parameters_dict
