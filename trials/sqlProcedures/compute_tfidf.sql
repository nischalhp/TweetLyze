select * from (select t4.entity,sum(t4.tf_idf) as tf_idf_score
from
(select t1.id,t1.entity,t2.count_id,t3.count_entity,
(1.0/t3.count_entity)*log((
		select count(*) from organized_tweets 
		where trend = 'Modi' 
		and location_id = '2295420')/t2.count_id) as tf_idf  from
(select id,entity from id_entity where id in
	(select id from organized_tweets 
		where trend = 'Modi' 
		and location_id = '2295420')) as t1
inner join
(select entity,count(id) as count_id from id_entity where id in
	(select id from organized_tweets 
		where trend = 'Modi' 
		and location_id = '2295420')group by entity) as t2
on
t1.entity = t2.entity
inner join
(select id,count(entity) as count_entity from id_entity where id in(select id from organized_tweets 
	where trend = 'Modi' 
		and location_id = '2295420' )group by id) as t3
on 
t1.id = t3.id) as t4 group by entity)as t5 order by tf_idf_score desc;


