#entity id Indexer
from PostgresConnector import PostgresConnector
from TfIdf import TfIdf
class EntityIdIndexer:

	def build(self):
		conn = PostgresConnector().get_connection() 
		cursor = conn.cursor() 
		query = 'select id,hashtags from "organizedTweets" '
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
		return entity_id_dict			

	def build_tf(self):
		# using group by first get tf score for each entity
		conn = PostgresConnector().get_connection() 
		cursor = conn.cursor() 
		query = 'select count(id),entity from "IdEntity" group by entity'
		cursor.execute(query)
		count_of_id_column = 0
		entities_column = 1
		entity_id_dict = {}
		for row in cursor:
			count_of_id = row[count_of_id_column]
			entity = row[entities_column]
			entity_id_dict[entity] = count_of_id

		return entity_id_dict			

	def get_total_documents(self):
		conn = PostgresConnector().get_connection() 
		cursor = conn.cursor() 
		query = 'select count(distinct(id)) from "IdEntity" '
		cursor.execute(query)
		count_of_distinct_id_column = 0
		total_documents_count = 0
		for row in cursor:
			total_documents_count = row[count_of_distinct_id_column]

		return total_documents_count





###############
EntityIdIndexObj = EntityIdIndexer()
entity_count_id_dict = EntityIdIndexObj.build_tf()
total_documents_count = EntityIdIndexObj.get_total_documents() 
TfIdfObj = TfIdf()
entity_tfidf_obj = TfIdfObj.computeTfIdf(entity_count_id_dict,total_documents_count)
TfIdfObj.write_to_db(entity_tfidf_obj)
