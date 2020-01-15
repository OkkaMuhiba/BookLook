# BookLook
E-commerce Book Store

## Getting Started
Default format for api list be like :
### {{Controller Name}}
**{{Method}}**
* Request
```http
GET/POST/PUT/DELETE {link}
Header xxx xxx
```

| Parameter | Type |
| :--- | :--- |
| `api_key` | `xxx` |

* Response 
```metadata json
{
  "message" : xxxx,
  ....
}
```

## Api list

### Authentication
**Sign-up User**
* Request
```http
POST /api/auth/signup
Header Content-Type : applicaton/json
```

| Parameter | Type |
| :--- | :--- |
| `email` | `string` |
| `name` | `string` |
| `numberPhone` | `string` |
| `password` | `string` |
| `username` | `string` |

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

**User Sign-in**
* Request
```http
POST /api/auth/signin
Header Content-Type : application/json
```

| Parameter | Type |
| :--- | :--- |
| `email` | `string` |
| `password` | `string` |

* Response 
```metadata json
{
  "result": "string",
  "status": boolean,
  "tokenType": "string"
}
```

**Admin Sign-in**
* Request
```http
POST /api/auth/admin/signin
Header Content-Type : application/json
```

| Parameter | Type |
| :--- | :--- |
| `email` | `string` |
| `password` | `string` |

* Response 
```metadata json
{
  "result": "string",
  "status": boolean,
  "tokenType": "string"
}
```

### User

**Get User Data**
* Request
```http
GET /api/users
Header Authorization : Bearer [JWT_TOKEN] 
```

* Response 
```metadata json
{
  "userId": "string",
  "name": "string",
  "email": "string",
  "username": "string"
  "numberPhone": "string",
  "userPhoto": "string",
  "readKey": "string",
  "createdAt": timestamp,
  "updatedAt": timestamp
}
```

**Get User Data From userId**
* Request
```http
GET /api/users/{userId}
Header Authorization : Bearer [JWT_TOKEN]
```

| Parameter | Type |
| :--- | :--- |
| `userId` | `string` |

* Response 
```metadata json
{
  "userId": "string",
  "name": "string",
  "email": "string",
  "username": "string"
  "numberPhone": "string",
  "userPhoto": "string",
  "readKey": "string",
  "createdAt": timestamp,
  "updatedAt": timestamp
}
```

**Change User Password**
* Request
```http
PUT /api/users/edit/password
Header Authorization : Bearer [JWT_TOKEN]
```

| Parameter | Type |
| :--- | :--- |
| `oldPassword` | `string` |
| `newPassword` | `string` |

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

**Edit User Profile**
* Request
```http
PUT api/users/edit/profile
Header Authorization : Bearer [JWT_TOKEN]
```

| Parameter | Type |
| :--- | :--- |
| `name` | `xxx` |
| `username` | `xxx` |
| `email` | `xxx` |
| `numberPhone` | `xxx` |

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

**{{Method}}**
* Request
```http
PUT /api/users/edit/photo
Header Content-Type : multipart/form-data
```

| Parameter | Type |
| :--- | :--- |
| `picture` | `picture` |

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

### Market

**Get Authenticated Market Data**
* Request
```http
GET /api/markets
Header Authorization : Bearer [JWT_TOKEN]
```

* Response 
```metadata json
{
  "marketId": "string",
  "userID": "string",
  "marketName": "string",
  "marketBio": "string",
  "marketCode": "string",
  "marketPhoto": "string",
  "totalProduct": long,
  "createdAt": timestamp,
  "updatedAt": timestamp
}
```

**Get Market Data From marketId**
* Request
```http
GET /api/markets/{marketId}
```

* Response 
```metadata json
{
  "marketId": "string",
  "userID": "string",
  "marketName": "string",
  "marketBio": "string",
  "marketCode": "string",
  "marketPhoto": "string",
  "totalProduct": long,
  "createdAt": timestamp,
  "updatedAt": timestamp
}
```

**Create new Market**
* Request
```http
POST /api/markets/create
Header Authorization : Bearer [JWT_TOKEN]
Header Content-Type : application/json
```

| Parameter | Type |
| :--- | :--- |
| `marketName` | `string` |
| `marketBio` | `string` | 

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

**Edit Market Profile**
* Request
```http
PUT /api/markets/edit/profile
Header Authorization : Bearer [JWT_TOKEN]
Header Content-Type : application/json
```

| Parameter | Type |
| :--- | :--- |
| `marketName` | `string` |
| `marketBio` | `string` |

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

**Edit Market Profile**
* Request
```http
PUT /api/markets/edit/profile/photo
Header Authorization : Bearer [JWT_TOKEN]
Header Content-Type : multipart/form-data
```

| Parameter | Type |
| :--- | :--- |
| `picture` | `picture` |

* Response 
```metadata json
{
  "message": "string",
  "success": boolean
}
```

**Check Authenticated-Market's Product From productId**
* Request
```http
GET /api/markets/check-book/{userId}/{key}/{fileName}
Header Authorization : Bearer [JWT_TOKEN]
```

| Parameter | Type |
| :--- | :--- |
| `userId` | `string` |
| `key` | `string` |
| `fileName` | `string` |

* Response 
```metadata json
Multipart Data
```
