# Create the api user
psql -f roles/api.sql

# Create the db
psql -f database.sql

# Create necessary extensions (uuid generator)
psql -f extensions/extensions.sql -d housing

# Create tables
psql -f ages.sql -d housing
psql -f users.sql -d housing
psql -f reject_reasons.sql -d housing
psql -f prop_types.sql -d housing
psql -f recs.sql -d housing
psql -f user_messages.sql -d housing

# Grant permissions
psql -f roles/permissions.sql -d housing
