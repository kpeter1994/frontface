# Building and running commands for Windows/Powershell
All the commands should be run from the root of the folder (same level as the pom.xml is).

## Build examples

### Build the application and run only the unit tests
```
./mvnw clean package
```

### Build the application and run all tests (unit and integration)
```
./mvnw clean verify
```

### Notes for running CI

In order to run the `./mvnw` command, it must have proper file permissions.
On Windows this problem does not exist, but your GitHub runner will be Linux-based.
If you get an error like this: `./mvnw: Permission denied`, it means it is not set up correctly.

There are two possible solutions:
* either instead of `./mvnw` use `mvn`, which will be available if you set up Java the same way as we did in our training exercises
* or run `git update-index --chmod=+x mvnw` and push a commit that contains this change

Now note, that GitHub Desktop might not work well with the second solution, so I recommend the first solution.

## Run examples

Note that when you will containerize the application, the last parameter 
(location of the JAR file) will change.

### Configuration requirements
To run the application successfully you do not need to specify any configuration.

However, you should set up the environment variable __FRONTFACE_TOKEN__ 
(or system property __frontface.token__) as the same token value as you set up for the
_background_ application.

Also, if you want to reach the _background_ application, its URL must be set up with the
environment variable __BACKGROUND_URL__ (or system property __background.url__).

Note that the configuration options you see can be combined.

### Run the application without any configuration or extra properties
```
java -jar target/cubix-exam-frontface-0.0.1-SNAPSHOT.jar
```

### Run the application with setting the token
The value of the token here is _REPLACEME_.
```
java "-Dfrontface.token=REPLACEME" -jar target/cubix-exam-frontface-0.0.1-SNAPSHOT.jar
```

### Run the application while the background application is running
The listening port is changed to 8081, while the _background_ application is running on 8080.
```
java "-Dbackground.url=localhost:8080" "-Dserver.port=8081" "-Dfrontface.token=REPLACEME" -jar target/cubix-exam-frontface-0.0.1-SNAPSHOT.jar
```
