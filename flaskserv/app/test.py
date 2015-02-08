import json
import csv

def run():
	newJsonData = []
	jsonFile = open('accessible_data.json')
	jsonData = json.load(jsonFile)
	columnF = open('accessible_columns')
	lines = columnF.readlines()
	columns = []
	for line in lines:
		columns.append(line.strip())
	columns = columns[0:-1]
	print columns
	
	csvFile = open('newCSVFile.txt','w')
	for data in jsonData:
		newData = {}
		csvStrings = []
		for column in columns:
			if column in data:
				if column is 'latitude' or column is 'longitude':
					csvStrings.append(str(int(data['location'][column])))
				if type(data[column]) is bool:
					csvStrings.append(str(int(data[column])))
				else:
					csvStrings.append(str((data[column])))				
			else:
				csvStrings.append('')
		csvFile.write(','.join(csvStrings)+'\n')
	csvFile.close()
