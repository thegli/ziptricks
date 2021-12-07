# Zip Tricks

A small Java application to demonstrate the loading of file entries of a zip archive into memory using only *java.util.zip* libraries.

## Requirements

Java 8 or higher.  
Maven is used for dependency and build  management, but besides JUnit no 3rd-party libraries are required.

## Testing

There are some test (zip) files in *src/test/resources* used by the unit tests.

## Build

```shell
mvn clean package
```

## Usage

`ZipArchiveLoader` contains the main code and can be used as utility class in your own code.

The main class `Main` provides a simple loader where the first program argument is taken as zip file path, e.g.

``` shell
java -jar target/ziptricks-1.0.0-SNAPSHOT.jar src/test/resources/flat.zip
```
