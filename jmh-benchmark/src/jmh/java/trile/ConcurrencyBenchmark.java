package trile;

import disruptor.DisruptorCoordinator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import trile.coroutines.CoroutineCoordinator;

@State(Scope.Thread)
public class ConcurrencyBenchmark {


  private final static Logger log = LogManager.getLogger(ConcurrencyBenchmark.class);
  @Param({"128", "2048"})
  private int bufferSize;

  @Param({"8", "64"})
  private int poolSize;

  @Param({"100", "1000000"})
  private int numberEvent;
  private DisruptorCoordinator disruptorCoordinator;

  private CoroutineCoordinator coroutinesCoordinator;
  private CoroutineCoordinator scopeCoordinator;

  private AtomicLong disruptorCounter;
  private AtomicLong coroutinesCounter;
  private AtomicLong scopeCounter;

  @Setup
  public void init(Blackhole blackhole) {
    disruptorCounter = new AtomicLong();
    coroutinesCounter = new AtomicLong();
    scopeCounter = new AtomicLong();

    disruptorCoordinator = new DisruptorCoordinator(bufferSize, poolSize,
      createCounterConsumerFunction(disruptorCounter, blackhole, numberEvent));
    coroutinesCoordinator = new CoroutineCoordinator(bufferSize, poolSize,
      createCounterConsumerFunction(coroutinesCounter, blackhole, numberEvent));
    scopeCoordinator = new CoroutineCoordinator(bufferSize, poolSize,
      createCounterConsumerFunction(scopeCounter, blackhole, numberEvent));
  }

  @TearDown
  public void teardown() {
    log.info("bufferSize {}, poolSize {}, numberEvent {}, disruptorCounter {}, coroutinesCounter {}, scopeCounter {}",
      bufferSize, poolSize, numberEvent, disruptorCounter.get(), coroutinesCounter.get(), scopeCounter.get());

    disruptorCoordinator.teardown();
    coroutinesCoordinator.teardown();
    scopeCoordinator.teardown();
  }

  @Benchmark
  public void disruptor() {
    disruptorCoordinator.produceAndConsumeMessage(numberEvent);
  }

  @Benchmark
  public void coroutine() {
    coroutinesCoordinator.produceAndConsumeMessage(numberEvent);
  }

  @Benchmark
  public void scope() {
    scopeCoordinator.produceAndConsumeInScope(numberEvent);
  }

  public Consumer<Long> createConsumerFunction(String message, Blackhole blackhole, int numberEvent) {
    return (data) -> {
      blackhole.consume(data);
      if (data >= numberEvent) {
        log.info(message);
      }
    };
  }

  public Consumer<Long> createCounterConsumerFunction(AtomicLong counter, Blackhole blackhole, int numberEvent) {
    return (data) -> {
      blackhole.consume(data);
      if (data >= numberEvent) {
        counter.incrementAndGet();
      }
    };
  }
}
