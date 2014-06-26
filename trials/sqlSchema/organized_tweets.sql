--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-06-26 23:45:32 IST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 177 (class 1259 OID 214205)
-- Name: organized_tweets; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE organized_tweets (
    id character varying(1000) NOT NULL,
    geo character varying(300),
    retweeted boolean,
    in_reply_to_screen_name character varying(500),
    truncated boolean,
    source character varying(500),
    created_at date,
    place character varying(500),
    user_id character varying(200),
    text character varying(1000),
    entities text,
    user_mentions text,
    retweet_count integer,
    favorite_count integer,
    trend character varying(500),
    location_id character varying(40)
);


ALTER TABLE public.organized_tweets OWNER TO postgres;

--
-- TOC entry 2001 (class 2606 OID 240015)
-- Name: organized_tweets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organized_tweets
    ADD CONSTRAINT organized_tweets_pkey PRIMARY KEY (id);


--
-- TOC entry 1999 (class 1259 OID 240338)
-- Name: id_trend; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX id_trend ON organized_tweets USING btree (id, trend, location_id, created_at);


-- Completed on 2014-06-26 23:45:33 IST

--
-- PostgreSQL database dump complete
--

