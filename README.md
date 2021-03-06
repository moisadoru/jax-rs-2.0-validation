## Apache CXF and Bean Validation 1.1

[![Build Status](https://drone.io/github.com/moisadoru/jax-rs-2.0-validation/status.png)](https://drone.io/github.com/moisadoru/jax-rs-2.0-validation/latest)

This is a prototype of a Java JAX-RS service implemented with CXF/Spring and Bean Validation (via Hibernate Validator).

### Build and run:

If you run it for the first time:
```bash
$> mvn clean install && mvn -pl example-app jetty:run
```

Subsequent times, if you did not modify anything, it's enough to run:
```bash
$> mvn -pl example-app jetty:run
```
### Test

You should see the Jetty startup message:
```
 [...]
 2014-06-06 19:51:22.555:INFO:oejs.ServerConnector:main: Started ServerConnector@7ade9f78...
 2014-06-06 19:51:22.556:INFO:oejs.Server:main: Started @8006ms
 [INFO] Started Jetty Server
 [INFO] Starting scanner at interval of 5 seconds.
```

Now you can run tests. Now it would be a good idea to install curl if you don't have it already. Optionally, install the xmllint command line tool, for pretty printing the xml output (for debian, install libxml2-utils).

#### Successful requests:
 * Add one person:
```bash
$> curl -s 'http://localhost:8080/rest/api/people' -H 'Accept: application/json' \
 -H 'Content-Type: application/json' -X POST \
 -d '{"email":"a1@b.com", "firstName": "Doe", "lastName": "John"}'
```
```json
{
  "email" : "a1@b.com",
  "firstName" : "Doe",
  "lastName" : "John"
}
```
 * Add another person:
```bash
$> curl -s 'http://localhost:8080/rest/api/people' -H 'Accept: application/xml' \
 -H 'Content-Type: application/json' -X POST \
 -d '{"email":"a2@b.com", "firstName": "Doe", "lastName": "John"}' | xmllint --format -
```
```xml
<?xml version="1.0"?>
<person>
  <email>a2@b.com</email>
  <firstName>Doe</firstName>
  <lastName>John</lastName>
</person>
```

#### Error responses:

 * When all the fields are missing:
```bash
$> curl -s 'http://localhost:8080/rest/api/people' -H 'Accept: application/json' \
 -H 'Content-Type: application/json' -X POST \
 -d '{}'
```
```json
{
  "type" : "VALIDATION_FAILED",
  "message" : "The input data is invalid",
  "errors" : [ {
    "type" : "NOTNULL",
    "message" : "may not be null",
    "field" : "lastName"
  }, {
    "type" : "NOTNULL",
    "message" : "may not be null",
    "field" : "firstName"
  }, {
    "type" : "NOTNULL",
    "message" : "may not be null",
    "field" : "email"
  } ],
  "moreInfo" : "https://example.com/documentation/validation"
}
```
 * When *email* is invalid, and *lastName* is missing:
```bash
$> curl -s 'http://localhost:8080/rest/api/people' -H 'Accept: application/xml' \
 -H 'Content-Type: application/json' -X POST \
 -d '{"email":"invalid","firstName":"Bla"}' | xmllint --format -
```
```xml
<?xml version="1.0"?>
<error>
  <type>VALIDATION_FAILED</type>
  <message>The input data is invalid</message>
  <more_info>https://example.com/documentation/validation</more_info>
  <errors>
    <error>
      <type>NOTNULL</type>
      <message>may not be null</message>
      <field>lastName</field>
    </error>
    <error>
      <type>EMAIL</type>
      <message>not a well-formed email address</message>
      <field>email</field>
    </error>
  </errors>
</error>
```
 * Property *lastName* too short:
```bash
$> curl -s 'http://localhost:8080/rest/api/people' -H 'Accept: application/xml' \
 -H 'Content-Type: application/json' -X POST \
 -d '{"email":"a3@b.com", "firstName": "Doe", "lastName": "J"}' | xmllint --format -
```
```xml
<?xml version="1.0"?>
<error>
  <type>VALIDATION_FAILED</type>
  <message>The input data is invalid</message>
  <more_info>https://example.com/documentation/validation</more_info>
  <errors>
    <error>
      <type>PATTERN</type>
      <message>must match "^[a-zA-Z]{2,}$"</message>
      <field>lastName</field>
    </error>
  </errors>
</error>
```

### Notes
[Original project](https://github.com/reta/jax-rs-2.0-validation)
