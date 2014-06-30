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
		with open("sentiments.tsv","w") as f:
			for row in cursor:
				text = row[text_column]
				blob = TextBlob(text,analyzer=NaiveBayesAnalyzer())
				print 'writing for tweet with id ' +str(row[id_column])

				f.write(str(row[id_column])+'\t'+str(blob.sentiment.classification)+'\t'+str(blob.sentiment.p_pos)+'\t'+str(blob.sentiment.p_neg)+'\n')

		# with open('sentiments.txt') as f:
		# 	cursor.write = conn.cursor()
		# 	cursor.write.copy_from(f,'sentiments',columns=('id','sentiment','pos_sentiment','neg_sentiment'))

		# conn.commit()
		# os.remove('sentiments.txt')
