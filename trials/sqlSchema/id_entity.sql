--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-06-26 23:46:27 IST

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
-- TOC entry 178 (class 1259 OID 214221)
-- Name: id_entity; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE id_entity (
    id character varying(1000),
    entity character varying(1000),
    trend character varying(200)
);


ALTER TABLE public.id_entity OWNER TO postgres;

--
-- TOC entry 1999 (class 1259 OID 256343)
-- Name: id_entity_id_entity_trend_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX id_entity_id_entity_trend_idx ON id_entity USING btree (id, entity, trend);


-- Completed on 2014-06-26 23:46:27 IST

--
-- PostgreSQL database dump complete
--

