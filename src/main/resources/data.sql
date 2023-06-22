-- 사용자 정보가 자동으로 저장이 되기 위해
-- resources에 file 생성, data.sql 이름은 많이 쓰는 컨벤션
-- 기본적인 유저 초기값 담아주는 방법, Postman에서 postmapping 확인
insert into account_user(id, name, created_at, updated_at)
values (1, 'Pororo', now(), now());
insert into account_user(id, name, created_at, updated_at)
values (2, 'Lupi', now(), now());
insert into account_user(id, name, created_at, updated_at)
values (3, 'Eddie', now(), now());

