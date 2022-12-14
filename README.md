ER Diagram
https://github.com/alexwti/java-filmorate/blob/main/DBDiag.png

Примеры запросов:
film:

findAll:
select t.* from films t

getById:
select t.* from films t where t.film_id=2

getTopRaitingFilms:
select t.* from films t where t.film_id in
(select l.film_id from film_likes l group by l.film_id order by count(l.film_id) desc limit 10)

user:

findAll:
select * from users
getById:
select t.* from users where t.user_id=1

getFriends:
select t1.* from users t1 
join friendship f on f.user_from = t1.user_id
where f.user_to=1
union all
select t1.* from users t1 
join friendship f on f.user_to = t1.user_id
where f.user_from=1

getCommonFriendsList:
select * from (
select t1.* from users t1 
join friendship f on f.user_from = t1.user_id
where f.user_to=3
union
select t1.* from users t1 
join friendship f on f.user_to = t1.user_id
where f.user_from=3
) s
where s.user_id in (
select t1.user_id from users t1 
join friendship f on f.user_from = t1.user_id
where f.user_to=2
union
select t1.user_id from users t1 
join friendship f on f.user_to = t1.user_id
where f.user_from=2
)
