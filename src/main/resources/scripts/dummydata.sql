-- To reset the sequence and delete the data from tables

--/*
alter sequence address_sequence restart;
alter sequence profile_sequence restart;
alter sequence user_sequence restart;
alter sequence comments_sequence restart;
alter sequence follows_sequence restart;
alter sequence likes_sequence restart;
alter sequence posts_sequence restart;

delete from follows;
delete from likes;
delete from comments;
delete from posts;
delete from app_user;
delete from profile;
delete from address;
--*/

alter table address 
alter column address_id set default nextval('address_sequence');

alter table profile 
alter column profile_id set default nextval('profile_sequence');

alter table app_user 
alter column user_id set default nextval('user_sequence');	

alter table comments
alter column id set default nextval('comments_sequence');	

alter table follows
alter column id set default nextval('follows_sequence');	

alter table likes
alter column id set default nextval('likes_sequence');	

alter table posts
alter column post_id set default nextval('posts_sequence');	

-- Address --

insert into address(barangay, city, house_no, province, street, subdivision, zip) values 
('Brgy. 168', 'Manila', '1', 'Metro Manila', '1st', 'Beverly', 1000),
('Brgy. 169', 'Manila', '2', 'Metro Manila', '2nd', 'Beverly', 1000),
('Brgy. 170', 'Manila', '3', 'Metro Manila', '3rd', 'Beverly', 1000),
('Brgy. 171', 'Manila', '4', 'Metro Manila', '4th', 'Beverly', 1000),
('Sto.Nino', 'Meycauayan', '5', 'Bulacan', '5th', 'Corazon', 3020),
('Loma De Gato', 'Marilao', '6', 'Bulacan', '6th', 'Heritage', 3019),
('San Antonio', 'Pasig', '7', 'Metro Manila', '7th', 'Green Heights', 1605),
('Laguan', 'Antipolo', '8', 'Rizal', '8th', 'Park View', 1850);

-- Profile -- 

insert into profile(firstname, gender, lastname, address_id, register_date, last_updated) values
('Harvey', 'MALE', 'Samson', 10000, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('Bryn', 'MALE', 'Bandiola', 10001, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('Ayn', 'MALE', 'Uson', 10002, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('Lawrence', 'MALE', 'Paulino', 10003, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('Sok', 'FEMALE', 'Yi', 10004, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('Dave', 'MALE', 'Ace', 10005, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('John', 'MALE', 'Dew', 10006, '2023-02-20 13:17:32', '2023-02-20 13:17:32'),
('Traci', 'FEMALE', 'Pau', 10007, '2023-02-20 13:17:32', '2023-02-20 13:17:32');

-- AppUser --

insert into app_user(date_created, email, last_updated, password, username, profile_id) values 
('2023-02-20 13:17:32', 'harvey@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'harveysamson', 10000),
('2023-02-20 13:17:32', 'bryn@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'bryn', 10001),
('2023-02-20 13:17:32', 'ayn@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'aynuson', 10002),
('2023-02-20 13:17:32', 'lawrence@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'lawrencepaulino', 10003),
('2023-02-20 13:17:32', 'sok@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'sokyi', 10004),
('2023-02-20 13:17:32', 'dave@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'daveace', 10005),
('2023-02-20 13:17:32', 'john@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'johndew', 10006),
('2023-02-20 13:17:32', 'traci@gmail.com', '2023-02-20 13:17:32', '$2a$12$9k5vh/lQwiQrfAuu5FzZteLWkc4UPOv4OcFvNHQDkga4lxONn2cpO', 'tracipau', 10007);

-- Posts --

insert into posts(image_url, message, user_id) values 
('https://media.istockphoto.com/id/1388524172/photo/asian-child-playing-at-park.jpg?b=1&s=170667a&w=0&k=20&c=-1MKEPIA7opxNO8M9V5fSjqSUuhwAFW8EEnTm5pFAt4=', 'My Child Playing', 10006 ),
('https://media.istockphoto.com/id/1356452059/photo/cafe.jpg?b=1&s=170667a&w=0&k=20&c=xGhOvg9rozwP_upBoaoNYuY0RIz8M3dUe1Tt9gAajWY=', 'Good morning to all', 10004),
('https://media.istockphoto.com/id/1340716614/photo/abstract-icon-representing-the-ecological-call-to-recycle-and-reuse-in-the-form-of-a-pond.jpg?s=612x612&w=0&k=20&c=CglVAOWBC02qDc6Wa2ltd1L-lBVPTaYahFDEXJa4ido=', 'Let us save earth', 10007),
('https://media.istockphoto.com/id/1206622213/photo/mother-father-children-son-and-daughter-runing-and-jumping-on-sunset.jpg?s=612x612&w=0&k=20&c=_cpcCvDBTGUvuQM6A9eboTirh45wubZt2Uhd0t9ylWc=', 'Happy Family', 10006),
('https://media.istockphoto.com/id/1257922267/vector/illustration-of-the-concept-of-sustainability-corporate-social-responsibility-or.jpg?s=612x612&w=0&k=20&c=VmIWFMbKy_9egmL2NbxPU4_8Ly3I75e7sW5Kn9o3bbs=', 'My First Web App', 10004);

-- Comments --

insert into comments(message, post_id, user_id) values 
('Great Photo', 10000, 10000),
('Hello, Good morning', 10001, 10003),
('Add Reduce, Reuse, Recycle text to the photo', 10002, 10001),
('Nice shot!', 10003, 10002),
('Great Achievement ', 10004, 10001);

-- Likes --

insert into likes(liked, post_id, user_id) values
(true, 10000, 10000),
(true, 10001, 10003),
(true, 10002, 10001),
(true, 10003, 10002),
(true, 10004, 10001),
(true, 10001, 10000),
(true, 10002, 10000),
(true, 10003, 10000),
(true, 10004, 10000);

-- Follows --

insert into follows(follower_id, following_id) values
(10000, 10001),
(10000, 10002),
(10000, 10003),
(10001, 10000),
(10001, 10002),
(10001, 10003),
(10002, 10000),
(10002, 10001),
(10002, 10003),
(10003, 10000),
(10003, 10001),
(10003, 10002);









