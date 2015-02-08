CREATE TABLE recs (
    rec_id SERIAL PRIMARY KEY,
    user_id varchar REFERENCES users(user_id),
    save boolean,
    area varchar,
    city varchar,
    beds integer,
    latitude numeric,
    longitude numeric,
    zipcode integer,
    price integer,
    buyerseller numeric, 
    type varchar,
    reject_reason varchar REFERENCES reject_reasons(reason)
);
