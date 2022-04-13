# TestByExample
Write Tests as Examples on the Method-under-test

## Progress
**THIS PROJECT IS STILL WIP**
- [x] Example Call Code Generation
- [x] Run the generated code with JUnit
- [ ] Run Action with Intellij Plugin
- [ ] Syntax Highlighting in Intellij Plugin

## Goal
This project will enable you to annotate a function or method with `@Example` and execute it as you would run a Unit-Test.
All values in the annotation are strings, but will be executed with generated code in the same scope as the file.
```kotlin
class Bar {
  @Example(
    self = "Bar()",
    params = ["42"],
    result = "44")
  fun foo(p: Int): Int {
    return p + 2
  }
}
```

## Interface
- `self`: Object the method is run on (Can be empty for functions) 
- `params`: Array of parameters for the function
- `context`: Function that will be called before the test to setup state
- `result`: Value that will be compared to the actual return value to make test suceed/fail

## Technologies
1. For generating the code that calls the examples [KSP](https://kotlinlang.org/docs/ksp-overview.html) is used.
2. The [JUnit Platform Launcher API](https://junit.org/junit5/docs/current/user-guide/#launcher-api) is used to run the generated code.
3. The Intellij Plugin provides syntax highlighting for the example code and an action to run the example.

## Useful Links
- https://blogs.oracle.com/javamagazine/post/junit-build-custom-test-engines-java
