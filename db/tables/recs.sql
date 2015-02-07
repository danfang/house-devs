CREATE TABLE recs (
    rec_id SERIAL PRIMARY KEY,
    prop_type varchar REFERENCES prop_types(type),
    prop_id integer NOT NULL,
    save boolean,
    reject_reason varchar REFERENCES reject_reasons(reason)
);


