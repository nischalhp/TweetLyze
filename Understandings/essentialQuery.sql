select count(*) from tweets

select count(*) from trends

select * from trends where date = '2014-04-17' 




--delete from trends

--insert into trends select * from trendsBackup where date = '2014-04-17' LIMIT 70 OFFSET 0

--create table tweetsBackup as select * from tweets

select * from tweets where trendId in(select * from trends where trend = '#IPL7')

--insert into tweetsBackup select * from tweets
insert into tweets select * from tweetsBackup where trendId in (select id from trends where date = '2014-04-17')

--delete from tweets

select * from location

--delete from location where id = '1235'


select count(*) as trendSignificance,trend from trends group by trend order by trendSignificance desc;



select count(distinct(json_extract_path_text(t2.jsonElements,'text')))
from

(select json(json_array_elements(t1.tweets)) as jsonElements from
(select tweets from tweets where trendId in(select id from trends where trend = '#IPL7' ))as t1)as t2


select count(distinct(json_extract_path_text(t2.jsonElements,'text')))
from

(select json(json_array_elements(t1.tweets)) as jsonElements from
(select tweets from tweets where trendId in(select id from trends where trend = '#IPL7' ))as t1)as t2



--select id,d from tweets where trendId in(select id from trends where trend = '#IPL7' )



