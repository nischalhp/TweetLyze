CREATE FUNCTION disintegrate_tweets(trend_id uuid) RETURNS void as $$
DECLARE
BEGIN
	insert into organized_tweets
	select distinct t3.id as "id",t3.geo as "geo",t3.retweeted as "retweeted",
	t3.in_reply_to_screen_name as "in_reply_to_screen_name",t3.truncated as "truncated"
	,t3.source as "source",t3.created_at as "created_at",t3.place as "place"
	,t3.user_id as "user_id",
	t3.text as "text",t3.hashtags as "hashtags",t3.user_mentions as "user_mentions",
	t3.retweet_count as "retweet_count",t3.favorite_count as "favorite_count",t3.trend_id as "trend_id"
	from 
		(select 
			json_extract_path_text(t2.jsonElements,'id') as "id",
			json_extract_path_text(t2.jsonElements,'geo') as "geo", 
			CAST (json_extract_path_text(t2.jsonElements,'retweeted') AS boolean) as "retweeted" , 
			json_extract_path_text(t2.jsonElements,'in_reply_to_screen_name') as "in_reply_to_screen_name",
			CAST (json_extract_path_text(t2.jsonElements,'truncated') as boolean) as "truncated",
			json_extract_path_text(t2.jsonElements,'source') as "source",
			CAST (json_extract_path_text(t2.jsonElements,'created_at') as date) as "created_at",
			json_extract_path_text(t2.jsonElements,'place') as "place",
			json_extract_path_text(json_extract_path(t2.jsonElements,'user'),'id') as "user_id",
			json_extract_path_text(t2.jsonElements,'text') as "text",
			json_extract_path_text(json_extract_path(t2.jsonElements,'entities'),'hashtags') as "hashtags",
			json_extract_path_text(json_extract_path(t2.jsonElements,'entities'),'user_mentions') as "user_mentions",
			CAST (json_extract_path_text(t2.jsonElements,'retweet_count') as integer) as "retweet_count",
			CAST (json_extract_path_text(t2.jsonElements,'favorite_count') as integer) as "favorite_count",
			t2.trendId as "trend_id"
			from
				(select json_array_elements(t1.tweets) as jsonElements,t1.trendId as trendId 
					from
					(select tweets,trendId from tweets where trendId = trend_id )as t1)as t2)as t3;

END;
$$ LANGUAGE plpgsql;   


--select disintegrate_tweets('45df9aed-4b77-4cb8-9351-aaceb69aa433');     