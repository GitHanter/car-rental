# Introduction

## What I've implemented ?
1. The project based on springboot and Gradle
2. 4 APIs:
    - Query available cars of a date range
    - Book a car on a date range
    - Query a customer's orders
    - Cancel order
3. Car price is stored by date, so a car can be reserved by a different customer at different date range
4. Use **redis distribute lock** to lock `car` when order a car of a date range; and to lock `order` when cancel a order
5. Kubernetes resource files used to deploy mysql, redis, and this app to **GKE**

## Database design
![Tables](images/schema.png)

## Architecture (Deployed on GKE)
![App on GKE Kubernetes architecture](images/architecture.png)

# API
### Response fields
```json
{
  "errorMessage": null,
  "data": true,
  "error": true
}
```
|  Field |Description |
|  ---- |----  |
| errorMessage | Null when request is success; Detail error message when request failed |
| data | Null when request is failed; Response data when request is success |
| error| `true` or `false`. `true` when request is success; `false` when request is failed|

### 1.[**getAccessToken**] Get access token using OAuth2 `password` flow
**This flow is not recommend on production environment**
```http
###
# @name getAccessToken
POST {{baseUrl}}/oauth/token
Authorization: Basic {{clientId}}:{{clientSecret}}
Content-Type: application/x-www-form-urlencoded

username={{customerName}}&password={{customerPassword}}&grant_type=password&scope=read,write
```
- URL: `/oauth/token`
- Headers
    - Authorization: `Basic` base64 encoded `clientId:clientSecret`
    - Content-Type: application/x-www-form-urlencoded

- Parameters:

|  Param   | Required |Description |
|  ----  | ----  |----  |
| username  | Y |The customer username of the system |
| password  | Y |The customer's login password |
| grant_type  | Y |OAuth2 grant type, e.g `password` |
| scope  | Y |`read,write` |

- Response
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDM2NTIwMjYsInVzZXJfbmFtZSI6ImN1c3RvbWVyQiIsImp0aSI6ImpTeWxHZFUzYThxWm0wM052SDV0MStnUkxMYz0iLCJjbGllbnRfaWQiOiIwYjUwMDUyNC04ZmRmLTQyY2YtOWE1NS1kMWQwNjlhODU1MTciLCJzY29wZSI6WyJyZWFkLHdyaXRlIl19.UAU4VIJVRny3i4hpoB27OIkM5_v5XTVxps2yBrLNtLo",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDYyMDA4MjYsInVzZXJfbmFtZSI6ImN1c3RvbWVyQiIsImp0aSI6IkEvVnEzUmZIeHhPSCtRbVNkODNBSlAwNGllaz0iLCJjbGllbnRfaWQiOiIwYjUwMDUyNC04ZmRmLTQyY2YtOWE1NS1kMWQwNjlhODU1MTciLCJzY29wZSI6WyJyZWFkLHdyaXRlIl0sImF0aSI6ImpTeWxHZFUzYThxWm0wM052SDV0MStnUkxMYz0ifQ.ZO-XCG1N-Jo5cIqRTkFxLKWOjsiXkbssSBVpnhmDwOE",
  "expires_in": 43199,
  "scope": "read,write",
  "jti": "jSylGdU3a8qZm03NvH5t1+gRLLc="
}
```

### 2.[**getAvailableCars**]  Get available cars on the selected date range 
```http
###
# @name getAvailableCars
@accessToken = {{getAccessToken.response.body.access_token}}
GET {{baseUrl}}/api/getAvailableCars?startDate={{pickUpDate}}&endDate={{returnDate}}
Authorization: Bearer {{accessToken}}
```
- URL: `/api/getAvailableCars`
- Headers
    - Authorization: `Bearer` the `JWT access_token` from `getAccessToken` API response

- Parameters:

|  Param   | Required |Description |
|  ----  | ----  |----  |
| startDate  | Y |The start date you want to book a car. Must equal or before `endDate`. Format is `YYYY-MM-DD` |
| endDate  | Y |The date when you return the car. Must equal or after `startDate`. Format is `YYYY-MM-DD` |

- Response
```json
{
  "errorMessage": null,
  "data": [
    {
      "id": 4,
      "carBrand": "Toyota",
      "carModel": "Toyota Camry",
      "carType": "ECAR",
      "color": "GRAY",
      "capacity": 5,
      "plateNumber": "YB 003AA",
      "mileage": 8000,
      "dailyPriceOfRange": 260.5,
      "selectedDays": 22
    }
  ],
  "error": false
}
```

### 3.[**bookCar**]  Book a car by CarId
```http
###
# @name bookCar
POST {{baseUrl}}/api/book
Authorization: Bearer {{accessToken}}
Content-type: application/json

{
    "carId": {{getAvailableCars.response.body.data[0].id}},
    "pickUpDate":"{{pickUpDate}}",
    "pickUpTime":"12:00:00",
    "returnDate":"{{returnDate}}",
    "returnTime":"12:00:00"
}
```
- URL: `/api/book`
- Headers
    - Authorization: `Bearer` the `JWT access_token` from `getAccessToken` API response
    - Content-type: `application/json`
    
- Request payload:

|  Param   | Required |Description |
|  ----  | ----  |----  |
| carId | Y | The `carId` queried from `getAvailableCars` API response |
| pickUpDate  | Y |The start date you want to book a car. Must equal or before `returnDate`. Format is `YYYY-MM-DD` |
| pickUpTime | Y | Time of the day. Format is `HH:mm:ss` |
| returnDate  | Y |The date when you return the car. Must equal or after `pickUpDate`. Format is `YYYY-MM-DD` |
| returnTime | Y | Time of the day. Format is `HH:mm:ss` |

- Response
```json
{
  "errorMessage": null,
  "data": 3,
  "error": false
}
```
This is a **Idempotent** API, If we submit same request multiple times, we'll get following respons
```json
{
  "errorMessage": "Car(1,BMW,BMW 650,LCAR) not available at the range: 2020-11-04,2020-11-25",
  "data": null,
  "error": true
}
```

### 4.[**getOrders**]  Get user's orders
```http
###
# @name getOrders
GET {{baseUrl}}/api/getOrders
Authorization: Bearer {{accessToken}}
```
- URL: `/api/getOrders`
- Headers
    - Authorization: `Bearer` the `JWT access_token` from `getAccessToken` API response
    - Content-type: `application/json`

- Response
```json
{
  "errorMessage": null,
  "data": [
    {
      "id": 1,
      "orderedCar": {
        "id": 1,
        "carBrand": "BMW",
        "carModel": "BMW 650",
        "carType": "LCAR",
        "color": "WHITE",
        "capacity": 5,
        "plateNumber": "YB 000AA",
        "mileage": 5000,
        "dailyPriceOfRange": 331.0,
        "selectedDays": 1
      },
      "reservedDate": "2020-10-25T04:16:00",
      "pickUpDate": "2020-11-25",
      "pickUpTime": "12:00:00",
      "returnDate": "2020-11-25",
      "returnTime": "12:00:00",
      "totalAmount": 331.0,
      "reserveStatus": "CANCELLED"
    },
    {
      "id": 2,
      "orderedCar": {
        "id": 1,
        "carBrand": "BMW",
        "carModel": "BMW 650",
        "carType": "LCAR",
        "color": "WHITE",
        "capacity": 5,
        "plateNumber": "YB 000AA",
        "mileage": 5000,
        "dailyPriceOfRange": 320.5,
        "selectedDays": 22
      },
      "reservedDate": "2020-10-25T04:42:00",
      "pickUpDate": "2020-11-04",
      "pickUpTime": "12:00:00",
      "returnDate": "2020-11-25",
      "returnTime": "12:00:00",
      "totalAmount": 7051.0,
      "reserveStatus": "CONFIRMED"
    }
  ],
  "error": false
}
```

### 5.[**cancelOrder**] Cancel order
```http
###
# @name cancelOrder
DELETE {{baseUrl}}/api/cancel/{{getOrders.response.body.data[0].id}}
Authorization: Bearer {{accessToken}}
```
- URL: `/api/cancel/{orderId}`
- Headers
    - Authorization: `Bearer` the `JWT access_token` from `getAccessToken` API response
    - Content-type: `application/json`
    
- Path variable: `orderId`

|  Param   | Required |Description |
|  ----  | ----  |----  |
| orderId | Y | The `orderId` queried from `getOrders` API response |

- Response
```json
{
  "errorMessage": null,
  "data": true,
  "error": true
}
```

# Deployment (GKE)
## Build application container
We use docker [multi-stage](https://codefresh.io/docs/docs/learn-by-example/java/gradle/) build feature to simplify our build process
### Clone code and build
```sh
git clone https://github.com/GitHanter/car-rental.git

export PROJECT_ID=<YOUR_GCP_PROJECT_ID>

docker build -t gcr.io/${PROJECT_ID}/car-rental-app:latest .
```

### Examine build result
```sh
docker images
```

### Push to registry
```sh
docker push gcr.io/${PROJECT_ID}/car-rental-app:latest
```

## Create kubernetes resources
### Create mysql ConfigMap
The ConfigMap contains database init SQLs which will be mount to `/docker-entrypoint-initdb.d`
```sh
kubectl apply -f mysql-db-config.yml
```

### Create mysql kubernetes resources
* A Secret with mysql database password
* A PersistentVolumeClaim
* A Deployment use [mysql8 docker image](https://hub.docker.com/_/mysql/) with one replica
* A ClusterIP Service
```sh
kubectl apply -f mysql.yml
```

### Create redis kubernetes resources
* A Deployment for single instance redis with one replica
* A ClusterIP Service
```sh
kubectl apply -f redis.yml
```

## Create car-rental application kubernetes resources
### Create Secret with OAuth2 configuration
```sh
kubectl apply -f car-rental-secret.yml
```

### Create Deployment with 3 replicas
We use `envsubst` to substitute the environment variable in resource file ([Ref](https://stackoverflow.com/questions/48296082/how-to-set-dynamic-values-with-kubernetes-yaml-file))
```sh
envsubst < car-rental-deployment.yml | kubectl apply -f -
``` 

### Create a LoadBalancer type Service to expose our service to internet
```sh
kubectl apply -f car-rental-service.yml
```

## Examine kubernetes resources
```sh
kubectl get pods -o wide

kubectl get deployment

kubectl get service
```

# API test
We use Visual Studio Code [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) extension to send the HTTP requests.
Define following variable in [api.http](api.http) file to do the test
- `@baseUrl`: e.g `http://localhost:8080/car-rental`
- `@clientId`: the OAuth2 clientId (`oauth2ClientId`) you defined in the [car-rental-secret.yml](kubernetes/car-rental-secret.yml)
- `@clientSecret`: the OAuth2 clientSecret (`oauth2Secret`) you defined in the [car-rental-secret.yml](kubernetes/car-rental-secret.yml)
- `@customerName`: the username on `USERS` table
- `@customerPassword`: the password of the user

Example:
![API test example](images/api-request-example.gif)

