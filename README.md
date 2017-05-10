# GuestBook
Guest book and feedback system for events.

# Overview

This application is a web-based guestbook. Organizers of an event launch the app in “kiosk mode” on computers set up at the entrance to an event. Event attendees track their attendance at the event by logging in to the kiosk with their email on their way in.

This system can also be used to facilitate feedback. Attendees could be asked to log in to the system again following the event to fill out a short survey, which would be recorded for the presenters to review later.

# Usage

This application consists of three separate modules: the **Sign-In Kiosk**, the **Organizer Console**, and the **Guest Console**. The console modules may be accessed at http://[hostname]:8080/GuestBook/manager. The contents of the console will depend on whether the user logging in is an Event Organizer or a Guest. The Sign-In Kiosk for an event can be launched from the Event Organizer Console.

## Event Organizer Console

The Event Organizer Console allows you to perform the following functions:

- List all events, with the ability to sort or filter by event name, date, or organizer
- View a single event’s details
- Edit a single event’s details (if the event occurs in the future)
- Delete an event
- Create a new event
- List all users, with the ability to sort or filter by name, email, or user type
- View a single user’s details
- Edit a single user’s details
- Delete a user
- Create a new user
- Register guests for an event
- Register multiple guests simultaneously by uploading a CSV
    - *Please note that, in this demo, guests who do not already exist will be created with a default password of 'abc123'*
- De-register guests from an event
- View responses to post-event surveys, with the ability to sort or filter by event, date, or organizer
- Export all survey responses in current view to a file
- View a single survey response's details, or export its contents to a file
- Start the sign-in kiosk for an event

## Guest Console

Guests can use the console to perform the following functions.

- List all events for which he/she is/was registered, with the ability to sort and filter
- View a single event’s details
- Register for an event with a special registration code
- De-register from an event
- List surveys available for events he/she attended in the past
- Complete a survey for an event that he/she attended, which may be required in order to record attendance

## Sign-In Kiosk

The sign-in kiosk, which is launched for a specific event from the Event Organizer Console, allows users to sign in to that event.
The kiosk will check for the user's registration status for the event, and sign them in appropriately.
If a survey is required for the event, the guest will receive a reminder.
If the guest enters an unrecognized email address, they will be given the opportunity to create a new account.

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
2. Locate the file `/db/schema.sql`
3. Executing the SQL code in this file will build the database schema necessary to run the app.
4. Execute the SQL code in `/db/testdata.sql` to populate the database with sample data. You may use one of the sample organizers with the password `cis498` to log in to the application.
5. Executing `schema.sql` and `testdata.sql` again will refresh the app to its default state.

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

This is useful for building a Web Archive (WAR) for final deployment to a Servlet container. This is also an easy way to push new versions, or "refresh" the application.

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
