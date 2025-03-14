--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4 (Debian 17.4-1.pgdg120+2)
-- Dumped by pg_dump version 17.4 (Debian 17.4-1.pgdg120+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: dish; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.dish (
    identifier uuid NOT NULL,
    name character varying(255),
    price double precision,
    restaurant uuid,
    weight double precision
);


ALTER TABLE public.dish OWNER TO "user";

--
-- Name: orders; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.orders (
    identifier uuid NOT NULL,
    delivery_address character varying(255),
    delivery_time timestamp(6) without time zone,
    dishes uuid[],
    restaurant uuid,
    total_amount double precision
);


ALTER TABLE public.orders OWNER TO "user";

--
-- Name: restaurant; Type: TABLE; Schema: public; Owner: user
--

CREATE TABLE public.restaurant (
    identifier uuid NOT NULL,
    cuisine character varying(255),
    minimum_order double precision,
    name character varying(255)
);


ALTER TABLE public.restaurant OWNER TO "user";

--
-- Data for Name: dish; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.dish (identifier, name, price, restaurant, weight) FROM stdin;
f5d7a0b2-6180-43fe-8118-8df4f40fc5f5	╨б╤Г╨┐	100	30aa6969-d53d-4c26-9d78-7f3115959ab7	100
6b50d175-4f74-4b76-8061-0364af8973cc	╨Ъ╨░╤А╤В╨╛╤Д╨╡╨╗╤М	150	30aa6969-d53d-4c26-9d78-7f3115959ab7	120
96e34de8-7c5d-4d13-b77d-02add70e5236	╨У╨╛╨▓╤П╨┤╨╕╨╜╨░	200	30aa6969-d53d-4c26-9d78-7f3115959ab7	10
d3163598-51f1-48eb-a2fc-717b274f66a6	╨Ъ╤Г╤А╨╕╤Ж╨░	100	30aa6969-d53d-4c26-9d78-7f3115959ab7	120
3f09d469-97bd-4942-81f7-9497dd028f72	╨и╨░╤Г╤А╨╝╨░	300	f47024de-0d2b-451a-a88d-004eff96b27b	250
23a2c670-592a-4d21-8460-26607698164f	╨и╨░╤Г╤А╨╝╨░ ╤Б ╨║╨░╤А╤В╨╛╤Д╨╡╨╗╨╡╨╝	360	f47024de-0d2b-451a-a88d-004eff96b27b	350
d224780e-56b2-434c-b8b8-c89b5d030995	╨и╨░╤Г╤А╨╝╨░ ╤Б ╤З╨╕╨╗╨╕	330	f47024de-0d2b-451a-a88d-004eff96b27b	300
b8141386-c078-4846-a422-424c6ead063f	╨Ч╨░╨┐╨╡╨║╨░╨╜╨║╨░	400	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	100
e26c9272-4bbc-4c82-ae8f-dfd9d1295829	╨Ъ╤А╨░╨▒╤Л	400	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	200
da566b34-76b6-4486-b666-89ef0d4d80aa	╨Ы╨╛╨▒╤Б╤В╨╡╤А╤Л	500	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	150
1fbe52ca-911e-43dd-87e1-bbf95ac3e3f3	╨Ь╨╕╨┤╨╕╨╕	500	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	150
e060c6e2-60e0-4c76-8667-7e5886dda058	╨Ы╨╛╤Б╨╛╤Б╤М	500	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	150
56d862e6-8972-48b9-979c-7c82582051bf	╨У╨╛╨▓╤П╨┤╨╕╨╜╨░	500	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	150
1ccab4b2-6d65-4b84-b69f-f5d1963007a6	╨п╨╖╤Л╨║ ╨│╨╛╨▓╤П╨╢╨╕╨╣	500	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	150
5cda9f16-5f61-468c-b300-d809aebcc574	╨У╨╗╨░╨╖╨░	500	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	150
bda49cb5-d365-47a6-8166-f01faf9e940d	╨б╨╡╤В ╨╝╨╕╨╜╨╕	450	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	500
d54f07a7-50e2-4169-a098-d868f85b0227	╨б╨╡╤В ╨б╤Г╤И╨╕	1200	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	1350
7b76c622-6af5-46a8-b9b1-71cb6ca9126b	╨б╨╡╤В ╨Ф╤Г╨▒╨░╨╣	770	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	800
19b30378-0596-4dfd-92e7-7b379a75f43f	╨Ъ╨╛╨╝╨▒╨╛	650	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	650
a4490779-e3c8-4c6b-8712-57063c1dec00	╨д╨╕╨╗╨░╨┤╨╡╨╗╤М╤Д╨╕╤П	1000	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	1000
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.orders (identifier, delivery_address, delivery_time, dishes, restaurant, total_amount) FROM stdin;
e4695c7e-a5a1-40e9-a170-c80f63200e38	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Я╨╗╨░╤В╨░╨╜╨╛╨▓╨░╤П ╨░╨╗╨╗╨╡╤П, ╨┤. 3	2025-03-14 00:22:33.630355	{e060c6e2-60e0-4c76-8667-7e5886dda058,b8141386-c078-4846-a422-424c6ead063f,5cda9f16-5f61-468c-b300-d809aebcc574,1fbe52ca-911e-43dd-87e1-bbf95ac3e3f3}	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	1900
e6abd2a3-49a9-4a01-8207-a02c3d88b54e	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Ю╨╗╨╕╨╝╨┐╨╕╨╣╤Б╨║╨╕╨╣ ╨┐╨░╤А╨║, ╨┤. 11	2025-03-14 00:22:33.642965	{d3163598-51f1-48eb-a2fc-717b274f66a6}	30aa6969-d53d-4c26-9d78-7f3115959ab7	100
511e869f-919a-43b2-a211-1272c4558312	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Ю╨╗╨╕╨╝╨┐╨╕╨╣╤Б╨║╨╕╨╣ ╨┐╨░╤А╨║, ╨┤. 11	2025-03-14 00:22:33.647757	{7b76c622-6af5-46a8-b9b1-71cb6ca9126b}	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	770
5c4bcf07-df1d-421e-aa9e-3338dacd8aaf	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨в╤А╨╕╤Г╨╝╤Д╨░╨╗╤М╨╜╤Л╨╣ ╨┐╤А╨╛╨╡╨╖╨┤, ╨┤. 9	2025-03-14 00:22:33.652914	{bda49cb5-d365-47a6-8166-f01faf9e940d}	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	450
fe7550e8-d81f-44e7-a2e1-8bc07ac250b1	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Я╨╗╨░╤В╨░╨╜╨╛╨▓╨░╤П ╨░╨╗╨╗╨╡╤П, ╨┤. 3	2025-03-14 00:22:33.659143	{3f09d469-97bd-4942-81f7-9497dd028f72}	f47024de-0d2b-451a-a88d-004eff96b27b	300
8ab38ea4-adbc-4985-8891-1ef2b7443f20	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨в╤А╨╕╤Г╨╝╤Д╨░╨╗╤М╨╜╤Л╨╣ ╨┐╤А╨╛╨╡╨╖╨┤, ╨┤. 9	2025-03-14 00:22:33.664958	{5cda9f16-5f61-468c-b300-d809aebcc574,b8141386-c078-4846-a422-424c6ead063f,da566b34-76b6-4486-b666-89ef0d4d80aa,1fbe52ca-911e-43dd-87e1-bbf95ac3e3f3}	c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	1900
e57ef625-09a1-4106-8516-75a388bd6a56	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Я╨░╤А╤Г╤Б╨╜╤Л╨╣ ╨▒╤Г╨╗╤М╨▓╨░╤А, ╨┤. 12	2025-03-14 00:22:33.670022	{a4490779-e3c8-4c6b-8712-57063c1dec00}	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	1000
634e5770-9ddc-4c66-97a4-b98a6c40ef82	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Я╨░╤А╤Г╤Б╨╜╤Л╨╣ ╨▒╤Г╨╗╤М╨▓╨░╤А, ╨┤. 12	2025-03-14 00:22:33.675497	{d54f07a7-50e2-4169-a098-d868f85b0227}	c2901aae-f9d1-4ef4-af23-00b8c7f62e22	1200
6ac539d5-2fbe-429c-b6c7-c7501522c0d7	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Ш╨╝╨╡╤А╨╡╤В╨╕╨╜╤Б╨║╨░╤П ╨╜╨░╨▒╨╡╤А╨╡╨╢╨╜╨░╤П, ╨┤. 1	2025-03-14 00:22:33.681992	{23a2c670-592a-4d21-8460-26607698164f}	f47024de-0d2b-451a-a88d-004eff96b27b	360
e3af3d0a-b66d-4641-a2c2-896d4c8c827a	╨┐╨│╤В ╨б╨╕╤А╨╕╤Г╤Б, ╨Я╨░╤А╤Г╤Б╨╜╤Л╨╣ ╨▒╤Г╨╗╤М╨▓╨░╤А, ╨┤. 12	2025-03-14 00:22:33.6882	{96e34de8-7c5d-4d13-b77d-02add70e5236}	30aa6969-d53d-4c26-9d78-7f3115959ab7	200
\.


--
-- Data for Name: restaurant; Type: TABLE DATA; Schema: public; Owner: user
--

COPY public.restaurant (identifier, cuisine, minimum_order, name) FROM stdin;
30aa6969-d53d-4c26-9d78-7f3115959ab7	╨Т╨║╤Г╤Б╨╜╨░╤П	45	╨Т╨╡╨│╨░
f47024de-0d2b-451a-a88d-004eff96b27b	╨б╤В╤А╨░╨╜╨╜╨░╤П	250	╨и╨░╤Г╤А╨╝╨╡╤З╨╜╨░╤П
c7edd6cd-8792-4a7d-bbf9-c7831a9540f6	╨н╨╗╨╕╤В╨╜╨░╤П	1500	╨н╨╗╨╕╤В
c2901aae-f9d1-4ef4-af23-00b8c7f62e22	╨б╤Г╤И╨╡╨╜╨╜╨░╤П	350	╨б╤Г╤И╨╕╨▒╨░╤А
\.


--
-- Name: dish dish_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.dish
    ADD CONSTRAINT dish_pkey PRIMARY KEY (identifier);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (identifier);


--
-- Name: restaurant restaurant_pkey; Type: CONSTRAINT; Schema: public; Owner: user
--

ALTER TABLE ONLY public.restaurant
    ADD CONSTRAINT restaurant_pkey PRIMARY KEY (identifier);


--
-- PostgreSQL database dump complete
--

