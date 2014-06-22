from textblob import TextBlob
from PostgresConnector import PostgresConnector
from textblob.sentiments import NaiveBayesAnalyzer

class SentimentAnalyzer:

	def get_sentiments(self):
		conn = PostgresConnector().get_connection()
		cursor = conn.cursor()
		query = """
		select text from organized_tweets limit 10
		"""
		cursor.execute(query)
		for row in cursor:
			text = row[0]
			blob = TextBlob(text,analyzer=NaiveBayesAnalyzer())
			print blob.sentiment
