import Pycluster
import traceback
from PostgresConnector import PostgresConnector

class KMedoid:


	def get_matrix(self,locationid):

		try:
			conn = PostgresConnector().get_connection()
			cursor = conn.cursor()
			query = """
			WITH 

			TREND_COUNT_TT AS
			(SELECT TREND,COUNT(*) AS TREND_COUNT 
				FROM TRENDS 
				WHERE LOCATIONID = %s GROUP BY TREND),


			TOP_TRENDS_TT AS
			(SELECT TREND FROM TREND_COUNT_TT ORDER BY TREND_COUNT DESC LIMIT 15),

			IDS_FOR_TOP_TRENDS_TT AS 
			(SELECT ID FROM ORGANIZED_TWEETS 
			WHERE TREND IN (SELECT TREND FROM TOP_TRENDS_TT) AND LOCATION_ID = '2295420'),
			--SELECT * FROM IDS_FOR_TOP_TRENDS_TT

			ID_ENTITY_TOP_TRENDS_TT AS
			(SELECT TREND,ID,ENTITY 
			FROM ID_ENTITY
			WHERE ID IN (SELECT ID FROM IDS_FOR_TOP_TRENDS_TT)),


			TREND_ENTITY_TF_IDF_SUM_TT AS
			(SELECT TREND,ENTITY,COUNT(ID) TF_IDF_SUM 
			FROM ID_ENTITY
			WHERE ID IN (SELECT ID FROM IDS_FOR_TOP_TRENDS_TT)
			GROUP BY TREND,ENTITY),
			--SELECT * FROM TREND_ENTITY_TF_IDF_SUM_TT

			TREND_TF_IDF_SQ_SUM_TT AS
			(SELECT TREND, 
			SUM(TF_IDF_SUM*TF_IDF_SUM) AS TF_IDF_SQ_SUM
			FROM TREND_ENTITY_TF_IDF_SUM_TT
			GROUP BY TREND),

			COSINE_DIST_NUM_TT AS 
			(SELECT T1.TREND AS TREND1,T2.TREND AS TREND2,
			SUM(T1.TF_IDF_SUM*T2.TF_IDF_SUM) AS COSINE_NUM
			FROM TREND_ENTITY_TF_IDF_SUM_TT AS T1
			INNER JOIN TREND_ENTITY_TF_IDF_SUM_TT AS T2 ON T2.TREND>T1.TREND AND T1.ENTITY = T2.ENTITY
			GROUP BY T1.TREND,T2.TREND),

			COSINE_DIST_TT AS
			(SELECT TREND1,TREND2,
			COSINE_NUM/(SQRT(T2.TF_IDF_SQ_SUM)*SQRT(T3.TF_IDF_SQ_SUM)) AS COSIND_DIST
			FROM COSINE_DIST_NUM_TT AS T1
			INNER JOIN TREND_TF_IDF_SQ_SUM_TT AS T2 ON T1.TREND1=T2.TREND
			INNER JOIN TREND_TF_IDF_SQ_SUM_TT AS T3 ON T1.TREND2=T3.TREND)

			SELECT * FROM COSINE_DIST_TT ORDER BY TREND1,TREND2;
			"""

			cursor.execute(query,(locationid,))
			trend1_column = 0
			trend2_column = 1
			distance_value_column = 2
			trends_list = [] 
			row_counter = 0
			max_columns = 15
			column_iteration = 1
			distance_matrix = [[0 for x in xrange(max_columns)] for x in xrange(max_columns)]

			for row in cursor:
				trend1 = row[trend1_column]

				if trend1 not in trends_list:
					trends_list.append(trend1)	

				# this is to check 0,0 1,1 and so on
				distance_matrix[row_counter][row_counter] = 0
				# this populates 1,2 and 2,1 and so on 
				# this avoid 2 loops
				print "writing data for the current row " +str(row_counter)+ " and column " +str(column_iteration)

				distance_matrix[row_counter][column_iteration] = row[distance_value_column]	
				distance_matrix[column_iteration][row_counter] = row[distance_value_column]	

				column_iteration = column_iteration + 1
				if column_iteration == max_columns:
					row_counter = row_counter + 1
					column_iteration = row_counter + 1 
						

			return distance_matrix

		except Exception:
			print traceback.format_exc()


