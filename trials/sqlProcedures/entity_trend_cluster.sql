select t1.entity,t2.trend from
	(select id,entity from id_entity ) as t1
	inner join
	(select id,trend from organized_tweets where trend in 
	(select trend from 
	(select count(*) as c,trend from 
	trends where locationid = '2295420' group by trend)as t_in order 
	by c desc limit 15))as t2
on
t1.id = t2.id