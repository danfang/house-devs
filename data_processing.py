import json
import requests

#this prefix has the private zillow url key
zillow_url_prefix = 'http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=X1-ZWz1az17wnr7d7_7x5fi'

def getZestimateForListing(accessible_listings_file):
	f = open(accessible_listings_file)
	jsonData = json.load(f)
	
	for listing in jsonData:
		address = listing['street_address']
		address = address.replace(' ', '-')
		zipcode = listing['zip_code']
		urlData = json.dumps({'address':address,'citystatezip':zipcode})		
		response = requests.post(zillow_url_prefix, urlData)
		print response
		break
	
	
