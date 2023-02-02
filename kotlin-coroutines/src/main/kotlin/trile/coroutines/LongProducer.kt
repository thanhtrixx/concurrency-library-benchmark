package trile.coroutines

import kotlinx.coroutines.channels.Channel

class LongProducer(
  private val channel: Channel<Long>
) {

  suspend fun produce(numberEvent: Long) {
    for (i in 1 until numberEvent + 1) {
      channel.send(i)
    }
  }
}
