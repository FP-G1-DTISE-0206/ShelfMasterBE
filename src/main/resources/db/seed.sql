insert into public.role (name)
values ('USER');
insert into public.role (name)
values ('WH_ADMIN');
insert into public.role (name)
values ('SUPER_ADMIN');

insert into public.user (email, user_name, password, image_url, is_verified)
values ('admin@admin.com', 'admina', '$2a$10$dlVVAX2bBVLjpVgxO5RNqOLs/j1wRNbjTUWtzdMoRq133R2d4NwxG', null, true);
insert into public.user_roles (user_id, role_id)
values (1, 3);

INSERT INTO public.category ("name") VALUES
	 ('Food'),
	 ('Bread'),
	 ('Snack'),
	 ('Drink');

INSERT INTO public.product ("name",sku,description,price,weight) VALUES
	 ('Dua Lombok Seblak 60 g','-','Dua Lombok Seblak 60 g adalah seblak lezat yang cocok untuk dimakan sebagaii makanan ringan maupun dicampur dengan makanan berkuah seperti soto dan baso. Rasanya yang pedas dan gurih, pastinya akan bikin ketagihan.',9200.0,65.0),
	 ('Bean Spot Roti Keju Susu','-','Roti Keju Susu adalah produk roti siap makan yang terbuat dari tepung pilihan dengan kualitas premium terbaik yang kemudian diproses secara modern sehingga mampu menghasilkan produk roti susu yang sangat lembut di dalamnya, sehingga sangat enak untuk di makan. Dilengkapi dengan gurihnya krim keju sebagai isian yang membuat Roti ini semakin enak untuk dinikmati. Krim keju yang meleleh di mulut mampu memberikan kenikmatan tiada tara.',7000.0,15.0);

INSERT INTO public.product_images (product_id,image_url) VALUES
	 (1,'https://cdn.builder.io/api/v1/image/assets%2F30e3604c9ea247809c03511e4a9c5f3f%2F8f1b5c652928482b91f28bfb195b14b1'),
	 (2,'https://cdn.builder.io/api/v1/image/assets%2F30e3604c9ea247809c03511e4a9c5f3f%2F7287664dd472424c95ea8156efef6737'),
	 (2,'https://cdn.builder.io/api/v1/image/assets%2F30e3604c9ea247809c03511e4a9c5f3f%2Fef3b1d0a94974697a75ad54fac68ff1b');

INSERT INTO public.product_categories (product_id,category_id) VALUES
	 (1,1),
	 (2,1),
	 (1,2),
	 (2,3);

INSERT INTO public.mutation_status (name) VALUES
	 ('PENDING'),
	 ('CANCELED'),
	 ('APPROVED'),
	 ('REJECTED');

INSERT INTO public.mutation_type (origin_type, destination_type) VALUES
	 ('VENDOR', 'WAREHOUSE'),
	 ('WAREHOUSE', 'WAREHOUSE'),
	 ('WAREHOUSE', 'USER'),
	 ('USER', 'WAREHOUSE');