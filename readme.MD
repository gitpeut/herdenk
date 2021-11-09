# herdenk

This repository is the Spring Boot backend for the Herdenk application, a virtual cemetery.

It supplies a number of rest endpoints that allow a frontend application to add users, graves
and reactions. A great effort has been made to ensure the integrity of the content for each grave.
## Software versions used

Java 14
Spring Boot 2.5.6
  - Extra dependency: Tika (tika-core, version 2.0.0)
Postgress 13

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

see [Herdenk-Api-Endpoints.pdf](Herdenk-api-endpoints.pdf)

#Appendix B
## Endpoint authorisation

see [End point authorisation](Endpoint%20security%20configuration%20Herdenk%20V1.1.pdf)

