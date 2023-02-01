package trile

import disruptor.DisruptorCoordinator
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Consumer
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.infra.Blackhole
import trile.coroutines.CoroutineCoordinator
import trile.coroutines.uti.Log

@State(Scope.Thread)
open class ConcurrencyBenchmark : Log {

  @Param("128", "2048")
  private var bufferSize = 0

  @Param("8", "64")
  private var poolSize = 0

  @Param("100", "1000000")
  private var numberEvent = 0

  private lateinit var disruptorCoordinator: DisruptorCoordinator
  private lateinit var coroutinesCoordinator: CoroutineCoordinator
  private lateinit var scopeCoordinator: CoroutineCoordinator
  private lateinit var disruptorCounter: AtomicLong
  private lateinit var coroutinesCounter: AtomicLong
  private lateinit var scopeCounter: AtomicLong

  @Setup
  fun init(blackhole: Blackhole) {
    disruptorCounter = AtomicLong()
    coroutinesCounter = AtomicLong()
    scopeCounter = AtomicLong()
    disruptorCoordinator = DisruptorCoordinator(
      bufferSize, poolSize,
      createCounterConsumerFunction(disruptorCounter, blackhole, numberEvent)
    )
    coroutinesCoordinator = CoroutineCoordinator(
      bufferSize, poolSize,
      createCounterConsumerFunction(coroutinesCounter, blackhole, numberEvent)
    )
    scopeCoordinator = CoroutineCoordinator(
      bufferSize, poolSize,
      createCounterConsumerFunction(scopeCounter, blackhole, numberEvent)
    )

  }

  @TearDown
  fun teardown() {
    l.info {
      "bufferSize $bufferSize, poolSize $poolSize, numberEvent $numberEvent, disruptorCounter ${disruptorCounter.get()}, coroutinesCounter ${coroutinesCounter.get()}, scopeCounter ${scopeCounter.get()}"
    }
    disruptorCoordinator.teardown()
    coroutinesCoordinator.teardown()
    scopeCoordinator.teardown()
  }

  @Benchmark
  fun disruptor() {
    disruptorCoordinator.produceAndConsumeMessage(numberEvent)
  }

  @Benchmark
  fun coroutine() {
    coroutinesCoordinator.produceAndConsumeMessage(numberEvent.toLong())
  }

  @Benchmark
  fun scope() {
    scopeCoordinator.produceAndConsumeInScope(numberEvent.toLong())
  }

  fun createConsumerFunction(message: String, blackhole: Blackhole, numberEvent: Int): Consumer<Long> {
    return Consumer { data: Long ->
      blackhole.consume(data)
      if (data >= numberEvent) {
        l.info(message)
      }
    }
  }

  private fun createCounterConsumerFunction(
    counter: AtomicLong,
    blackhole: Blackhole,
    numberEvent: Int
  ): Consumer<Long> {
    return Consumer { data: Long ->
      blackhole.consume(data)
      if (data >= numberEvent) {
        counter.incrementAndGet()
      }
    }
  }

}

