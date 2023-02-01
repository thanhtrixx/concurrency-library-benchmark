package trile.coroutines

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.function.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CoroutineCoordinator(
  bufferSize: Int, poolSize: Int, private val consumeFunction: Consumer<Long>
) {

  private val channel = Channel<Long>(bufferSize)

  private val daemonThreadFactory = ThreadFactory { r ->
    val t = Thread(r)
    t.isDaemon = true
    t
  }

  private val executor = Executors.newFixedThreadPool(poolSize, daemonThreadFactory)

  private val dispatcher = executor.asCoroutineDispatcher()


  fun produceAndConsumeMessage(numberEvent: Long) = runBlocking {
    val producer = LongProducer(channel)
    val consumer = LongConsumer(channel, consumeFunction)

    val producerScope = launch { producer.produce(numberEvent) }
    val consumerScope = launch { consumer.consume(numberEvent) }

    producerScope.join()
    consumerScope.join()
  }

  private fun CoroutineScope.produce(numberEvent: Long) = launch {
    for (i in 1 until numberEvent + 1) {
      channel.send(i)
    }
  }

  private fun CoroutineScope.consume(numberEvent: Long) = launch {
    for (i in 1 until numberEvent + 1) {
      consumeFunction.accept(channel.receive())
    }
  }

  fun produceAndConsumeInScope(numberEvent: Long) = runBlocking(dispatcher) {

    val producerScope = produce(numberEvent)
    val consumerScope = consume(numberEvent)

    producerScope.join()
    consumerScope.join()
  }

  fun teardown() {
    channel.close()
    executor.shutdown()
  }
}

fun main() {
  val coroutine = CoroutineCoordinator(8, 8) { println(it) }

  coroutine.produceAndConsumeMessage(10)

  val flow = CoroutineCoordinator(8, 8) { println(it) }

}
