### Overview
This package is a scratch pad for Ion related tools/code.

### IonSpaceCompare
The following tool will read `FILE` as Ion/JSON, and do some very basic size comparisons.  The `SST`
parameter will create the ideal shared symbol table with the given name.

```
$ ./gradlew runCompare -PappArgs=FILE,SST

```

### Notes
* Gradle/Kotlin with OpenJDK 9 may be problematic, OpenJDK 8 works well.
  * Specifically, the Kotlin/Gradle integration appears to be affected by [JDK-8171377](https://bugs.openjdk.java.net/browse/JDK-8171377).
    Having a version greater than b150 should resolve this.
