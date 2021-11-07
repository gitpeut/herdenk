# herdenk

This reository is the Spring Boot backend for the Herdenk application, a virtual cemetery.

It supplies a number of rest endpoints that allow a frontend application to add users, graves
and reactions. A great effort has been made to ensure the inregrity of the content for each grave.

##Functionality

Users can register and login. After succesfully registering, a grave can be made and the user becomes 
owner of this new grave.

Other users can be authorized to 
- view the grave and put flowers or tears next to a grave
- after having asked and received permission from the grave owner
  add a text and/or a photo to the grave.

A grave owner has unlimited access to all reactions to a grave, and can change or delete any
reactions deemed inappropriate. Also he can deny access to any user previously granted access.

The site admin has unlimited access to all graves and reactions,and can remove graves that
are deemed inappropriate.

##Installation

###Postgress
The application uses a Postgress database. An installer for your Operating System can be downloaded 
from [The postgress website](https://www.postgresql.org/download/).
A nice installation instruction for Windows can be found [here](https://www.2ndquadrant.com/en/blog/pginstaller-install-postgresql/)

Make sure to note all passwords.
In the postgress installation the postrgress management tool PGadmin is included. Start this tool,
and connect to postgress supplying and noting all requried passwords.
Then, create a database named "herdenk", create a user for this database, and grant this user
all access to the herdenk database. 


###Application

#### Importing in IDE
This is a Spring Boot project. To import it, first clone this repository to your own computer.
Then import this project in your favourite IDE, for developement Intellij was used. In Intellij
this is done by going to File->New->from existing sources, select the pom.xml file. This ensures
that Intellij will import all necessary dependencies for Spring Boot.

#### Appplication.properties
Then open in your IDE the file ...src/main/resources/application.properties and change 

  spring.datasource.url=jdbc:postgresql://localhost:5432/herdenk
  spring.datasource.username=<The username you gave full access to in the Postgress instal;lation>
  spring.datasource.password=<The password of this user>

Also, decide where the image files uploaded by the users will reside by changing this variable

  # Herdenk reaction media will be uploaded to this directory. Use UNIX directory
  # syntax. On windows / means the root directory of the disk the application is running on.
  herdenk.uploads=/herdenk/media

## Testing

A postman collection is available to test 

## Deployment


  

