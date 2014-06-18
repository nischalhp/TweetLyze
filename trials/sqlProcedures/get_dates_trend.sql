select max(date),min(date) from trends where trend in 
		(select t1.trend as trend from
		(select count(*) as c,trend from trends where 
			locationid = '2295420' group by trend)as t1 order by c desc limit 15)
			and locationid = '2295420'