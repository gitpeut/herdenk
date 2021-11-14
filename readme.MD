# Herdenk

**Author:** *José Baars*\
**Version:** *1.0*\
**Date** 13 November 2021


###
Contents

 - Introduction
 - Functionality
 - Installation and configuration
   - Postgress
   - Application
 - Testing
 - Deployment
 - Appendix A
   - Endpoints
 - Appendix B
   - Endpoint security configuration


###

## Introduction

This repository is the Spring Boot backend for the Herdenk application, a virtual cemetery.

It supplies a number of rest endpoints that allow a frontend application to add users, graves
and reactions. A great effort has been made to ensure the integrity of the content for each grave.
## Software versions used

  - Java 14
  - Spring Boot 2.5.6
  - Extra dependency: Tika (tika-core, version 2.0.0)
  - Postgress 13

## Functionality

Users can register and login. After successfully registering, a grave can be made and the user becomes
owner of this new grave.

Other users can be authorized to
- view the grave and put flowers or tears next to a grave
- after having asked and received permission from the grave owner
  add a text and/or a photo to the grave.

A grave owner has unlimited access to all reactions to a grave, and can change or delete any
reactions deemed inappropriate, also he can deny access to any user previously granted access.

The site admin has unlimited access to all graves and reactions,and can remove graves that
are deemed inappropriate.

## Installation and configuration

### Postgress

The application uses a Postgress database. An installer for your Operating System can be downloaded
from [The postgress website](https://www.postgresql.org/download/).
A nice installation instruction for Windows can be found [here](https://www.2ndquadrant.com/en/blog/pginstaller-install-postgresql/)

Make sure to note all passwords.
In the postgress installation the postrgress management tool PGadmin is included. Start this tool,
and connect to postgress supplying and noting all required passwords.
Then, create a database named "herdenk", create a user for this database, and grant this user
all access to the herdenk database.


### Application

#### Importing in IDE
This is a Spring Boot project. To import it, first clone this repository to your own computer.
Then import this project in your favourite IDE, for development Intellij was used. In Intellij
this is done by going to File->New->from existing sources, select the pom.xml file. This ensures
that Intellij will import all necessary dependencies for Spring Boot.

#### Application.properties
Then open in your IDE the file ...src/main/resources/application.properties and change
```
  spring.datasource.url=jdbc:postgresql://localhost:5432/herdenk
  spring.datasource.username=<The username you gave full access to in the Postgress instal;lation>
  spring.datasource.password=<The password of this user>
```

Also, decide where the image files uploaded by the users will reside and decide how many hours
a JWT token will be valid.

```
# Herdenk reaction media will be uploaded to this directory. Use UNIX directory
# syntax. On windows / means the root directory of the disk the application is running on.
herdenk.uploads=/herdenk/media

# The number of hours before the JWT token expires and users have to
# login again using their email/password
herdenk.jwtexpiration=480

```
You may also want to change the port the application can be access. In that case, also change
```
server.port=40545
```

Optionally you can limit the size of the files that are uploaded by the users. In the supplied 
application properties this is :
```
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

For testing purposes the database is emptied at each restart. This can be changed by
changing this line:



### Testing

For testing purposes data.sql file is in the src/main/resources directory.
It contains an admin and a few user records all have password "password".

A [postman collection is available in the /src/test directory](src/test/java/herdenk.postman_collection.json).
This can be imported into postman. 

Before using this collection, login first as admin@admin.com, copy the JWT token in the body of the response 
and open the get users query. Paste the JWT token in the Bearer token, 
then select the whole string. Postman will ask to save it as a variable. Do so, and save it as variable adminJWT.
Then do the same for user@user.com, login, copy the JWT token, and save it as variable userJWT  

The application can be tested comfortably in IntelliJ by pressing the run button on top,
or the run button shown after right=clicking on HerdenkApplication.java.

### Deployment

#### Preparation
1. Make sure data.sql only contains an admin user record 
2. Make sure that in application.properties the correct path to the database 
   is entered,if the database will be running on another machine, localhost must be
   replaced by the hostname of that machine:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/herdenk
```
3. Start the application, login as admin
4. Verify the application is working by executing a few requests
5. Change the following lines in application.properties
```
# generate schema dll to create tables
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create
spring.sql.init.mode=always

# database initialization with data.sql after hibernate
spring.jpa.defer-datasource-initialization=true
```
to
```
# generate schema dll to create tables
spring.jpa.generate-ddl=false
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=never

# database initialization with data.sql after hibernate
spring.jpa.defer-datasource-initialization=false
```  
Alternatively, you can remove these lines altogether.
5. Also change
```
spring.jpa.show-sql=true
```
to
```
spring.jpa.show-sql=false
```
6. Delete data.sql
7. Run maven clean and package. 

#### Install and run
Copy herdenk.0.0.1-SNAPSHOT.jar to the installation directory. Start it using the 
appropriate method, depending on the platform.



# Appendix A
## Endpoints

/api/v1/authorities/all:\
get:\
summary: \"GET api/v1/authorities/all\"\
operationId: \"getAuthorities\"\
responses:\
\"200\":\
description: \"OK\"\
\-\--\
/api/v1/authorities/grave/{graveId}:\
get:\
summary: \"GET api/v1/authorities/grave/{graveId}\"\
operationId: \"getAuthoritiesForGrave\"\
parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"

\-\--\
/api/v1/authorities/grave/{graveId}/{userId}/{access}:\
put:\
summary: \"PUT api/v1/authorities/grave/{graveId}/{userId}/{access}\"\
operationId: \"updateAuthority\"\
parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"access\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"string\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  post:\
  summary: \"POST api/v1/authorities/grave/{graveId}/{userId}/{access}\"\
  operationId: \"registerAuthority\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"access\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"string\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/api/v1/authorities/user/{userId}:\
get:\
summary: \"GET api/v1/authorities/user/{userId}\"\
operationId: \"getAuthoritiesForUser\"\
parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/authorities/{userId}/{graveId}:\
  get:\
  summary: \"GET api/v1/authorities/{userId}/{graveId}\"\
  operationId: \"getOneAuthority\"\
  parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  delete:\
  summary: \"DELETE api/v1/authorities/{userId}/{graveId}\"\
  operationId: \"getAuthority\"\
  parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/api/v1/graves:\
post:\
summary: \"POST api/v1/graves\"\
operationId: \"registerGrave\"\
responses:\
\"200\":\
description: \"OK\"\
\-\--

/api/v1/graves/all:\
get:\
summary: \"GET api/v1/graves/all\"\
operationId: \"getGraves\"\
responses:\
\"200\":\
description: \"OK\"\
\-\--

/api/v1/graves/summary:\
get:\
summary: \"GET api/v1/graves/summary\"\
operationId: \"getSummaries\"\
responses:\
\"200\":\
description: \"OK\"\
\-\--\
/api/v1/graves/{graveId}:\
get:\
summary: \"GET api/v1/graves/{graveId}\"\
operationId: \"getGrave\"\
parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  put:\
  summary: \"PUT api/v1/graves/{graveId}\"\
  operationId: \"updateGrave\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  delete:\
  summary: \"DELETE api/v1/graves/{graveId}\"\
  operationId: \"deleteGrave\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/login:\
  post:\
  summary: \"POST api/v1/login\"\
  operationId: \"createAuthenticationToken\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/api/v1/reactions/all:\
get:\
summary: \"GET api/v1/reactions/all\"\
operationId: \"getReactions\"\
responses:\
\"200\":\
description: \"OK\"\
\-\--\
/api/v1/reactions/grave/{graveId}:\
get:\
summary: \"GET api/v1/reactions/grave/{graveId}\"\
operationId: \"getReactionsForGrave\"\
parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  post:\
  summary: \"POST api/v1/reactions/grave/{graveId}\"\
  operationId: \"saveReaction\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/reactions/permission/{graveId}:\
  get:\
  summary: \"GET api/v1/reactions/permission/{graveId}\"\
  operationId: \"getPermission\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/api/v1/reactions/permission/{graveId}/{permission}:\
post:\
summary: \"POST api/v1/reactions/permission/{graveId}/{permission}\"\
operationId: \"askPermission\"\
parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"permission\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"string\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/reactions/token/{graveId}/{token}:\
  get:\
  summary: \"GET api/v1/reactions/token/{graveId}/{token}\"\
  operationId: \"getToken\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"token\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"string\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  post:\
  summary: \"POST api/v1/reactions/token/{graveId}/{token}\"\
  operationId: \"sendToken\"\
  parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
- name: \"token\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"string\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/api/v1/reactions/user/{userId}:\
get:\
summary: \"GET api/v1/reactions/user/{userId}\"\
operationId: \"getReactionsForUser\"\
parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/reactions/{reactionId}:\
  put:\
  summary: \"PUT api/v1/reactions/{reactionId}\"\
  operationId: \"updateReaction\"\
  parameters:\
- name: \"reactionId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  delete:\
  summary: \"DELETE api/v1/reactions/{reactionId}\"\
  operationId: \"deleteReaction\"\
  parameters:\
- name: \"reactionId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/register:\
  post:\
  summary: \"POST api/v1/register\"\
  operationId: \"registerUser\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--\
  /api/v1/users/all:\
  get:\
  summary: \"GET api/v1/users/all\"\
  operationId: \"getUsers\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/api/v1/users/{userId}:\
get:\
summary: \"GET api/v1/users/{userId}\"\
operationId: \"getUser\"\
parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  put:\
  summary: \"PUT api/v1/users/{userId}\"\
  operationId: \"updateUser\"\
  parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  delete:\
  summary: \"DELETE api/v1/users/{userId}\"\
  operationId: \"deleteUser\"\
  parameters:\
- name: \"userId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int64\"\
  responses:\
  \"200\":\
  description: \"OK\"\
  \-\--

/media/{graveId}/{reactionId}/{fileName}:\
get:\
summary: \"GET media/{graveId}/{reactionId}/{fileName}\"\
operationId: \"downloadMedia\"\
parameters:\
- name: \"graveId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int32\"\
- name: \"reactionId\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"number\"\
  format: \"int32\"\
- name: \"fileName\"\
  in: \"path\"\
  required: true\
  schema:\
  type: \"string\"\
  responses:\
  \"200\":\
  description: \"OK\"


# Appendix A
## Endpoint security configuration

**Endpoint security configuration Herdenk V1.0**

All endpoints, except login and register, require a user to be
registered and logged in, i.e. authenticated.\
This allows the application to check authorization at every request
made, as the current user is set by the\
authentication process, encrypted in a JWT token.

Overall design goal is that grave owners have full authority over their
grave, who has access to it and\
reactions posted to the grave can be authorized, removed or even changed
by the grave owner.\
However users should also retain full access to any reactions they
posted.\
\
In case of issues, the site administrator has full access and can
mitigate or fix these issues if needed.

An extra issue encountered was CORS. Browser do a pre-flight test in the
form of an OPTIONS request to an endpoint and check for accessibility.
If this fails, the actual request will not be executed. OPTIONS\
request must be explicitly configured, the disable csrf option in Spring
Boot is not sufficient.

Apart from the standard, self-explanatory access methods like
fullyAuthenticated and permitAll, some authorizations are executed by
special Beans:

**isSelfOrIsAdmin**

returns true if the current user has an ADMIN role or if the current
user wants access to his own data

**hasGraveAccessOrIsAdmin**

return true if the current user has at least READ access or access due
to PUBLIC access to the grave.\
also returns true if the user has role ADMIN

**hasAtLeastOwnerAccess\
**\
returns true is the user has role ADMIN or if the current user is OWNER
of the grave.

**hasAtLeastWriteAccess**

returns true is the user has role ADMIN or if the current user is OWNER
of the grave or the current user\
has been granted WRITE access.

**isGraveOwnerOrAuthor**

returns true if a reaction is written by the current user, or if current
user is OWNER of the grave the reaction applies to, or if the current
user has role ADMIN

Below the simplified security configuration:

.authorizeRequests()

OPTIONS \"/\*\*\") permitAll()

\"/api/v1/authorities\*\*\" fullyAuthenticated()

\"/api/v1/graves\*\*\" fullyAuthenticated()

\"/api/v1/users\*\*\" fullyAuthenticated()

\"/api/v1/reactions\*\*\" fullyAuthenticated()

\"/media\*\*\" fullyAuthenticated()

\"/api/v1/login\" permitAll()

\"/api/v1/register\" permitAll()

GET, \"/api/v1/users/all\" hasRole(" ADMIN\")

DELETE, \"/api/v1/users/{userId}\" hasRole(" ADMIN\")

GET, \"/api/v1/users/{userId}\" access(\"\@AccessBeans.isSelfOrIsAdmin(
userId )\")

PUT, \"/api/v1/users/{userId}\" access(\"\@AccessBeans.isSelfOrIsAdmin(
userId )\")

GET, \"/api/v1/graves/all\" hasRole( " ADMIN\" )

GET, \"/api/v1/graves/summary\" permitAll()

GET, \"/api/v1/graves/{graveId}" access(
\@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )\")

PUT, \"/api/v1/graves/{graveId}" access(
\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

DELETE,\"/api/v1/graves/{graveId}" access(
\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

GET, \"/api/v1/authorities/all\"hasRole(" ADMIN\")

GET, \"/api/v1/authorities/user/{userId}"
access(\"\@AccessBeans.isSelfOrIsAdmin( #userId )\")

GET, \"/api/v1/authorities/grave/{graveId}"
access(\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

DELETE,\"/api/v1/authorities/{UserId}/{graveId}"
access(\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

POST, \"/api/v1/authorities/grave/{graveId}/\*\*"
access(\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

PUT, \"/api/v1/authorities/grave/{graveId}/\*\*"
access(\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

GET, \"/api/v1/reactions/all\"hasRole(" ADMIN\")

GET, \"/api/v1/reactions/grave/{graveId}"
access(\"\@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )\")

GET, \"/api/v1/reactions/user/{userId}"
access(\"\@AccessBeans.isSelfOrIsAdmin( #userId )\")

POST, \"/api/v1/reactions/grave/{graveId}"
access(\"\@AccessBeans.hasAtLeastWriteAccess( #graveId )\")

PUT, \"/api/v1/reactions/{reactionId}"
access(\"\@AccessBeans.isGraveOwnerOrAuthor( #reactionId )\")

GET, \"/api/v1/reactions/permission/{graveId}"
access(\"\@AccessBeans.hasAtLeastOwnerAccess( #graveId )\")

POST, \"/api/v1/reactions/permission/{graveId}/{permission}\"permitAll()

GET, \"/api/v1/reactions/token/{graveId}/{token}"
access(\"\@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )\")

POST, \"/api/v1/reactions/token/{graveId}/{token}"
access(\"\@AccessBeans.hasGraveAccessOrIsAdmin( #graveId )\")

DELETE,\"/api/v1/reactions/{reactionId}"
access(\"\@AccessBeans.isGraveOwnerOrAuthor( #reactionId )\")

\"/media/{graveId}/\*\*" access(\"\@AccessBeans.hasGraveAccessOrIsAdmin(
graveId )\")

.anyRequest(denyAll()

