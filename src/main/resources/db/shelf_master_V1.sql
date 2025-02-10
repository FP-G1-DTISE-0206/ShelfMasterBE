
-- -----------------------------------
-- "public"."user"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user" CASCADE;
CREATE TABLE "public"."user" (
                                 "id" BIGSERIAL NOT NULL,
                                 "email" CHARACTER VARYING(255) NOT NULL,
                                 "user_name" CHARACTER VARYING(255) NOT NULL,
                                 "password" CHARACTER VARYING NOT NULL,
                                 "image_url" TEXT,
                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                 PRIMARY KEY ( "id" ),
                                 CONSTRAINT "user_email_key" UNIQUE ( "email" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."role"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."role" CASCADE;
CREATE TABLE "public"."role" (
                                 "id" BIGSERIAL NOT NULL,
                                 "name" CHARACTER VARYING(255) NOT NULL,
                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                 PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."address"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."address" CASCADE;
CREATE TABLE "public"."address" (
                                    "id" BIGSERIAL NOT NULL,
                                    "location" TEXT,
                                    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "deleted_at" TIMESTAMP WITH TIME ZONE,
                                    PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."user_roles"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user_roles" CASCADE;
CREATE TABLE "public"."user_roles" (
                                       "id" BIGSERIAL NOT NULL,
                                       "user_id" BIGINT NOT NULL,
                                       "role_id" BIGINT NOT NULL,
                                       "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "deleted_at" TIMESTAMP WITH TIME ZONE,
                                       PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."user_addresses"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user_addresses" CASCADE;
CREATE TABLE "public"."user_addresses" (
                                           "id" BIGSERIAL NOT NULL,
                                           "user_id" BIGINT NOT NULL,
                                           "address_id" BIGINT NOT NULL,
                                           "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           "deleted_at" SERIAL,
                                           PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."warehouse"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."warehouse" CASCADE;
CREATE TABLE "public"."warehouse" (
                                      "id" BIGSERIAL NOT NULL,
                                      "name" CHARACTER VARYING(255) NOT NULL,
                                      "location" TEXT NOT NULL,
                                      "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "deleted_at" TIMESTAMP WITH TIME ZONE,
                                      PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product" CASCADE;
CREATE TABLE "public"."product" (
                                    "id" BIGSERIAL NOT NULL,
                                    "name" CHARACTER VARYING(255) NOT NULL,
                                    "sku" CHARACTER VARYING(100) NOT NULL,
                                    "description" TEXT,
                                    "price" DOUBLE PRECISION NOT NULL DEFAULT 0,
                                    "weight" DOUBLE PRECISION NOT NULL DEFAULT 0,
                                    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "deleted_at" TIMESTAMP WITH TIME ZONE,
                                    PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_stock"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_stock" CASCADE;
CREATE TABLE "public"."product_stock" (
                                          "id" BIGSERIAL NOT NULL,
                                          "warehouse_id" BIGINT NOT NULL,
                                          "product_id" BIGINT NOT NULL,
                                          "quantity" BIGINT NOT NULL DEFAULT 0,
                                          "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "deleted_at" TIMESTAMP WITH TIME ZONE,
                                          PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_images"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_images" CASCADE;
CREATE TABLE "public"."product_images" (
                                           "id" BIGSERIAL NOT NULL,
                                           "product_id" BIGINT NOT NULL,
                                           "image_url" TEXT NOT NULL,
                                           "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           "deleted_at" TIMESTAMP WITH TIME ZONE,
                                           PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."category"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."category" CASCADE;
CREATE TABLE "public"."category" (
                                     "id" BIGSERIAL NOT NULL,
                                     "name" CHARACTER VARYING(255) NOT NULL,
                                     "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     "deleted_at" TIMESTAMP WITH TIME ZONE,
                                     PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_categories"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_categories" CASCADE;
CREATE TABLE "public"."product_categories" (
                                               "id" BIGSERIAL NOT NULL,
                                               "product_id" BIGINT NOT NULL,
                                               "category_id" BIGINT NOT NULL,
                                               "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               "deleted_at" TIMESTAMP WITH TIME ZONE,
                                               PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_mutation_form"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_mutation_form" CASCADE;
CREATE TABLE "public"."product_mutation_form" (
                                                  "id" BIGSERIAL NOT NULL,
                                                  "requested_by" BIGINT NOT NULL,
                                                  "warehouse_destination" BIGINT NOT NULL,
                                                  "warehouse_origin" BIGINT NOT NULL,
                                                  "product_id" BIGINT NOT NULL,
                                                  "quantity" BIGINT NOT NULL DEFAULT 0,
                                                  "is_approved" BOOLEAN NOT NULL DEFAULT false,
                                                  "approved_by" BIGINT,
                                                  "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                  "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                  "deleted_at" TIMESTAMP WITH TIME ZONE,
                                                  PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_mutation_log"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_mutation_log" CASCADE;
CREATE TABLE "public"."product_mutation_log" (
                                                 "id" BIGSERIAL NOT NULL,
                                                 "product_mutation_form_id" BIGINT NOT NULL,
                                                 "mutation_status_id" BIGINT NOT NULL,
                                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                                 PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."mutation_status"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."mutation_status" CASCADE;
CREATE TABLE "public"."mutation_status" (
                                            "id" BIGSERIAL NOT NULL,
                                            "name" CHARACTER VARYING(255) NOT NULL,
                                            "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            "deleted_at" TIMESTAMP WITH TIME ZONE,
                                            PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."order"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."order" CASCADE;
CREATE TABLE "public"."order" (
                                  "id" BIGSERIAL NOT NULL,
                                  "user_id" BIGINT NOT NULL,
                                  "latest_status" CHARACTER VARYING(255) NOT NULL,
                                  "total_price" DOUBLE PRECISION NOT NULL DEFAULT 0,
                                  "is_paid" BOOLEAN NOT NULL DEFAULT false,
                                  "address_id" BIGINT NOT NULL,
                                  "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "deleted_at" TIMESTAMP WITH TIME ZONE,
                                  PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."order_items"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."order_items" CASCADE;
CREATE TABLE "public"."order_items" (
                                        "id" BIGSERIAL NOT NULL,
                                        "order_id" BIGINT NOT NULL,
                                        "product_id" BIGINT NOT NULL,
                                        "quantity" BIGINT NOT NULL DEFAULT 0,
                                        "total_price" DOUBLE PRECISION NOT NULL DEFAULT 0,
                                        "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "deleted_at" TIMESTAMP WITH TIME ZONE,
                                        PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."order_log"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."order_log" CASCADE;
CREATE TABLE "public"."order_log" (
                                      "id" BIGSERIAL NOT NULL,
                                      "order_id" BIGINT NOT NULL,
                                      "order_status_id" BIGINT NOT NULL,
                                      "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "deleted_at" TIMESTAMP WITH TIME ZONE,
                                      PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."order_status"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."order_status" CASCADE;
CREATE TABLE "public"."order_status" (
                                         "id" BIGSERIAL NOT NULL,
                                         "name" CHARACTER VARYING(255) NOT NULL,
                                         "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "deleted_at" TIMESTAMP WITH TIME ZONE,
                                         PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."warehouse_admins"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."warehouse_admins" CASCADE;
CREATE TABLE "public"."warehouse_admins" (
                                             "id" BIGSERIAL NOT NULL,
                                             "user_id" BIGINT NOT NULL,
                                             "warehouse_id" BIGINT NOT NULL,
                                             "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             "deleted_at" TIMESTAMP WITH TIME ZONE,
                                             PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- Foreign Keys
-- -----------------------------------

ALTER TABLE "public"."user_roles"
    ADD CONSTRAINT "user_roles_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."user_roles"
    ADD CONSTRAINT "user_roles_role_id_fkey" FOREIGN KEY ( "role_id" ) REFERENCES "public"."role" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."user_addresses"
    ADD CONSTRAINT "user_addresses_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."user_addresses"
    ADD CONSTRAINT "user_addresses_address_id_fkey" FOREIGN KEY ( "address_id" ) REFERENCES "public"."address" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_stock"
    ADD CONSTRAINT "product_stock_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_stock"
    ADD CONSTRAINT "product_stock_warehouse_id_fkey" FOREIGN KEY ( "warehouse_id" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_images"
    ADD CONSTRAINT "product_images_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_categories"
    ADD CONSTRAINT "product_categories_category_id_fkey" FOREIGN KEY ( "category_id" ) REFERENCES "public"."category" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_categories"
    ADD CONSTRAINT "product_categories_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_form"
    ADD CONSTRAINT "product_mutation_form_pic_id_fkey" FOREIGN KEY ( "requested_by" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_form"
    ADD CONSTRAINT "product_mutation_form_approved_by_fkey" FOREIGN KEY ( "approved_by" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_form"
    ADD CONSTRAINT "product_mutation_form_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_form"
    ADD CONSTRAINT "product_mutation_form_warehouse_destination_fkey" FOREIGN KEY ( "warehouse_destination" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_form"
    ADD CONSTRAINT "product_mutation_form_warehouse_origin_fkey" FOREIGN KEY ( "warehouse_origin" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_log"
    ADD CONSTRAINT "product_mutation_log_product_mutation_form_id_fkey" FOREIGN KEY ( "product_mutation_form_id" ) REFERENCES "public"."product_mutation_form" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."product_mutation_log"
    ADD CONSTRAINT "product_mutation_log_mutation_status_id_fkey" FOREIGN KEY ( "mutation_status_id" ) REFERENCES "public"."mutation_status" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."order"
    ADD CONSTRAINT "order_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."order_items"
    ADD CONSTRAINT "order_items_order_id_fkey" FOREIGN KEY ( "order_id" ) REFERENCES "public"."order" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."order_items"
    ADD CONSTRAINT "order_items_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."order_log"
    ADD CONSTRAINT "order_log_order_id_fkey" FOREIGN KEY ( "order_id" ) REFERENCES "public"."order" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."order_log"
    ADD CONSTRAINT "order_log_order_status_id_fkey" FOREIGN KEY ( "order_status_id" ) REFERENCES "public"."order_status" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."warehouse_admins"
    ADD CONSTRAINT "warehouse_admins_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."warehouse_admins"
    ADD CONSTRAINT "warehouse_admins_warehouse_id_fkey" FOREIGN KEY ( "warehouse_id" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;
