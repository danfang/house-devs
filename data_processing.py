import json
import requests
from xml.etree import ElementTree

#this prefix has the private zillow url key
zillow_searchResults_url_prefix = 'http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=X1-ZWz1az08e3q7t7_8mfo0'

def zestForItem(accessible_listings_file):
	f = open(accessible_listings_file)
	jsonData = json.load(f)	
	for listing in jsonData:
		print '-'
		address = listing['street_address']
		address = address.replace(' ', '-')
		zipcode = listing['zip_code']
		urlData = {'address':address,'citystatezip':zipcode, 'rentzestimate': True}		
		# PROCESS SEARCH RESULTS
		resp = requests.post(zillow_searchResults_url_prefix, params=urlData)
		tree = ElementTree.fromstring(resp.content)
		if tree.find('response') is None:
			continue
		results = tree.find('response').find('results')
		if results.find('result') is  None:
			continue
		result = results.find('result')
		if result is None:
			continue
		listing['zestimatecost'] = result.find('zestimate').find('amount').text
		listing['zpid'] =  result.find('zpid').text
		listing['latitude'] = result.find('address').find('latitude').text
         	listing['longitude'] = result.find('address').find('longitude').text
	with open('result.txt', 'w') as resFile:
		json.dump(jsonData, resFile)
		
def getKeysInJson(jsonData):
	setOfKeys = set([])
	for item in jsonData:
		keys = item.keys()
		for key in keys:
			setOfKeys.add(key)
	return setOfKeys
	
