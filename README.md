# concurrency-library-benchmark

### [jmh-benchmark](jmh-benchmark)
### [kotlin-coroutines-channel](kotlin-coroutines-channel)
### [kotlin-coroutines-flow](kotlin-coroutines-flow)
### [lmax-disruptor](lmax-disruptor)

### Run
```shell
./gradlew :jmh-benchmark:jmh -Dorg.gradle.java.home=$(/usr/libexec/java_home -v17)
```

```shell
(export JAVA_VERSION=17 && export DATE=$(date +%Y-%m-%d-%H-%M-%S) && ./gradlew clean build :jmh-benchmark:jmh -Dorg.gradle.java.home=$(/usr/libexec/java_home -v$JAVA_VERSION))
```

### Result

+ [Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [512], numberEvent=[20, 10000, 100000] with GC info](https://jmh.morethan.io/?gist=e14ce0b67bfd95ab47e974fa36d2ae8f)
+ [Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [512], numberEvent=[20, 10000, 100000]](https://jmh.morethan.io/?gist=33bfe82a8d42a77ccf89890a822bfd4e)
+ [Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [32, 512], numberEvent=[20, 10000]](https://jmh.morethan.io/?gist=0c26adc21439d1674682880ffc5e1c2d)
+ [Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [128, 512, 1024], numberEvent=[100, 1000, 10000, 20000]](https://jmh.morethan.io/?gist=3bd4037a941da944fe3a578ee2fe5fb6)
