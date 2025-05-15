#!/bin/bash

# Check if pom.xml exists
if [ ! -f "pom.xml" ]; then
    echo "Error: pom.xml not found in project directory."
    exit 1
fi


# Check if Maven Wrapper is installed
if [ -f "./mvnw" ]; then
    # Make sure mvnw is executable
    chmod +x ./mvnw
    MVN_CMD="./mvnw"
    echo "Maven Wrapper was founded in the project"
else
    echo "Maven is not installed. Please install Maven and try again."
    exit 1
fi

 
# Clean and package the project
echo "Building the JavaFX application..."
$MVN_CMD clean package -Dmaven.compiler.forceJavacCompilerUse=true || { echo "Failed to build the application"; exit 1; }

echo "Build done"


echo "Running the application..."
$MVN_CMD javafx:run  || { echo "Failed to run application"; exit 1; }

echo "Application execution completed."
 

