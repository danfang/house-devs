Internal API Documentation
==

## Client Calls

### GET /user/:userId

Response

- HTTP 404 if user does not exist

- HTTP 200 if user exists

### POST /user/:userId

Create a new user with basic preferences filled out

Request

| name | type | required |           
| ---- | ---- | -------- |           
| locale | string | yes |          
| first_home | boolean| yes |   
| category | string | yes |     
| beds | integer | yes |     
| voucher | boolean | yes |     
| subsidy | boolean | yes |     
| income | integer | yes |     
| price_weight | float  | yes |     
| amenities_weight | float  | yes |     
| education_weight | float  | yes |     
| transportation_weight | float | yes |     
| prop_type | string | yes |     
| age_range | integer | yes |     

Response

| name | type |
| ---- | ---- |
| success | boolean |

### GET /saved/:userId | GET /rejected/:userId | GET /history/:userId

Get the list of saved, rejected, and all houses for this user

Response

| name | type |
| ---- | ---- |
| latitude | float |
| longitude | float |
| address | string |
| zipcode | int |
| beds | int |
| price | int |
| type | string |

### GET /rec/:userId

Fetch a new recommendation for this user

Response

| name | type | 
| ---- | ---- |
| rec_id | int |
| latitude | float |
| longitude | float |
| address | string |
| zipcode | int |
| beds | int |
| price | int |
| type | string |
| <other_attribute> | object |

### PUT /rec/:recId

Update a recommendation for this user

Request

| name | type |
| ---- | ---- |
| rec_id | int |
| save | boolean |
| reject_reason | string |

Response 

| name | type |
| ---- | ---- |
| success | boolean |
