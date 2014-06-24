from textblob import TextBlob
from PostgresConnector import PostgresConnector
from textblob.sentiments import NaiveBayesAnalyzer

class SentimentAnalyzer:

	def get_sentiments(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		query = """
		select id,text from organized_tweets 
		"""
		cursor.execute(query)
		id_column = 0
		text_column = 1
		for row in cursor:
			cursor_insert = conn.cursor()
			text = row[text_column]
			blob = TextBlob(text,analyzer=NaiveBayesAnalyzer())
			print "inserting sentiments for the text %s" % text
			insert_query = """
				update organized_tweets
				set sentiment = %s,
				pos_sentiment = %s,
				neg_sentiment = %s
				where id = %s

			"""
			cursor_insert.execute(insert_query,(blob.sentiment.classification,
				blob.sentiment.p_pos,blob.sentiment.p_neg,row[id_column]))
			conn.commit()	