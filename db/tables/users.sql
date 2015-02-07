CREATE TABLE users (
    user_id varchar PRIMARY KEY,
    locale varchar NOT NULL,
    first_home boolean,
    category varchar NOT NULL,
    beds integer,
    voucher boolean,
    subsidy boolean,
    income numeric NOT NULL,
    price_weight numeric DEFAULT 1.0,
    amenities_weight numeric,
    education_weight numeric,
    transportation_weight numeric,
    age_range integer REFERENCES ages(age_id),
    CHECK (amenities > 0.0 AND amenities < 100.0),
    CHECK (education > 0.0 AND education < 100.0),
    CHECK (transportation > 0.0 AND transportation < 100.0),
    CHECK (age_range > 0 AND age_range < 2)
);
