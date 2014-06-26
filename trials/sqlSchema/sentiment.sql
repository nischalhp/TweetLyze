--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-06-26 23:45:54 IST

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
-- TOC entry 180 (class 1259 OID 264533)
-- Name: sentiments; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sentiments (
    id character varying(1000) NOT NULL,
    sentiment character varying(10),
    pos_sentiment double precision,
    neg_sentiment double precision
);


ALTER TABLE public.sentiments OWNER TO postgres;

--
-- TOC entry 2001 (class 2606 OID 264540)
-- Name: sentiments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sentiments
    ADD CONSTRAINT sentiments_pkey PRIMARY KEY (id);


--
-- TOC entry 1999 (class 1259 OID 264541)
-- Name: sentiments_id_sentiment_pos_sentiment_neg_sentiment_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sentiments_id_sentiment_pos_sentiment_neg_sentiment_idx ON sentiments USING btree (id, sentiment, pos_sentiment, neg_sentiment);


-- Completed on 2014-06-26 23:45:54 IST

--
-- PostgreSQL database dump complete
--

