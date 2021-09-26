#Employee management API & UI

System requirements : 

 - Git
 - Java 8+
 - Maven 3+
 - Node 10+

Steps to Install : 

 1) Clone the repositories : 
     A) https://github.com/samratnayak/EmployeeManagementApi.git
     B) https://github.com/samratnayak/EmployeeManagement-UI.git
 3) Start the API application
     A) Go to Root where pom.xml is present
     B) Run `mvn clean install`
     C) Now cd target/
     D) From command line, run `java -jar EmployeeManagement-0.0.1-SNAPSHOT.jar`
     E) On successful startup, explore API Docs: http://localhost:8080/swagger-ui.html
 5) Start the UI app
    A) Got UI project root where package.json is present
    B) Run `npm install`
    C) Once it is complete after a few minutes, run `npm start`
    B) Hit the browser with URL: http://localhost:3000 
 
