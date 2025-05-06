#!/bin/bash

# ==== CONFIG ====
SRC_MAIN="src/main"
SRC_TEST="src/test"
BIN_DIR="bin"
LIB_DIR="lib"
JUNIT_JAR="$LIB_DIR/junit-4.13.2.jar"
HAMCREST_JAR="$LIB_DIR/hamcrest-core-1.3.jar"

# ==== CLEAN ====
echo "Cleaning previous builds..."
rm -rf $BIN_DIR
mkdir -p $BIN_DIR

# ==== COMPILE MAIN ====
echo "Compiling source files..."
find $SRC_MAIN -name "*.java" > sources.txt
javac -d $BIN_DIR @sources.txt
rm sources.txt

# ==== COMPILE TESTS ====
echo "Compiling test files..."
find $SRC_TEST -name "*.java" > test_sources.txt
javac -cp "$BIN_DIR:$JUNIT_JAR:$HAMCREST_JAR" -d $BIN_DIR @test_sources.txt
rm test_sources.txt

# ==== RUN TESTS ====
echo "Running tests..."
for test_class in $(find $BIN_DIR -name "*Test.class" | sed "s|$BIN_DIR/||;s|/|.|g;s|.class||")
do
    echo "== Running $test_class =="
    java -cp "$BIN_DIR:$JUNIT_JAR:$HAMCREST_JAR" org.junit.runner.JUnitCore $test_class
done
