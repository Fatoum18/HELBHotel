#!/bin/bash

# Check if pom.xml exists
if [ ! -f "pom.xml" ]; then
    echo "Error: pom.xml not found in project directory."
    exit 1
fi

# Check if Maven Wrapper is installed
if [ -f "./mvnw" ]; then
    chmod +x ./mvnw
    MVN_CMD="./mvnw"
    echo "Maven Wrapper was found in the project"
else
    echo "Maven Wrapper not found. Please install Maven or include mvnw and try again."
    exit 1
fi

# Run JUnit tests
echo "Running JUnit tests..."
$MVN_CMD test -Dmaven.compiler.forceJavacCompilerUse=true || { echo "JUnit tests failed"; exit 1; }

echo "JUnit tests completed successfully."
