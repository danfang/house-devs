# Create the api user
psql -f roles/api.sql

# Create the db
psql -f database.sql

# Create necessary extensions (uuid generator)
psql -f extensions/extensions.sql -d housing

# Create tables
psql -f tables/ages.sql -d housing
psql -f tables/prop_types.sql -d housing
psql -f tables/reject_reasons.sql -d housing
psql -f tables/prop_rent.sql -d housing
psql -f tables/users.sql -d housing
psql -f tables/recs.sql -d housing
psql -f tables/user_messages.sql -d housing
psql -f tables/prop_accessible.sql -d housing

# Grant permissions
psql -f roles/permissions.sql -d housing

psql -f data/populate.sql -d housing
