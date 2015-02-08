Hack Housing Database Reference
==
## Users

| role | password |
| ---- | -------- |
| api  | * |

## Notable Tables

### users

| field name | type | required | description |
| ---------- | ---- | -------- | ----------- |
| user_id | varchar | yes | facebook id |
| locale | varchar | yes | metro area |
| first_home | boolean | no | first time homebuyers |
| category | varchar | yes | single/family/low-income/etc |
| beds | integer | no | preferred number of beds | 
| subsidy | boolean | no |  qualifies for subsidy |
| income | numeric | yes | dollar income value | 
| price_weight | numeric | no | 0.0 - 100.0 |
| amentities_weight | numeric | no | 0.0 - 100.0 |
| education_weight | numeric | no | 0.0 - 100.0 |
| transportation_weight | numeric | no | 0.0 - 100.0 |
| prop_type | string | yes | 'rent' or 'buy' | 
| age_range | integer | no | see ages table (age_id) |

### ages 

| field name | type | description |
| ---------- | ---- | ----------- |
| age_id | integer | age category |
| description | varchar | descrption for this age category |

| age_id | descrption |
| ------ | ---------- |
|   0    | <25 |
|   1    | 26-55 |
|   2    | 55+ |

