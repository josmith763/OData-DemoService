# OData-DemoService
A sample Java OData API using Apache Olingo.

Note: The data source is mocked for simplicity in this example. Future version will most likely use an external data source.

To run, run the main class or `mvn spring-boot:run`.

There is a Postman collection in the projects root directory that you can use to play around with the various endponts offered. If you need to change something in the API and would like to see your changes reflected, run "mvn tomcat7:undeploy" and then "mvn tomcat7:redeploy" in the same place as you would the deploy command mentioned previously.
