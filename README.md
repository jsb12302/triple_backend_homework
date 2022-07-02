## ERD
![triple-erd drawio](https://user-images.githubusercontent.com/73890228/176977482-1f86c277-d6c7-4c81-86b3-55695c4ad872.png)

## DDL
### Account
```
create table account (
                         account_id varchar(255) not null,
                         primary key (account_id)
);
create index i_account on account (account_id);
```

### Review
```
create table review (
                        review_id varchar(255) not null,
                        content varchar(255),
                        create_time datetime(6),
                        place_id varchar(255),
                        account_id varchar(255),
                        primary key (review_id),
                        foreign key (account_id)
                            references account (account_id)
);
create index i_placeId on review (place_id);
```

### Photos
```
create table photos (
                        photo_id varchar(255) not null,
                        review_id varchar(255),
                        primary key (photo_id),
                        foreign key (review_id)
                            references review (review_id)
);
create index i_photos on photos (photo_id);
```

### Point
```
create table point (
                       id bigint not null AUTO_INCREMENT,
                       point integer not null,
                       account_id varchar(255),
                       primary key (id),
                       foreign key (account_id)
                           references account (account_id)
);
create index i_account on point (account_id);
```

### PointHistory
```
create table point_history (
                               id bigint not null AUTO_INCREMENT,
                               amount bigint not null,
                               content varchar(255),
                               current_point integer not null,
                               mode varchar(255),
                               review_id varchar(255),
                               time datetime(6),
                               account_id varchar(255),
                               primary key (id),
                               foreign key (account_id)
                                references account (account_id)
);
```

### ReviewHistory
```

create table review_history (
                                id bigint not null AUTO_INCREMENT,
                                content integer not null,
                                first_post integer not null,
                                picture integer not null,
                                review_id varchar(255),
                                primary key (id)
);
create index i_reviewId on review_history (review_id);
```
## 실행방법
1. ./gradlew bootJar
2. ./gradlew bootRun -Pargs=--userName='DB 유저네임',--password='DB 비밀번호'
Ex) ./gradlew bootRun -Pargs=--userName=root,--password=1234

