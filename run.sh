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
    echo "Maven Wrapper was found in the project"
else
    echo "Maven Wrapper not found. Please install it or use a system Maven."
    exit 1
fi

# Clean and package the project without running tests
echo "Building the JavaFX application (tests will be skipped)..."
$MVN_CMD clean package -DskipTests -Dmaven.compiler.forceJavacCompilerUse=true || {
    echo "Failed to build the application";
    exit 1;
}

echo "Build done"

# Run the application
echo "Running the application..."
$MVN_CMD javafx:run || {
    echo "Failed to run application";
    exit 1;
}

echo "Application execution completed."
