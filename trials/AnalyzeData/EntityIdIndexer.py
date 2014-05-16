#entity id Indexer
from PostgresConnector import PostgresConnector
class EntityIdIndexer:

	def build(self):
		conn = PostgresConnector().get_connection() 
		cursor = conn.cursor() 
		query = 'select id,hashtags from "organizedTweets" limit 10'
		cursor.execute(query)
		id_column = 0
		entities_column = 1
		entity_id_dict = {}
		for row in cursor:
			tweet_id = row[id_column]
			hashtag_array = row[entities_column]
			hashtag_list = [hashtag['text'] for hashtag in hashtag_array]
			for entity in hashtag_list:
				if entity in entity_id_dict.keys():
					id_list = entity_id_dict[entity]
					id_list.append(tweet_id)
					entity_id_dict[entity] = id_list
				else:
					id_list = []
					id_list.append(tweet_id)
					entity_id_dict[entity] = id_list

		print entity_id_dict

EntityIdIndexer().build()

