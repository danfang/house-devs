# -*- coding: utf-8 -*-
"""
Created on Sat Feb 07 14:59:57 2015

@author: Jay
"""
import pandas as pd

#user = {'user_id' : 'Jay Feng', 'beds': 2, 'subsidy' : False, 'income': 40000, 
#'price_weight': 1, 'age': 60}
#user = pd.read_json(fileDan)
c = '/home/jfeng/output.json'
global c
#craig = pd.read_json(c)
acc = '/home/jfeng/accessible_data.json'
global acc
fmr = pd.read_csv('/home/jfeng/FMR_Path.csv')
global fmr
#accessible = pd.read_json(acc)

globalUserData = {}
global globalUserData
# key for the dict - userId from the API

#Pass in user json information
#Return apartment json recommendation
#TODO: Adjust for school recommendation
def get_rec(user):
    user = pd.read_json(user)
    rent = get1(user)
    return rent.to_json()

#Pass in user as a pandas dataframe    
def get1(user):
    #check for elderly
    if int(user['age']) > 55:
        #retrieve dataset corresponding to old people
        data = get(user['user_id'], acc)
    else:
        #retrieve dataset for everyone elsess
        data = get(user['user_id'], c)
    #call retrieve function to generate recommendation on specific user
    apartment = retrieve(data, user)
    return apartment

#pass in userId and path to dataset
def get(userId, dataPath):
    #check if userid already registered in product
    if userId not in globalUserData:
        #read in appropriate json if not
        globalUserData[userId] = pd.read_json(dataPath)
    return globalUserData[userId]

#pass in dataset and user parameters
def retrieve(data, user):
    #check for users who need vouchers
    if not user['subsidy']:
        #filter for correct number of beds and price below income level
        apartment = data[(data['beds'] == int(user['beds'])) & (data['price'] < (int(user['income'])/12)*(int(user['price_weight'])/100)*0.3)]
        #assign the nicest neighborhood for the price
        apartment = min(apartment['buyerseller'])
    else:
        #filter for correct number of beds and fair market rate prices given number of beds
        apartment = data[(data['beds'] == int(user['beds'])) & (data['price'] < int(fmr['%s' %int(user['beds'])]))]
        apartment = max(apartment['buyerseller'])
    #Grab the top value in the index 
    apartment = data[data['buyerseller'] == apartment][0:1]
    globalUserData[user['user_id']] = data.drop(apartment.index)
    return apartment

def dropNeighborhood(neighborhood):
    data = pd.read_json(acc)
    data = data[data['neighbhorhood'] != neighborhood]
    return data





#if voucher: sql max FMR value under and select max buyer seller index
# else: sql income*.3 and select min buyer seller index