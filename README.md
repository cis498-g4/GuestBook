# GuestBook
Guest book and feedback system for events.

# Project Structure
The project follows the standard Maven directory layout for Web applications (http://bit.ly/1Mof9DC)

All **Java source code** will be contained in `src/main/java/com/cis498/group4/`. Controller servlets will be placed in `com.cis498.group4.controllers` and model objects will be placed in `com.cis498.group4.models`. Utility classes (e.g. database connection utilities) go in `com.cis498.group4.util`. Data access objects (DAO) classes, which are used to perform database operations, go in `com.cis498.group4.data`. See http://bit.ly/2nTYlvM

The web root is `src/webapp/`. All **JSP, HTML, CSS, images, Javascript, etc.** will go in this folder and its subdirectories (e.g. `/img`, `/scripts`, etc.). JSP views should go in `/views`. Partial JSP views (e.g. `_header.jsp`) should go in `/templates`.

# MySQL database setup

**IMPORTANT:** The database connection properties _must_ be configured before building and deploying the application.

## Requirements
- MySQL (5.6)

## Database schema setup
1. This assumes you have a running MySQL instance and the ability to connect to it with full privileges.
2. 

## Connecting the application to the database
1. In the source code, locate the file `/src/main/resources/db.properties`. Edit this file to configure the URL, username, and password used to connect to your MySQL instance. You should not need to change the "driver" property.
2. Save the file, then build and deploy the application (See **Deploying to Tomcat server**, below)

# Deploying to running Tomcat server with Apache Maven
**(SEE: http://bit.ly/2p0YeQh)**

This is useful for deploying to a running Tomcat instance for development and testing.

## Requirements
- Java (1.8)
- Apache Maven (3.3.9)
- Apache Tomcat (8.5)

## Environment Setup
1. Create Tomcat user with admin privileges in `{TOMCAT_HOME}/conf/tomcat-users.xml`:
```
<role rolename="manager"/>
<role rolename="admin"/>
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="{Username}" 
      password="{Password}" 
      roles="manager,admin,manager-gui,manager-script"/>
```
2. Add server info to Maven `{M2_HOME}/libexec/conf/settings.xml`, inside the `<servers>` tag:
```
<server>
  <id>{ServerName}</id>
  <username>{Username}</username>
  <password>{Password}</password>
</server>
```

## Deploying
1. Clone or download and unzip the repository to the directory of your choice (the "project root")
2. Update server info in pom.xml so that the `<url>` tag in the Tomcat plugin matches your hostname and port, and the `<server>` tag matches the `{ServerName}` you added to Maven in Environment Setup, above.
3. Configure your MySQL database connection properties in `/src/main/java/db.properties` (See **MySQL database setup**, above).
4. Start Tomcat if it is not already running
5. In the project root directory, execute: `mvn tomcat7:deploy`, or `mvn tomcat7:redeploy` if you have deployed the project on your server previously.
6. Browse to http://{hostname:port}/GuestBook to access the app

# Deploying to Tomcat server with Apache Maven and WAR

This is useful for building a Web Archive (WAR) for final deployment to a Servlet container.

## Requirements
- Java  (1.8)
- Apache Maven (3.3.9)
- Apache Tomcat (8.5.12) _This does not work with 8.5.13_

## Deploying
1. Clone or download and unzip the repository to the directory of your choice (the "project root")
2. Configure your MySQL database connection properties in `/src/main/java/db.properties` (See **MySQL database setup**, above).
3. In the project root directory, execute: `mvn package`
4. The WAR file will be built in `{Project Root}/target`
5. Stop Tomcat
6. Copy GuestBook.war to your Tomcat webapps directory
7. Start Tomcat and navigate to to http://{hostname:port}/GuestBook to access the app

Or deploy via the Tomcat web manager gui: **http://{hostname:port}/manager/html**
