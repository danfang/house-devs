Refine Housing (Zillow Hack Housing)
==

## Overview

Hi! We're team 10 (The House Devs) and we've created Refine, a decision making engine that utilizes the datasets that we found most impactful.  What Refine does is help those who have vague ideas about what living criteria they require (price, neighborhood, affordability), but are very clear about their personal lives and needs. 

We believe it's easier for users to answer questions about what they prefer and value rather having to filter through huge sets of results over broad criteria. From these questions, we generate a profile and list of suggested living solutions, which they can "reject" or "save" to their profile. Each time they make such a decision, we analyze the reason why they chose to save or reject a property and refine our search results based on several heuristics backed by data sets.

We have built an API service, algorithm engine, and an Android client to present the data.

## Documentation

We have documented our schemas and public APIs, which can be found at [API Reference](api/README.md) and [DB Reference](db/README.md)

## Challenge Areas

We decided to create a decision-making process (profile building) that allows us to explore many different aveneues of issues outlined in the Hack Housing guidelines. 

Specifically during the competition period, we pursued:

1. Assisted living for senior citizens

2. Low-income voucher and subsidy programs.

3. Education and safety optimized results for families

Each branch specified above can be reached via questionaries along the decision tree and each category can have its attributes weighted with a certain priority and preference. Our search algorithm uses these weights as heuristics to decide which houses to recommend.

## Future Goals

We have pretty straightfoward goals ahead of us. 

Primarily, we want to finish up the decision tree to open up data and recommendations formore varied demographics and for cities outside of the City of Seattle. Finding consistent, well-formatted data for each city in itself is challenging, but we believe Socrata, the HUD, and Zillow are making these processes more and more accessible. 

Following this, we want to open up our API to the supply-side of the market - The US Government, landlords, housing developers, public housing services - in order to tap into the wealth of user feedback we receive based on the iterative refinement process we've built. We're imagining queries that can answer questions such as 'What is the most common issue single-parent families face in the home buying process?'We have a lot of the data to answer this question, but making it readily available and queriable would be another top priority.

Additionally, we want to provide resources to users who answer 'I don't know' to many of the questions regarding housing subsidies, vouchers, and assisted living options. We've come up with a messaging system which will send customized notifications targeted to the users who need such information and resources the most.

Finally, we will continue to pursue adding in layers of datasets to facilitiate extremely precise and useful experiences for the customer.

## Team

Daniel Fang (API/DevOps) - Sophomore, UW CSE

Deepak Nettem (Data) - Microsoft, Azure Team

James Lee (Data) - Sophomore, UW

Jay Feng (Data) - Senior, UW CSE

Justin Harjanto (Android) - Sophomore, UW CSE

## Technology Stack

### Datasets & APIs

- HUD Fair Market Rents for city income medians, voucher qualifications
- Socrata Seattle Accessible Housing for senior citizen living options
- Zillow API for Zestiments for mortgages and rents
- Education ranking dataset

### Languages

- Python
  - Flask, pandas

- Javascript
  - Node, express, body-parser, 

- HTML5/CSS/jQuery/Materialize.css

- Java
  - Android SDK

- SQL (PostgreSQL)
