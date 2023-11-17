# school-project-comp.cs.140
Programming course 3, project done in group of two, spring -23.
Sisu is web application where students of Tampere University draft their personal study plan and sign up for courses.
This implementation fetches and parses JSON data from Sisu API. Selected study degree structure is then shown in GUI for viewing. 

## Usage

### Run locally
Packaged into JAR file for easy distributing, navigate to Sisu/target
```sh
java -jar Sisu-1.0.one-jar.jar
```
.. if you don´t have javaFX installed, run the following with correct file path
```sh
java --module-path "C:\Program Files\Java\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml -jar Sisu-1.0.one-jar.jar
```

## Learning goals
Adding functionality to a program using object oriented design.
Using external libraries as a part of the implementation (design by contract).
Implementing a GUI as a part of program.
Inheritance.
Json parsing.
Unit testing.
Implementing a program as a team effort.



