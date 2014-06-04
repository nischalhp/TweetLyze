import json
import StringIO
import os
import sys
from PostgresConnector import PostgresConnector
import json
class IdEntityIndexer:

    def build(self):
        conn = PostgresConnector().get_connection()
        cursor = conn.cursor()
        query = 'select id,entities from organized_tweets '
        cursor.execute(query)
        id_column = 0
        entities_column = 1

        with open('copy_from.txt','w') as f:
            for row in cursor:
                tweet_id = row[id_column]
                hashtag_array = row[entities_column]
                json_array = json.loads(hashtag_array)
                hashtag_list = [hashtag["text"] for hashtag in json_array]
                hashtag_list_unique = list(set(hashtag_list))
                print 'Writing data to table for the tweet_id ' +tweet_id 
                for hashtag in hashtag_list_unique:
                    f.write(tweet_id + '\t' + hashtag.encode('utf-8') + '\n')

        with open('copy_from.txt') as f:
            cursor.copy_from(f, 'id_entity', columns=('id', 'entity'))
            conn.commit()

        os.remove('copy_from.txt')


