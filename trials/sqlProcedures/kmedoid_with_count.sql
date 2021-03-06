﻿WITH 

TREND_COUNT_TT AS
(SELECT TREND,COUNT(*) AS TREND_COUNT 
FROM TRENDS 
WHERE LOCATIONID = '2295420' GROUP BY TREND),


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