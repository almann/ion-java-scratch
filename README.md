### Overview
This package is a scratch pad for Ion related tools/code.

### IonSpaceCompare
The following tool will read `FILE` as Ion/JSON, and do some very basic size comparisons.  The `SST`
parameter will create the ideal shared symbol table with the given name.

```
$ ./gradlew runCompare -PappArgs=FILE,SST

```

### Notes
* Gradle/Kotlin with OpenJDK 9 seems to be problematic due to modules, using OpenJDK 8 seems to work better
