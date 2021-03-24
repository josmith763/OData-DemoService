# OData-DemoService
A sample Java OData API using Apache Olingo.

Note: The data source is mocked for simplicity in this example. Future version will most likely use an external data source.

To run, make sure you have a Tomcat 7 server up and running and configured to use port 8080. To build the project, all you need to do is run "mvn tomcat7:deploy" in IntelliJ's "Run Anything" tab, or another IDE's equivalent.

There is a Postman collection in the projects root directory that you can use to play around with the various endponts offered. If you need to change something in the API and would like to see your changes reflected, run "mvn tomcat7:undeploy" and then "mvn tomcat7:redeploy" in the same place as you would the deploy command mentioned previously.
