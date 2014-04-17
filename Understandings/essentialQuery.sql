select count(*) from tweets

select count(*) from trends

select * from trends where date = '2014-04-17' 




--delete from trends

--insert into trends select * from trendsBackup where date = '2014-04-17' LIMIT 70 OFFSET 0

--create table tweetsBackup as select * from tweets

select * from tweets where trendId in(select id from trends where trend = '#IPL7')

--insert into tweetsBackup select * from tweets

--insert into tweets select * from tweetsBackup

--delete from tweets