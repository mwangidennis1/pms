# MAYA
This is a parcel management system that automates parcel delivery services and solves some challenges in operational efficiencies

This was done as part of my SWE 3090 course : software engineering project 1

## Features
* automated notification system
* Dynamic pricing model
* Estimated Time of Arrival (ETA) prediction
* automated parcel tracking

## Installation
prerequisites:
* local maven installation
* JDK 17 +

optional :
* Africa's-Talking credentials
``` 
https://github.com/mwangidennis1/pms.git
cd pms
mvn clean install
```
## Quickstart
```
mvn spring-boot:run
```

## configuration
To set up the Africa`s Talking sms configuration visit here for more information :
https://developers.africastalking.com/

To set up the mail services:
https://stackoverflow.com/questions/41351540/configure-smtp-host-using-yaml-file-in-spring-boot

## testing
To run tests
```
mvn  test
```


