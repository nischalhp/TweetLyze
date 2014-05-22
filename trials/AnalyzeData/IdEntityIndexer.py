import json
import StringIO
import os
import sys
from PostgresConnector import PostgresConnector

class IdEntityIndexer:

	def build(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		conn.autocommit = True
		query = 'select id,hashtags from "organizedTweets" '
		cursor.execute(query)
		id_column = 0
		entities_column = 1
		io = open('copy_from.txt','wr')
		for row in cursor:
			tweet_id = row[id_column]
			hashtag_array = row[entities_column]
			hashtag_list = [hashtag['text'] for hashtag in hashtag_array]
			hashtag_list_unique = list(set(hashtag_list))
			for hashtag in hashtag_list_unique: 
				io.write(tweet_id+'\t'+hashtag.encode('utf-8')+'\n')

		io.close()
		io = open('copy_from.txt','r')
		cursor.copy_from(io,'"IdEntity"',columns=('id','entity'))	
		io.close()
		os.remove('copy_from.txt')

IdEntityIndexer().build()

