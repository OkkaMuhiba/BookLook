-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    number_phone character varying(255) COLLATE pg_catalog."default",
    user_photo text COLLATE pg_catalog."default",
    read_key character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to blibli;

-- SEQUENCE: public.roles_role_id_seq

-- DROP SEQUENCE public.roles_role_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.roles_role_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.roles_role_id_seq
    OWNER TO blibli;

-- Table: public.roles

-- DROP TABLE public.roles;

CREATE TABLE IF NOT EXISTS public.roles
(
    role_id bigint NOT NULL DEFAULT nextval('roles_role_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (role_id),
    CONSTRAINT uk_nb4h0p6txrmfc0xbrd1kglp9t UNIQUE (name)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.roles
    OWNER to blibli;

-- Table: public.user_roles

-- DROP TABLE public.user_roles;

CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
    CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id)
        REFERENCES public.roles (role_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_roles
    OWNER to blibli;

-- Table: public.markets

-- DROP TABLE public.markets;

CREATE TABLE IF NOT EXISTS public.markets
(
    market_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    market_name character varying(255) COLLATE pg_catalog."default",
    market_bio text COLLATE pg_catalog."default",
    market_photo text COLLATE pg_catalog."default",
    market_code character varying(255) COLLATE pg_catalog."default",
    total_product bigint,
    user_id character varying(255) COLLATE pg_catalog."default",
    user_fk character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT markets_pkey PRIMARY KEY (market_id),
    CONSTRAINT fknu6rbk1tdygyvr5x1h6mbayxe FOREIGN KEY (user_fk)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.markets
    OWNER to blibli;

-- Table: public.products

-- DROP TABLE public.products;

CREATE TABLE IF NOT EXISTS public.products
(
    product_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    title character varying(255) COLLATE pg_catalog."default",
    author character varying(255) COLLATE pg_catalog."default",
    publisher character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    isbn character varying(255) COLLATE pg_catalog."default",
    page_total bigint,
    price bigint,
    sku character varying(255) COLLATE pg_catalog."default",
    product_file text COLLATE pg_catalog."default",
    product_photo text COLLATE pg_catalog."default",
    product_confirm character varying(255) COLLATE pg_catalog."default",
    market_fk character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT products_pkey PRIMARY KEY (product_id),
    CONSTRAINT fk577r8bwy8ayrrldgib3lk3yce FOREIGN KEY (market_fk)
        REFERENCES public.markets (market_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.products
    OWNER to blibli;

-- Table: public.category_lists

-- DROP TABLE public.category_lists;

CREATE TABLE IF NOT EXISTS public.category_lists
(
    category_list_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    category_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT category_lists_pkey PRIMARY KEY (category_list_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.category_lists
    OWNER to blibli;

-- Table: public.category_products

-- DROP TABLE public.category_products;

CREATE TABLE IF NOT EXISTS public.category_products
(
    product_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    category_list_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT category_products_pkey PRIMARY KEY (product_id, category_list_id),
    CONSTRAINT fkkbg1enc0xm91hrxap7tlr32fl FOREIGN KEY (category_list_id)
        REFERENCES public.category_lists (category_list_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkpr3kfk7ij874uqy9u846oc42o FOREIGN KEY (product_id)
        REFERENCES public.products (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.category_products
    OWNER to blibli;

-- Table: public.baskets

-- DROP TABLE public.baskets;

CREATE TABLE IF NOT EXISTS public.baskets
(
    basket_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_fk character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT baskets_pkey PRIMARY KEY (basket_id),
    CONSTRAINT fk3f1pch9lnfsku3pidxuk399yq FOREIGN KEY (user_fk)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.baskets
    OWNER to blibli;

-- Table: public.basket_details

-- DROP TABLE public.basket_details;

CREATE TABLE IF NOT EXISTS public.basket_details
(
    bakset_detail_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    basket_fk character varying(255) COLLATE pg_catalog."default",
    product_fk character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT basket_details_pkey PRIMARY KEY (bakset_detail_id),
    CONSTRAINT fk1iakmittb92rphd19ku283a3d FOREIGN KEY (basket_fk)
        REFERENCES public.baskets (basket_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkqrjouwfxwj58jfm76k3i691o2 FOREIGN KEY (product_fk)
        REFERENCES public.products (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.basket_details
    OWNER to blibli;

-- Table: public.libraries

-- DROP TABLE public.libraries;

CREATE TABLE IF NOT EXISTS public.libraries
(
    library_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_fk character varying(255) COLLATE pg_catalog."default",
    product_fk character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT libraries_pkey PRIMARY KEY (library_id),
    CONSTRAINT fk3cgbcuidmj5667xvk91iot4ga FOREIGN KEY (product_fk)
        REFERENCES public.products (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkef4n44uak9ydn7fxd978unctd FOREIGN KEY (user_fk)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.libraries
    OWNER to blibli;

-- Table: public.transactions

-- DROP TABLE public.transactions;

CREATE TABLE IF NOT EXISTS public.transactions
(
    transaction_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    transaction_code character varying(255) COLLATE pg_catalog."default",
    checkout bigint,
    transfer_confirm character varying(255) COLLATE pg_catalog."default",
    user_id character varying(255) COLLATE pg_catalog."default",
    user_fk character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id),
    CONSTRAINT fkbxsk1df4bqlce3f089ru4g50r FOREIGN KEY (user_fk)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.transactions
    OWNER to blibli;

-- Table: public.transaction_details

-- DROP TABLE public.transaction_details;

CREATE TABLE IF NOT EXISTS public.transaction_details
(
    transaction_detail_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    market_id character varying(255) COLLATE pg_catalog."default",
    product_confirm character varying(255) COLLATE pg_catalog."default",
    transaction_fk character varying(255) COLLATE pg_catalog."default",
    product_fk character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT transaction_details_pkey PRIMARY KEY (transaction_detail_id),
    CONSTRAINT fk83xxlspswe9hq3ovicmafva93 FOREIGN KEY (transaction_fk)
        REFERENCES public.transactions (transaction_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkb0c7dyql7g3ld5tjmebxanfrh FOREIGN KEY (product_fk)
        REFERENCES public.products (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.transaction_details
    OWNER to blibli;

-- Table: public.wishlists

-- DROP TABLE public.wishlists;

CREATE TABLE IF NOT EXISTS public.wishlists
(
    wishlist_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    product_fk character varying(255) COLLATE pg_catalog."default",
    user_fk character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT wishlists_pkey PRIMARY KEY (wishlist_id),
    CONSTRAINT fkmhwwhvbd8ugigaebyp5fbblub FOREIGN KEY (user_fk)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknd6pywa7125tabydaboufw56f FOREIGN KEY (product_fk)
        REFERENCES public.products (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.wishlists
    OWNER to blibli;

-- Table: public.blocked_users

-- DROP TABLE public.blocked_users;

CREATE TABLE IF NOT EXISTS public.blocked_users
(
    blocked_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_fk character varying(255) COLLATE pg_catalog."default",
    started_at timestamp without time zone,
    end_at timestamp without time zone,
    CONSTRAINT blocked_users_pkey PRIMARY KEY (blocked_id),
    CONSTRAINT fkqujdkupq59u5jrsh8gyob20n3 FOREIGN KEY (user_fk)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.blocked_users
    OWNER to blibli;