#building the pipline
# to read trends based on their count
# then to get all their tweets
# then to extract the required columns
# then create the id,entity table
from PostgresConnector import PostgresConnector
import traceback
import os
import re
import string
import json

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
			id_location["city"] = row[city_column]
			locations_list.append(id_location)

		return locations_list


	def get_trends(self,location_id):
		trends_list = []	
		try:
			conn = PostgresConnector().get_connection()
			cursor = conn.cursor()
			query = """
			select c,trend from
			(select count(*) as c,trend from trends where 
				locationid = %s group by trend)as t1 order by c desc limit 15
			"""
			cursor.execute(query,(location_id,))
			trend_column = 1
			count_column = 0
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
			query_location = 'select id from location'	
			cursor.execute(query_location)
			location_column = 0

			for row_location in cursor:

				query = 'select id,trend from (select count(*) as c,trend,id from trends where locationid = %s group by trend,id)as t1 order by c desc limit 15'
				cursor = conn.cursor()
				location_id = row_location[location_column]
				cursor.execute(query,(location_id,))
				trend_id_column = 0
				trend_name_column = 1
				trend_count = 0

				for row in cursor:
					trend_count = trend_count + 1
					trend_id = row[trend_id_column]
					trend_name = row[trend_name_column]
					print 'Processing for trend ' +trend_id+' , ' +str(trend_count)
					query_tweets = 'select tweets from tweets where trendId = \''+str(trend_id)+'\''
					cursor_tweets = conn.cursor()
					cursor_tweets.execute(query_tweets)
					tweets_column = 0

					with open(trend_name+'.txt','w') as f:

						# rows of tweets array
						for tweets_row in cursor_tweets:
							tweets_json_array = tweets_row[tweets_column]

							# tweets in a tweets array
							for json_in in tweets_json_array:

								id = json_in['id']
								tweet_id_exists = tweet_id_dict.get(id)

								if tweet_id_exists is None:
									#print jsonIn
									tweet_id_dict[id] = 1
									geo =  'none' if json_in['geo'] is None else 'none' #json['geo']
									retweeted = json_in['retweeted']
									in_reply_to_screen_name = 'none' if json_in['in_reply_to_screen_name'] is None else json_in['in_reply_to_screen_name']
									truncated = 'none' if json_in['truncated'] is None else json_in['truncated']
									source = json_in['source']
									created_at = json_in['created_at']
									place = 'none' if json_in['place'] is None else 'none'#json['place']
									user_id = json_in['user']['id']
									text = json_in['text'].strip()
									#text = " ".join(str(text).split())
									text = str(filter(lambda x: x in string.printable,text))
									#text = text.encode('utf-16')
									text = re.sub('\s+',' ',text)
									text = text.replace('\\','')
									entities = json_in['entities']['hashtags']
									user_mentions = json_in['entities']['user_mentions']
									user_mentions = [] 
									retweet_count = json_in['retweet_count']
									favorite_count = json_in['favorite_count']
									entities_json_list = []

									if len(entities) > 0:
										for entity in entities:
											for k,v in entity.items():
												if k in 'text':
													entity_list = {}
													new_v = entity[k]
													new_v = str(new_v.encode('utf-8'))
													new_v = filter(lambda x: x in string.printable,new_v)
													#print id,check,new_v,len(new_v)
													if len(new_v) > 0: 
														entity[k] = new_v
													else:
														entity[k] = ''



									#print id,geo,retweeted ,in_reply_to_screen_name ,truncated ,source ,created_at ,place ,user_id ,text ,entities ,user_mentions,retweet_count,favorite_count
									f.write(str(id)+'\t'+str(geo)+'\t'+str(retweeted)+'\t'+str(in_reply_to_screen_name.encode('utf-8'))+'\t'+str(truncated)+'\t'+str(source.encode('utf-8'))+'\t'+str(created_at.encode('utf-8'))+'\t'+str(place)+'\t'+str(user_id)+'\t'+text+'\t'+str(json.dumps(entities))+'\t'+str(user_mentions)+'\t'+str(retweet_count)+'\t'+str(favorite_count)+'\t'+str(trend_name)+'\t'+str(location_id)+'\n')

								else:
									continue

								# array of tweets json ends here
								#break

							# total number of tweets rows for a given trend ends here
							#break

					print 'Writing to table'

					with open(trend_name+'.txt') as f:
						cursor_write = conn.cursor()
						cursor_write.copy_from(f,'organized_tweets',columns=('id','geo','retweeted','in_reply_to_screen_name','truncated','source','created_at','place','user_id','text','entities','user_mentions','retweet_count','favorite_count','trend','location_id'))

					conn.commit()
					os.remove(trend_name+'.txt')

					# all trends finish here
					#break



		except Exception :
			print traceback.format_exc()

	    		



#pipeLine = Pipeline()
#pipeLine.update_organized_tweets()
#list = pipeLine.get_locations()
# conn = PostgresConnector().get_connection()
# cursor = conn.cursor()
# with open('organized_data.txt') as f:
# 	cursor.copy_from(f,'organized_tweets',columns=('id','geo','retweeted','in_reply_to_screen_name','truncated','source','created_at','place','user_id','text','entities','user_mentions','retweet_count','favorite_count','trend_id'))
# conn.commit()

#print list

