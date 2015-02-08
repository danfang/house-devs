# -*- coding: utf-8 -*-
"""
Created on Sat Feb 07 14:59:57 2015

@author: Jay
"""
import pandas as pd

'''
user = {'user_id' : 'Jay Feng', 'beds': 2, 'subsidy' : False, 'income': 40000, 
'price_weight': 100, 'age': 50}
user1 = {u'category': u'Single Professional', u'subsidy': False, u'age_range': 
2, u'user_id': u'10200118760568302', u'price_weight': u'1', u'locale': u'Vhvh', 
u'first_home': True, u'prop_type': u'rent', u'education_weight': u'0', u'beds': 
    3, u'voucher': False, u'income': u'70001', u'amenities_weight': u'0', u'transportation_weight': u'3'}
user2 = {u'category': u'Single Professional', u'subsidy': False, u'age_range': 
2, u'user_id': u'10200118760568302', u'price_weight': u'.9', u'locale': u'Vhvh', 
u'first_home': True, u'prop_type': u'rent', u'education_weight': u'0', u'beds': 
    3, u'voucher': False, u'income': u'70001', u'amenities_weight': u'0', u'transportation_weight': u'3'}
user3 = {u'category': u'Single Professional', u'subsidy': False, u'age_range': 
2, u'user_id': u'10200118760568303', u'price_weight': u'1.2', u'locale': u'Vhvh', 
u'first_home': True, u'prop_type': u'rent', u'education_weight': u'0', u'beds': 
    3, u'voucher': False, u'income': u'70001', u'amenities_weight': u'0', u'transportation_weight': u'3'}
'''
#user = pd.read_json(fileDan)
#user1 = pd.DataFrame(user, index=[1])
#c = '/home/jfeng/output.json'
#c = 'C:/Users/Jay/Documents/output.json'
#global c
#craig = pd.read_json(c)
dataset = '/home/jfeng/dataset.json'
#dataset = 'C:/Users/Jay/Documents/dataset.json'
global dataset
fmr = pd.read_csv('/home/jfeng/FMR_Path.csv')
#fmr = pd.read_csv('C:/Users/Jay/Documents/FMR_Path.csv')
global fmr
#accessible = pd.read_json(acc)

# key for the dict - userId from the API

#Pass in user json information
#Return apartment json recommendation
#TODO: Adjust for school recommendation
def get_rec(user):
    global dataset
    user = pd.DataFrame(user, index=[1])
    rent = get1(user)
    return rent.to_json()

#Pass in user as a pandas dataframe    
def get1(user):
    global dataset
    #check for elderly
    #retrieve dataset corresponding to old people
    data = get(user['user_id'].item(), dataset)
    if int(user['age_range']) > 1:
        data = data[data['type'] == 'accessible']
    else:
        data = data[data['type'] == 'regular']
    apartment = retrieve(data, user)    
    return apartment

#pass in userId and path to dataset
def get(userId, dataPath):
    #check if userid already registered in product
    if userId not in globalUserData:
        #read in appropriate json if not
        global globalUserData
        globalUserData[userId] = pd.read_json(dataPath)
    return globalUserData[userId]

#pass in dataset and user parameters
def retrieve(data, user):
    global globalUserData
    global fmr
    #check for users who need vouchers
    if not user['subsidy'].item():
        #filter for correct number of beds and price below income level
        data = data[(data['beds'] == int(user['beds']))]
        apartment = data[data['price'] < (int(user['income'])/12)*float(user['price_weight'])*0.3]
        #assign the nicest neighborhood for the price
        if len(apartment) < 1:
            return NaN
        apartment = min(apartment['buyerseller'])
    else:
        #filter for correct number of beds and fair market rate prices given number of beds
        apartment = data[(data['beds'] == int(user['beds'])) & (data['price'] < int(fmr['%s' %int(user['beds'])]))]
        if len(apartment) < 1:
            return NaN
        apartment = max(apartment['buyerseller'])
    #Grab the top value in the index 
    apartment = data[data['buyerseller'] == apartment][0:1]
    globalUserData[user['user_id'].item()] = data.drop(apartment.index)
    return apartment

def dropNeighborhood(neighborhood):
    data = pd.read_json(acc)
    data = data[data['neighbhorhood'] != neighborhood]
    return data





#if voucher: sql max FMR value under and select max buyer seller index
# else: sql income*.3 and select min buyer seller index