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

LOMBOK_PATH=$(find ~/.m2 -name "lombok-1.18.38.jar" | head -n 1)

JAVAFX_OPTIONS="-Djavafx.verbose=true"

if [ ! -z "$LOMBOK_PATH" ]; then
    JAVAFX_OPTIONS="$JAVAFX_OPTIONS -Djavafx.maven.javaagent.path=$LOMBOK_PATH"
    echo "Build with Lombok : $LOMBOK_PATH"
else
    echo "LOMBOK_PATH not found"
    exit 1
fi


# Clean and package the project
echo "Building the JavaFX application..."
$MVN_CMD clean package -Dmaven.compiler.forceJavacCompilerUse=true || { echo "Failed to build the application"; exit 1; }

echo "Build done"


echo "Running the application..."
$MVN_CMD javafx:run \
    -Djavafx.run.jvmArgs="\
        --module-path $LOMBOK_PATH \
        --add-modules lombok \
        --add-opens java.base/java.lang=ALL-UNNAMED \
        --add-opens java.base/sun.net.www.protocol.jar=ALL-UNNAMED \
        -javaagent:$LOMBOK_PATH" \
    || { echo "Failed to run application"; exit 1; }

echo "Application execution completed."


echo "Application has completed execution."

