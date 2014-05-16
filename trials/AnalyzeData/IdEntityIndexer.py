import json
from PostgresConnector import PostgresConnector

class IdEntityIndexer:

	def build(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		query = 'select id,hashtags from "organizedTweets" '
		cursor.execute(query)
		id_column = 0
		entities_column = 1
		id_entity_dict = {}
		for row in cursor:
			tweet_id = row[id_column]
			hashtag_array = row[entities_column]
			hashtag_list = [hashtag['text'] for hashtag in hashtag_array]
			id_entity_dict[tweet_id] = hashtag_list 

		print len(id_entity_dict)


obj = IdEntityIndexer().build()
