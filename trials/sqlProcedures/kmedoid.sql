select ft,st, ((fti_sti)/((sqrt(sum(fti_square)))*(sqrt(sum(sti_square))))) as cosine_distance
from
(select ft,st,fe,se,fti*sti as fti_sti,(fti*fti) as fti_square,(sti*sti) as sti_square
from
(select first_block.trend as ft,second_block.trend as st,first_block.entity fe
,second_block.entity as se,first_block.tf_idf_score as fti,second_block.tf_idf_score as sti from 
(select * from
(select main.entity,main.trend,sum(main.tf_idf) as tf_idf_score
from
(select first.id,first.entity,first.trend,second.count_id,third.count_entity,
	(1.0/third.count_entity)*log((
	(select count(*) from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420'))/second.count_id) as tf_idf  from
(select id,trend,entity from id_entity where id in 
	(select id from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420'))as first
inner join
(select count(id) as count_id,trend,entity from id_entity where id in 
	(select id from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420')group by trend,entity) as second
on
first.entity = second.entity
inner join
(select id ,trend,count(entity) as count_entity from id_entity where id in 
	(select id from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420')group by trend,id) as third
on
third.id = first.id)as main group by trend,entity) as main_out order by trend)as first_block
,
(select * from
(select main.entity,main.trend,sum(main.tf_idf) as tf_idf_score
from
(select first.id,first.entity,first.trend,second.count_id,third.count_entity,
	(1.0/third.count_entity)*log((
	(select count(*) from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420'))/second.count_id) as tf_idf  from
(select id,trend,entity from id_entity where id in 
	(select id from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420'))as first
inner join
(select count(id) as count_id,trend,entity from id_entity where id in 
	(select id from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420')group by trend,entity) as second
on
first.entity = second.entity
inner join
(select id ,trend,count(entity) as count_entity from id_entity where id in 
	(select id from organized_tweets where trend in 
		(select t1.trend as trend from
			(select count(*) as c,trend from trends where 
				locationid = '2295420' group by trend)
							as t1 order by c desc limit 15)
									and location_id = '2295420')group by trend,id) as third
on
third.id = first.id)as main group by trend,entity) as main_out order by trend) as second_block
where second_block.trend > first_block.trend and
first_block.entity = second_block.entity)as full_block order by ft,st)as computed_block group by ft,st,fti_sti