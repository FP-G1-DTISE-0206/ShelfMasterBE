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