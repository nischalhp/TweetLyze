#building the pipline
# to read trends based on their count
# then to get all their tweets
# then to extract the required columns
# then create the id,entity table
from PostgresConnector import PostgresConnector


class Pipeline:

	def readTrends(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		query = 'select trend from (select count(id),trend from "trends" group by trend) as t1 order by count desc'
		cursor.execute(query)
		trend_Column = 0
		# this fetches all the ids for the trend
		for row in cursor:
			trend = row[trend_Column]
			queryJoin = 'select id from "trends" where trend = \'' + trend +'\''
			cursor.execute(queryId)

			#this fetches all the tweets by doing a join

