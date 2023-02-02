package trile.coroutines

import java.util.function.Consumer
import kotlinx.coroutines.channels.Channel

class LongConsumer(
  private val channel: Channel<Long>,
  private val consumeFunction: Consumer<Long>
) {

  suspend fun consume(numberEvent: Long) {
    for (i in 1 until numberEvent + 1) {
      consumeFunction.accept(channel.receive())
    }
  }
}
