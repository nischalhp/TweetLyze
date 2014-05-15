import json
import PostgresConnector

class EntityIndexer:

	def build_entity_indexer(self):
		conn = PostgresConnector.PostgresConnector().connect_to_postgres()
		cursor = conn.cursor()
		query = 'select id,hashtags from "organizedTweets" limit 1'
		cursor.execute(query)
		id_column = 0
		entities_column = 1

		for row in cursor:
			print row[id_column],row[entities_column]


obj = EntityIndexer().build_entity_indexer()
