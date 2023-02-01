package disruptor;

import com.lmax.disruptor.RingBuffer;

public class LongProducer {

  private final RingBuffer<Event> ringBuffer;

  public LongProducer(RingBuffer<Event> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void produce(long numberEvent) {
    for (long i = 1; i <= numberEvent; i++) {
      long sequence = ringBuffer.next();
      Event event = ringBuffer.get(sequence);
      event.setData(i);
      ringBuffer.publish(sequence);
    }
  }
}
