from PostgresConnector import PostgresConnector
import traceback
class KMeans:

	def get_data(self,locationid):
		entity_trend = []
		try:
			conn = PostgresConnector().get_connection()
			cursor = conn.cursor()
			query = """select t1.entity,t2.trend from
						(select id,entity from id_entity ) as t1
						inner join
						(select id,trend from organized_tweets where trend in 
							(select trend from 
							(select count(*) as c,trend from 
								trends where locationid = %s group by trend)as t_in order 
							by c desc limit 15))as t2
						on
						t1.id = t2.id"""
			cursor.execute(query,(locationid,))
			entity_column = 0
			trend_column = 1
			for row in cursor:
				entity_trend_tuple = (row[entity_column],row[trend_column])
				entity_trend.append(entity_trend_tuple)
		except Exception:
			print traceback.format_exc()

		return entity_trend		


	def build_kMeans(self,locationid):
		entity_trend = self.get_data(locationid)
		print entity_trend	


