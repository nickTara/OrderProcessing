# Introduction

This repository contains the skeletal structure codebase for Order Processing App.
It consists of several API operations to create customer, place orders and retreive customer data and order details.

## Stack

#### Backend
1. Java 17
2. Maven
3. SpringBoot (3.4.2)
4. MongoDB(7.0)

# Project Structure
This project comprises 4 main packages (below are the links to their READMEs):
* [Controller](src/main/java/com/app/orderprocessing/controller)
* [Models](src/main/java/com/app/orderprocessing/models)
* [Repositories](src/main/java/com/app/orderprocessing/repositories)
* [Service](src/main/java/com/app/orderprocessing/service)
* [OrderProcessingApplication](src/main/java/com/app/orderprocessing/OrderprocessingApplication.java) main funstion to run application.

###  Running the Application Locally
   
Simply run the application from OrderProcessingApplication it will start tomcat server on [http://localhost:8081](http://localhost:8081).
After that you can hit various Endpoints to test functionality of OrderProcessingApp such as
* CreateCustomer(/createCustomer)
* PlaceOrder(/placeOrder)
and many more...




