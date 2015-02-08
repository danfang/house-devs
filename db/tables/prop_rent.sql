CREATE TABLE prop_rent (
	CraigID bigint PRIMARY KEY,
	price integer,
	link varchar,
	longitude double precision,
	baths real,
	size integer,
	area varchar,
	title varchar,
	coord varchar,
	beds integer,
	zipcode char(5),
	buyerseller double precision
);
COPY prop_rent FROM '/home/jfeng/output.csv' DELIMITER ',' CSV;
