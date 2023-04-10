
drop table public.payment;
CREATE TABLE public.payment (
                                id    BIGINT auto_increment PRIMARY KEY,
                         price DECIMAL(30, 8) NOT NULL,
                        bank_response varchar(255) NOT NULL
);

drop table public.seat;
create table  public.seat  (
                               id    BIGINT auto_increment PRIMARY KEY,
                               flight_id    BIGINT NOT NULL,
                               seat_number varchar(20) NOT NULL,
                               price DECIMAL(30, 8) NOT NULL,
                               customer_id BIGINT
                            ,CONSTRAINT FLIGHTID_SEATNUMBER_CONST unique (flight_id,seat_number)
                             ,CONSTRAINT FLIGHTID_CUSTOMERID_CONST unique (flight_id,customer_id)


);

drop table public.flight;
create table public.flight  (
                                 id    BIGINT auto_increment PRIMARY KEY,
                                 flight_name varchar(20) NOT NULL UNIQUE,
                                 description varchar(50) NOT NULL

);

