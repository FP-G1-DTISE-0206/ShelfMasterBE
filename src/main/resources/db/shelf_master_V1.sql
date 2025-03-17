
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
                                 "is_verified" BOOLEAN NOT NULL DEFAULT false,
                                 "verification_token" CHARACTER VARYING,
                                 "token_expiry" TIMESTAMP WITH TIME ZONE,
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
-- "public"."user_address"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user_address" CASCADE;
CREATE TABLE "public"."user_address" (
                                         "id" BIGSERIAL NOT NULL,
                                         "user_id" BIGINT NOT NULL,
                                         "contact_name" CHARACTER VARYING NOT NULL,
                                         "contact_number" CHARACTER VARYING NOT NULL,
                                         "area_id" CHARACTER VARYING(255) NOT NULL,
                                         "province" CHARACTER VARYING(255) NOT NULL,
                                         "city" CHARACTER VARYING NOT NULL,
                                         "district" CHARACTER VARYING(255) NOT NULL,
                                         "postal_code" CHARACTER VARYING NOT NULL,
                                         "address" TEXT NOT NULL,
                                         "latitude" DOUBLE PRECISION NOT NULL,
                                         "longitude" DOUBLE PRECISION NOT NULL,
                                         "is_default" BOOLEAN NOT NULL DEFAULT false,
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
-- "public"."warehouse"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."warehouse" CASCADE;
CREATE TABLE "public"."warehouse" (
                                      "id" BIGSERIAL NOT NULL,
                                      "name" CHARACTER VARYING(255) NOT NULL,
                                      "contact_name" CHARACTER VARYING(255) NOT NULL,
                                      "contact_number" CHARACTER VARYING(255) NOT NULL,
                                      "area_id" CHARACTER VARYING(255) NOT NULL,
                                      "province" CHARACTER VARYING(255) NOT NULL,
                                      "city" CHARACTER VARYING(255) NOT NULL,
                                      "district" CHARACTER VARYING(255) NOT NULL,
                                      "postal_code" CHARACTER VARYING(255) NOT NULL,
                                      "address" TEXT NOT NULL,
                                      "latitude" DOUBLE PRECISION NOT NULL,
                                      "longitude" DOUBLE PRECISION NOT NULL,
                                      "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "deleted_at" TIMESTAMP WITH TIME ZONE,
                                      PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."vendor"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."vendor" CASCADE;
CREATE TABLE "public"."vendor" (
                                   "id" BIGSERIAL NOT NULL,
                                   "name" CHARACTER VARYING(255) NOT NULL,
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
                                    "sku" CHARACTER VARYING(255) NOT NULL,
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
                                          "version" BIGINT NOT NULL DEFAULT 0,
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
-- "public"."product_mutation"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_mutation" CASCADE;
CREATE TABLE "public"."product_mutation" (
                                             "id" BIGSERIAL NOT NULL,
                                             "mutation_type_id" BIGINT NOT NULL,
                                             "origin_id" BIGINT NOT NULL,
                                             "destination_id" BIGINT NOT NULL,
                                             "product_id" BIGINT NOT NULL,
                                             "quantity" BIGINT NOT NULL DEFAULT 0,
                                             "requested_by" BIGINT NOT NULL,
                                             "processed_by" BIGINT,
                                             "is_approved" BOOLEAN NOT NULL DEFAULT false,
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
                                                 "product_mutation_id" BIGINT NOT NULL,
                                                 "mutation_status_id" BIGINT NOT NULL,
                                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                                 PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_mutation_log_reason"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_mutation_log_reason" CASCADE;
CREATE TABLE "public"."product_mutation_log_reason" (
                                                        "id" BIGSERIAL NOT NULL,
                                                        "product_mutation_log_id" BIGINT NOT NULL,
                                                        "reason" CHARACTER VARYING(255) NOT NULL,
                                                        "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                        "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                        "deleted_at" TIMESTAMP WITH TIME ZONE,
                                                        PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."product_mutation_order"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."product_mutation_order" CASCADE;
CREATE TABLE "public"."product_mutation_order" (
                                                   "id" BIGSERIAL NOT NULL,
                                                   "order_id" BIGINT NOT NULL,
                                                   "ordered_product_mutation_id" BIGINT NOT NULL,
                                                   "returned_product_mutation_id" BIGINT,
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
                                            "name" CHARACTER VARYING(50) NOT NULL,
                                            "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            "deleted_at" TIMESTAMP WITH TIME ZONE,
                                            PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."mutation_type"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."mutation_type" CASCADE;
CREATE TABLE "public"."mutation_type" (
                                          "id" BIGSERIAL NOT NULL,
                                          "origin_type" CHARACTER VARYING(30) NOT NULL,
                                          "destination_type" CHARACTER VARYING(30) NOT NULL,
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
                                  "payment_method_id" BIGINT DEFAULT 1 NOT NULL ,
                                  "gateway_trx_id" text,
                                  "payment_proof" text,
                                  "warehouse_id" BIGINT NOT NULL,
                                  shipping_cost     DOUBLE PRECISION,
                                  shipping_method   CHARACTER VARYING(255),
                                  final_price       DOUBLE PRECISION    DEFAULT 0   NOT NULL,
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
-- "public"."promotion"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."promotion" CASCADE;
CREATE TABLE "public"."promotion" (
                                      "id" BIGSERIAL NOT NULL,
                                      "title" CHARACTER VARYING(255) NOT NULL,
                                      "description" TEXT,
                                      "image_url" TEXT NOT NULL,
                                      "product_url" TEXT,
                                      "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "deleted_at" TIMESTAMP WITH TIME ZONE,
                                      PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."cart"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."cart" CASCADE;
CREATE TABLE "public"."cart" (
                                 "id" BIGSERIAL NOT NULL,
                                 "user_id" BIGINT NOT NULL,
                                 "product_id" BIGINT NOT NULL,
                                 "quantity" BIGINT NOT NULL DEFAULT 0,
                                 "is_processed" BOOLEAN NOT NULL DEFAULT false,
                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                 PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."payment_method"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."payment_method" CASCADE;

CREATE TABLE "public"."payment_method" (
                                "id"    BIGSERIAL NOT NULL,
                                "name"  CHARACTER VARYING(255) NOT NULL,
                                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                "deleted_at" TIMESTAMP WITH TIME ZONE,
                                PRIMARY KEY ( "id" )
);

-- -----------------------------------
-- Foreign Keys
-- -----------------------------------

ALTER TABLE "public"."user_address"
    ADD CONSTRAINT "user_address_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."user_roles"
    ADD CONSTRAINT "user_roles_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."user_roles"
    ADD CONSTRAINT "user_roles_role_id_fkey" FOREIGN KEY ( "role_id" ) REFERENCES "public"."role" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_stock"
    ADD CONSTRAINT "product_stock_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_stock"
    ADD CONSTRAINT "product_stock_warehouse_id_fkey" FOREIGN KEY ( "warehouse_id" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_images"
    ADD CONSTRAINT "product_images_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_categories"
    ADD CONSTRAINT "product_categories_category_id_fkey" FOREIGN KEY ( "category_id" ) REFERENCES "public"."category" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_categories"
    ADD CONSTRAINT "product_categories_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_mutation"
    ADD CONSTRAINT "product_mutation_mutation_type_id_fkey" FOREIGN KEY ( "mutation_type_id" ) REFERENCES "public"."mutation_type" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_mutation"
    ADD CONSTRAINT "product_mutation_pic_id_fkey" FOREIGN KEY ( "requested_by" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_mutation"
    ADD CONSTRAINT "product_mutation_processed_by_fkey" FOREIGN KEY ( "processed_by" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_mutation"
    ADD CONSTRAINT "product_mutation_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_mutation_log"
    ADD CONSTRAINT "product_mutation_log_product_mutation_id_fkey" FOREIGN KEY ( "product_mutation_id" ) REFERENCES "public"."product_mutation" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."product_mutation_log"
    ADD CONSTRAINT "product_mutation_log_mutation_status_id_fkey" FOREIGN KEY ( "mutation_status_id" ) REFERENCES "public"."mutation_status" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order"
    ADD CONSTRAINT "order_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order_items"
    ADD CONSTRAINT "order_items_order_id_fkey" FOREIGN KEY ( "order_id" ) REFERENCES "public"."order" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order_items"
    ADD CONSTRAINT "order_items_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order_log"
    ADD CONSTRAINT "order_log_order_id_fkey" FOREIGN KEY ( "order_id" ) REFERENCES "public"."order" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order_log"
    ADD CONSTRAINT "order_log_order_status_id_fkey" FOREIGN KEY ( "order_status_id" ) REFERENCES "public"."order_status" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."warehouse_admins"
    ADD CONSTRAINT "warehouse_admins_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."warehouse_admins"
    ADD CONSTRAINT "warehouse_admins_warehouse_id_fkey" FOREIGN KEY ( "warehouse_id" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."cart"
    ADD CONSTRAINT "cart_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."cart"
    ADD CONSTRAINT "cart_product_id_fkey" FOREIGN KEY ( "product_id" ) REFERENCES "public"."product" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order"
    ADD CONSTRAINT "order_payment_method_id_fkey" FOREIGN KEY ( "payment_method_id" ) REFERENCES "public"."payment_method" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE "public"."order"
    ADD CONSTRAINT "order_warehouse_id_fkey" FOREIGN KEY ( "warehouse_id" ) REFERENCES "public"."warehouse" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION
        DEFERRABLE INITIALLY DEFERRED;