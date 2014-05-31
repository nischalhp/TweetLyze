#building the pipline
# to read trends based on their count
# then to get all their tweets
# then to extract the required columns
# then create the id,entity table
from PostgresConnector import PostgresConnector

class Pipeline:


	# returns the list of all the locations with their geoid
	def get_locations(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		query = 'SELECT id,city from location';
		cursor.execute(query)
		id_column = 0 
		city_column = 1
		locations_list = []
		for row in cursor:
			id_location = {}
			id_location["geoid"] = row[id_column]
			id_location["location"] = row[city_column]
			locations_list.append(id_location)

		return locations_list


	def get_trends(self,location_id):
		trends_list = []	
		try:
			conn = PostgresConnector().get_connection()
			cursor = conn.cursor()
			query = 'select trend,count from (select count(id) as "count",trend from "trends" where locationid = \''+location_id+'\' group by trend) as t1 order by count desc'
			cursor.execute(query)
			trend_column = 0
			count_column = 1
			for row in cursor:
				trend_count = {}
				trend_count["trend"] = row[trend_column]
				trend_count["count"] = row[count_column]
				trends_list.append(trend_count)

		except Exception as e:
			print e 

		return trends_list

	def update_organized_tweets(self):
		tweet_id_dict = {} 


		try:
			conn = PostgresConnector().get_connection()
			cursor = conn.cursor()
			query = 'select id from "trends"'
			cursor.execute(query)
			trend_id_column = 0
			for row in cursor:
				trend_id = row[trend_id_column]
				query_tweets = 'select tweets from tweets where trendId = \''+trend_id+'\''
				cursor_tweets = conn.cursor()
				cursor_tweets.execute(query_tweets)
				tweets_column = 0
				for tweets_row in cursor_tweets:
					tweets_json_array = tweets_row[tweets_column]
					for json in tweets_json_array:
						id = json['id']
						tweet_id_exits = tweet_id_dict[id]
						if tweet_id_exits is None:
							tweet_id_dict[id] = 1
							geo = json['geo']
							retweeted = json['retweeted']
							in_reply_to_screen_name = json['in_reply_to_screen_name']
							truncated = json['truncated']
							source = json['source']
							created_at = json['created_at']
							place = json['place']
							user_id = json['user'][0]['id']
							text = json['text']
							entities = json['entities'][0]['hashtags']
							user_mentions = json['entities'][0]['user_mentions']
							retweet_count = json['retweet_count']
							favorite_count = json['favorite_count']

							print id,geo,retweeted,in_reply_to_screen_name,truncated,source,created_at,place,user_id,text,entities,user_mentions,retweet_count,favorite_count,trend_id

						    # rest of the process to get all the fields and write to a file

						else:
							continue
						break
						# array of tweets json ends here

					# total number of tweets rows for a given trend ends here
					break

				# all trends finish here
				break
				






		except Exception as e:
			print e 

	    		



pipeLine = Pipeline()
pipeLine.update_organized_tweets()
#list = pipeLine.get_locations()


#print list

