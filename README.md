# GuestBook
Guest book and feedback system for events.

# Project Structure
The project follows the standard Maven directory layout for Web applications (http://bit.ly/1Mof9DC)

All **Java source code** will be contained in `src/main/java/com/cis498/group4/`. Controller servlets will be placed in `com.cis498.group4.controllers` and model objects will be placed in `com.cis498.group4.models`.

The web root is `src/webapp/`. All **JSP, HTML, CSS, images, Javascript, etc.** will go in this folder and its subdirectories (e.g. `/img`, `/scripts`, etc.). JSP views should go in `/views`. Partial JSP views (e.g. `_header.jsp`) should go in `/templates`.

# Deploying to running Tomcat Server with Apache Maven
**(SEE: http://bit.ly/2p0YeQh)**

This is useful for deploying to a running Tomcat instance for development and testing.

## Requirements
- Apache Maven (3.3.9)
- Apache Tomcat (8.5.13)

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
3. Start Tomcat if it is not already running
4. In the project root directory, execute: `mvn tomcat7:deploy`, or `mvn tomcat7:redeploy` if you have deployed the project on your server previously.
5. Browse to http://{hostname:port}/GuestBook to access the app

# Deploying to Tomcat server with Apache Maven and WAR

This is useful for building a Web Archive (WAR) for final deployment to a Servlet container.

## Requirements
- Apache Maven (3.3.9)
- Apache Tomcat (8.5.13)

## Deploying
1. Clone or download and unzip the repository to the directory of your choice (the "project root")
2. In the project root directory, execute: `mvn package`
3. The WAR file will be built in `{Project Root}/target`
4. Stop Tomcat
5. Copy GuestBook.war to your Tomcat webapps directory
6. Start Tomcat and navigate to to http://{hostname:port}/GuestBook to access the app

Or deploy via the Tomcat web manager gui: http://{hostname:port}/manager/html
