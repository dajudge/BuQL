# BuQL - A Bulk Query Library 
___Note:___ __THIS IS STILL WORK IN PROGRESS. DON'T BELIEVE THE PROMISES!__


Modern streaming applications often process thousands of messages per second. Classical RDMBS implementations
can easily become a bottleneck in your architecture when DB access is performed on a per-message basis.

BuQL uses a specialized SQL generation engine that can retrieve a large number of result sets in a streamlined
fashion to greatly reduce the communication overhead found in systems that hit the database once (or even 
multiple times!) for each processed message.

## Compatibility
BuQL has automated tests verifying proper functionality with the following databases:
* H2
* PostgreSQL (with PostGIS extension)
* MariaDB

## Running a bulk query with BuQL (concept)
In order to simplify data retrieval, BuQL uses reflection to simplify implementation of the data access logic
you want to execute. All you need to do is provide some Java Beans for passing in query parameters / getting
out results and an interface you use to describe the queries you want to execute. 

Example:
```java
// Provide a result object
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private String street;
    private String zip;
    private String country;
   
    [... getters / setters ...]
}

// Define the repository interface
@Table("person")
public interface PersonRepository {
    // This is a traditional one-by-one data access method (still
    // supported in BuQL)
    Person findById(Long ids);
    
    // The is a bulk data access method. The Strings in the Map types 
    // correlate the returned results to the query objects you passed in.
    Map<String, Person> getById(Map<String, Long> ids);
    
    // Queries can also be more complex objects. Even sophisticated
    // hierarchical predicates can be formulated.
    Map<String, List<Person>> findByFilter(Map<String, ComplexPersonFilter> ids);
}

// In your business logic:
PersonRepository repo = buql.up(PersonRepository.class);
Map<String Long> ids = new HashMap<>();
ids.put("ID0", 42);
ids.put("ID1", 7);
ids.put("ID2", 10);
Map<String, Person> result = repo.getById(ids);
System.out.println("Hello, 42. Your name is: " + result.get("ID0").getFirstName());
``` 