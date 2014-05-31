CREATE FUNCTION compute_idf
(in_trend_id uuid,
start_date date,
end_date date)
RETURNS double as $$
DECLARE

	idf double;
	id_entity_var id_entity%ROWTYPE;

BEGIN
	select id,entity from id_entity where id in (select id from organized_tweets where trend_id = in_trend_id 
	and created_at between start_date and end_date) as t1;
	




