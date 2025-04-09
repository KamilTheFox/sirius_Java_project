-- Устанавливаем параметры подключения
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

-- Устанавливаем параметры таблиц
SET default_tablespace = '';
SET default_table_access_method = heap;

-- Создаем таблицу ресторанов (начинаем с неё, так как она независимая)
CREATE TABLE public.restaurant (
    identifier uuid NOT NULL,
    name character varying(255) NOT NULL,
    cuisine character varying(255) NOT NULL,
    minimum_order double precision NOT NULL CHECK (minimum_order >= 0),
    CONSTRAINT restaurant_pkey PRIMARY KEY (identifier)
);

-- Создаем таблицу блюд
CREATE TABLE public.dish (
    identifier uuid NOT NULL,
    name character varying(255) NOT NULL,
    price double precision NOT NULL CHECK (price > 0),
    weight double precision NOT NULL CHECK (weight > 0),
    restaurant uuid NOT NULL,
    CONSTRAINT dish_pkey PRIMARY KEY (identifier),
    CONSTRAINT fk_dish_restaurant FOREIGN KEY (restaurant)
        REFERENCES public.restaurant (identifier)
        ON DELETE CASCADE
);

-- Создаем таблицу заказов
CREATE TABLE public.orders (
    identifier uuid NOT NULL,
    delivery_address character varying(255) NOT NULL,
    delivery_time timestamp(6) without time zone,
    dishes uuid[] NOT NULL,
    restaurant uuid NOT NULL,
    total_amount double precision NOT NULL CHECK (total_amount >= 0),
    CONSTRAINT orders_pkey PRIMARY KEY (identifier),
    CONSTRAINT fk_order_restaurant FOREIGN KEY (restaurant)
        REFERENCES public.restaurant (identifier)
        ON DELETE CASCADE
);

-- Создаем индексы для оптимизации запросов
CREATE INDEX idx_dish_restaurant ON public.dish(restaurant);
CREATE INDEX idx_orders_restaurant ON public.orders(restaurant);

-- Устанавливаем владельца
ALTER TABLE public.restaurant OWNER TO "user";
ALTER TABLE public.dish OWNER TO "user";
ALTER TABLE public.orders OWNER TO "user";

-- Добавляем комментарии к таблицам
COMMENT ON TABLE public.restaurant IS 'Таблица ресторанов';
COMMENT ON TABLE public.dish IS 'Таблица блюд';
COMMENT ON TABLE public.orders IS 'Таблица заказов';