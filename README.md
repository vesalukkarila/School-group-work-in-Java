# Programming 3 course, comp.cs.140, spring 2023
A school project done in group of two.

## Usage

### Run locally
Navigate to Sisu/target
```sh
java -jar Sisu-1.0.one-jar.jar
```
And if javaFX not installed, run the following with correct file path
```sh
java --module-path "C:\Program Files\Java\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml -jar Sisu-1.0.one-jar.jar
```

### Learning goals
Adding functionality to a program using object oriented design.
Using external libraries as a part of the implementation (design by contract).
Implementing a GUI as a part of program.
Inheritance.
Json parsing.
Unit testing.
Implementing a program as a team effort.

### Program
Sisu is web application where students of Tampere University draft their personal study plan and sign up for courses.
This program fetches and parses JSON data from Sisu API. Selected study degree structure is then shown in GUI. 
