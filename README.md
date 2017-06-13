# statistics
Statistics challenge

## Instructions:

1. Build and install the package:

mvn install

2. Run the server:

mvn jetty:run 

3. Open the browser at the main endpoint:

http://localhost:8080/statistics


## Potential improvements for a more scalable solution

- Separate service (web) packages from core classes in a separate project (library).
- Implement the datasource centralized so different threads can push and pull data from it.
- Include a loadbalancer with a backend of nodes running the application.