#building the pipline
# to read trends based on their count
# then to get all their tweets
# then to extract the required columns
# then create the id,entity table
from PostgresConnector import PostgresConnector


class Pipeline:


	# returns the list of all the locations with their geoid
	def get_locations(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		query = 'SELECT id,city from location';
		cursor.execute(query)
		id_column = 0 
		city_column = 1
		locations_list = []
		for row in cursor:
			id_location = {}
			id_location["geoid"] = row[id_column]
			id_location["location"] = row[city_column]
			locations_list.append(id_location)

		return locations_list


	def get_trends(self,location_id):
		trends_list = []	
		try:
			conn = PostgresConnector().get_connection()
			cursor = conn.cursor()
			query = 'select trend,count from (select count(id) as "count",trend from "trends" where locationid = \''+location_id+'\' group by trend) as t1 order by count desc'
			cursor.execute(query)
			trend_Column = 0
			count_Column = 1
			for row in cursor:
				trend_count = {}
				trend_count["trend"] = row[trend_Column]
				trend_count["count"] = row[count_Column]
				trends_list.append(trend_count)

		except Exception as e:
			print e 

		return trends_list


#pipeLine = Pipeline()
#list = pipeLine.get_locations()
#print list

