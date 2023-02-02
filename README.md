# Kotlin Coroutines & LMAX Disruptor Benchmark Project

This project benchmarks the performance of Kotlin Coroutines and LMAX Disruptor, two popular tools for asynchronous and concurrent programming in the JVM. The benchmark uses the Java Microbenchmark Harness (JMH) for accurate and reliable performance measurements.

## Kotlin Coroutines

Kotlin Coroutines is a library for writing asynchronous, non-blocking code in Kotlin. It provides a lightweight and expressive way to write asynchronous code, making it easier to write and understand concurrent programs.

## LMAX Disruptor

LMAX Disruptor is a high-performance inter-thread communication library for the JVM. It provides a fast and efficient way to pass data between threads, making it well-suited for high-throughput and low-latency applications.

## Benchmarking

The project benchmarks the performance of Kotlin Coroutines and LMAX Disruptor by measuring the time it takes for a large number of events to be processed by each library. The results of the benchmark can be used to compare the performance of the two libraries and help developers choose the best tool for their specific use case.

## How to Run the Benchmark

To run the benchmark, clone the repository and run the following command:

```shell
./gradlew :jmh-benchmark:jmh -Dorg.gradle.java.home=$(/usr/libexec/java_home -v17)
```

```shell
(export JAVA_VERSION=17 && export DATE=$(date +%Y-%m-%d-%H-%M-%S) && ./gradlew clean build :jmh-benchmark:jmh -Dorg.gradle.java.home=$(/usr/libexec/java_home -v$JAVA_VERSION))
```

The results of the benchmark will generate in [result](result) directory

## Visualized Result

After running the benchmark, the results will be generated in the [result](result) directory. For a fair comparison between the two libraries, these results can be visualized using [https://jmh.morethan.io/](https://jmh.morethan.io/).

+ [Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [512], numberEvent=[20, 10000, 100000] with GC info](https://jmh.morethan.io/?gist=e14ce0b67bfd95ab47e974fa36d2ae8f)
![Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [512], numberEvent=[20, 10000, 100000] with GC info](result/images/bufferSize-512-numberEvent-20-10000-100000.png)
+ [Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [128, 512, 1024], numberEvent=[100, 1000, 10000, 20000]](https://jmh.morethan.io/?gist=3bd4037a941da944fe3a578ee2fe5fb6)
![Lmax-Disruptor vs Kotlin Coroutines: bufferSize = [128, 512, 1024], numberEvent=[100, 1000, 10000, 20000]](result/images/bufferSize-128-512-1024-numberEvent-100-1000-100000-200000.png)
## Conclusion

Based on the benchmark results, it can be concluded that Kotlin Coroutines have better throughput compared to LMAX Disruptor, however, they consume more memory. The choice between the two ultimately depends on the specific requirements of the application and the trade-offs the developer is willing to make.

## Contributing

If you're interested in contributing to this project, please reach out to the project maintainers. We're always looking for ways to improve the benchmark and make it more comprehensive.
