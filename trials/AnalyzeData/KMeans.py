from PostgresConnector import PostgresConnector
import traceback
from sklearn.feature_extraction.text import TfidfVectorizer 
class KMeans:

	def get_data(self,locationid):
		entity_trend_dict = {} 
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
				id = row[trend_column]
				if id in entity_id_dict.keys():
					entity_list = entity_id_dict[id]
					entity_list.append(row[entity_column])
					entity_id_dict[id] = entity_list
				else:
					entity_id_dict[id] = [row[entity_column]]


		except Exception:
			print traceback.format_exc()

		return entity_trend_dict


	def build_kMeans(self,locationid):
		entity_trend_dict = self.get_data(locationid)
		vectorize = TfidfVectorizer()
		x = vectorize.fit_transform(entity_trend_dict)
		print x


