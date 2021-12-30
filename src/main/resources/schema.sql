create table if not exists ip_lookup
(
    ip          inet not null,
    location    text,
    lookup_date date
);

alter table ip_lookup
    owner to weather;

create index if not exists ip_lookup_ip_lookup_date_index
    on ip_lookup (ip, lookup_date);

create table if not exists weather_condition
(
    location    text,
    description text,
    lookup_time timestamp
);

alter table weather_condition
    owner to weather;

create index if not exists weather_condition_location_lookup_time_index
    on weather_condition (location, lookup_time);

