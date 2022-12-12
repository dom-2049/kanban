--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1 (Debian 15.1-1.pgdg110+1)
-- Dumped by pg_dump version 15.0

-- Started on 2022-12-12 20:09:13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 16384)
-- Name: board; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.board (
    title character varying(255) NOT NULL,
    username character varying(255)
);


ALTER TABLE public.board OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16391)
-- Name: board_cardlists; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.board_cardlists (
    board_title character varying(255) NOT NULL,
    cardlists_id uuid NOT NULL
);


ALTER TABLE public.board_cardlists OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16396)
-- Name: boards_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.boards_members (
    username character varying(255) NOT NULL,
    title character varying(255) NOT NULL
);


ALTER TABLE public.boards_members OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16403)
-- Name: card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card (
    id uuid NOT NULL,
    description character varying(255),
    title character varying(255),
    member_username character varying(255),
    card_list_id uuid
);


ALTER TABLE public.card OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16410)
-- Name: card_list; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card_list (
    id uuid NOT NULL,
    title character varying(255),
    board_title character varying(255)
);


ALTER TABLE public.card_list OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16417)
-- Name: card_list_cards; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card_list_cards (
    card_list_id uuid NOT NULL,
    cards_id uuid NOT NULL
);


ALTER TABLE public.card_list_cards OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16420)
-- Name: member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.member (
    username character varying(255) NOT NULL
);


ALTER TABLE public.member OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16426)
-- Name: user_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_account (
    id integer NOT NULL,
    password character varying(255),
    username character varying(255)
);


ALTER TABLE public.user_account OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16425)
-- Name: user_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_account_id_seq OWNER TO postgres;

--
-- TOC entry 3381 (class 0 OID 0)
-- Dependencies: 221
-- Name: user_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_account_id_seq OWNED BY public.user_account.id;


--
-- TOC entry 3204 (class 2604 OID 16429)
-- Name: user_account id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_account ALTER COLUMN id SET DEFAULT nextval('public.user_account_id_seq'::regclass);


--
-- TOC entry 3208 (class 2606 OID 16395)
-- Name: board_cardlists board_cardlists_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.board_cardlists
    ADD CONSTRAINT board_cardlists_pkey PRIMARY KEY (board_title, cardlists_id);


--
-- TOC entry 3206 (class 2606 OID 16390)
-- Name: board board_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.board
    ADD CONSTRAINT board_pkey PRIMARY KEY (title);


--
-- TOC entry 3212 (class 2606 OID 16402)
-- Name: boards_members boards_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.boards_members
    ADD CONSTRAINT boards_members_pkey PRIMARY KEY (username, title);


--
-- TOC entry 3216 (class 2606 OID 16416)
-- Name: card_list card_list_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_list
    ADD CONSTRAINT card_list_pkey PRIMARY KEY (id);


--
-- TOC entry 3214 (class 2606 OID 16409)
-- Name: card card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card
    ADD CONSTRAINT card_pkey PRIMARY KEY (id);


--
-- TOC entry 3220 (class 2606 OID 16424)
-- Name: member member_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member
    ADD CONSTRAINT member_pkey PRIMARY KEY (username);


--
-- TOC entry 3210 (class 2606 OID 16435)
-- Name: board_cardlists uk_kded99tsd4u0thobu32dqrqgm; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.board_cardlists
    ADD CONSTRAINT uk_kded99tsd4u0thobu32dqrqgm UNIQUE (cardlists_id);


--
-- TOC entry 3218 (class 2606 OID 16437)
-- Name: card_list_cards uk_kg43lt0xjpj83qnyk3d9edo2h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_list_cards
    ADD CONSTRAINT uk_kg43lt0xjpj83qnyk3d9edo2h UNIQUE (cards_id);


--
-- TOC entry 3222 (class 2606 OID 16433)
-- Name: user_account user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (id);


--
-- TOC entry 3226 (class 2606 OID 16453)
-- Name: boards_members fk5014fhhiuaf7qi5u16nusi6va; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.boards_members
    ADD CONSTRAINT fk5014fhhiuaf7qi5u16nusi6va FOREIGN KEY (title) REFERENCES public.member(username);


--
-- TOC entry 3231 (class 2606 OID 16478)
-- Name: card_list_cards fk5rvohgmok5wunyfp5mjwa4hsj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_list_cards
    ADD CONSTRAINT fk5rvohgmok5wunyfp5mjwa4hsj FOREIGN KEY (cards_id) REFERENCES public.card(id);


--
-- TOC entry 3228 (class 2606 OID 16463)
-- Name: card fk5toxtls059mevllsrxp1hv92m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card
    ADD CONSTRAINT fk5toxtls059mevllsrxp1hv92m FOREIGN KEY (member_username) REFERENCES public.member(username);


--
-- TOC entry 3224 (class 2606 OID 16443)
-- Name: board_cardlists fkdrayy2allh9oalhpwv0x4fsg3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.board_cardlists
    ADD CONSTRAINT fkdrayy2allh9oalhpwv0x4fsg3 FOREIGN KEY (cardlists_id) REFERENCES public.card_list(id);


--
-- TOC entry 3227 (class 2606 OID 16458)
-- Name: boards_members fkfb2oelvsy6tik9rbp4u4358xk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.boards_members
    ADD CONSTRAINT fkfb2oelvsy6tik9rbp4u4358xk FOREIGN KEY (username) REFERENCES public.board(title);


--
-- TOC entry 3223 (class 2606 OID 16438)
-- Name: board fkj39dcdu66gkei7ny0hka4e3g8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.board
    ADD CONSTRAINT fkj39dcdu66gkei7ny0hka4e3g8 FOREIGN KEY (username) REFERENCES public.member(username);


--
-- TOC entry 3229 (class 2606 OID 16468)
-- Name: card fkmx5a5u9d08u1m63sylcwpq28; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card
    ADD CONSTRAINT fkmx5a5u9d08u1m63sylcwpq28 FOREIGN KEY (card_list_id) REFERENCES public.card_list(id);


--
-- TOC entry 3230 (class 2606 OID 16473)
-- Name: card_list fkq0hrnytt5ej4wwtl4ldkrmwah; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_list
    ADD CONSTRAINT fkq0hrnytt5ej4wwtl4ldkrmwah FOREIGN KEY (board_title) REFERENCES public.board(title);


--
-- TOC entry 3232 (class 2606 OID 16483)
-- Name: card_list_cards fkr38bp0uq5hg4qyxy1a4f88k1x; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_list_cards
    ADD CONSTRAINT fkr38bp0uq5hg4qyxy1a4f88k1x FOREIGN KEY (card_list_id) REFERENCES public.card_list(id);


--
-- TOC entry 3225 (class 2606 OID 16448)
-- Name: board_cardlists fkt71tj5x73w7bnrj6t8ie6n2mq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.board_cardlists
    ADD CONSTRAINT fkt71tj5x73w7bnrj6t8ie6n2mq FOREIGN KEY (board_title) REFERENCES public.board(title);


-- Completed on 2022-12-12 20:09:13

--
-- PostgreSQL database dump complete
--

