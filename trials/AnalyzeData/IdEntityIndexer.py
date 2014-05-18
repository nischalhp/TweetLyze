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
			IdEntityIndexer().insert_to_db(tweet_id,hashtag_list)
		print len(id_entity_dict)

	# insert id , and hashtags into the table IdEntity
	def insert_to_db(self,id,hashtag_list):
		conn = PostgresConnector().get_connection()
		conn.autocommit = True
		cursor = conn.cursor()
		for hashtag in hashtag_list:
			query = "INSERT INTO \"IdEntity\" values(\'"+id+"\',\'"+hashtag+"\')"
			try:
				print "Executing query " + query
				cursor.execute(query)
			except:
				print "Something went wrong while inserting" 



obj = IdEntityIndexer().build()
