package trile

import disruptor.DisruptorCoordinator
import trile.coroutines.CoroutineCoordinator
import trile.coroutines.uti.Log


class Main : Log {

  fun run() {
    l.info("Starting")
    val coroutine = CoroutineCoordinator(
      8, 8
    ) { data: Long? -> l.info { "coroutine $data" } }
    val scope = CoroutineCoordinator(
      8, 8
    ) { data: Long? -> l.info { "scope $data" } }
    val disruptor = DisruptorCoordinator(
      8, 8
    ) { data: Long? -> l.info { "disruptor $data" } }
    l.info("coroutine")
    coroutine.produceAndConsumeMessage(100)
    l.info("coroutine done")
    coroutine.teardown()
    l.info("scope")
    scope.produceAndConsumeInScope(100)
    l.info("scope done")
    scope.teardown()
    l.info("disruptor")
    disruptor.produceAndConsumeMessage(100)
    l.info("disruptor done")
    disruptor.teardown()
    l.info("Done")
  }
}

fun main() {
  Main().run()
}
