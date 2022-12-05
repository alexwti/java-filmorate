ER Diagram
https://github.com/alexwti/java-filmorate/blob/main/DBDiag.png

Примеры запросов:
film:

findAll:
select t.* from film t

getById:
select t.* from film t where t.film_id=2

getTopRaitingFilms:
select t.* from film t where t.film_id in
(select l.film_id from likes_film l group by l.film_id order by count(l.film_id) desc limit 10)

user:

findAll:
select * from public.user
getById:
select t.* from public.user where t.user_id=1

getFriends:
select t1.* from public.user t1 
join friendship f on f.user_from = t1.user_id
where f.user_to=1
union all
select t1.* from public.user t1 
join friendship f on f.user_to = t1.user_id
where f.user_from=1

getCommonFriendsList:
select * from (
select t1.* from public.user t1 
join friendship f on f.user_from = t1.user_id
where f.user_to=3
union
select t1.* from public.user t1 
join friendship f on f.user_to = t1.user_id
where f.user_from=3
) s
where s.user_id in (
select t1.user_id from public.user t1 
join friendship f on f.user_from = t1.user_id
where f.user_to=2
union
select t1.user_id from public.user t1 
join friendship f on f.user_to = t1.user_id
where f.user_from=2
)
