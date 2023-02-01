package disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

public class DisruptorCoordinator {

  private final ExecutorService executor;

  private final Disruptor<Event> disruptor;

  private final RingBuffer<Event> ringBuffer;
  private final LongConsumer longConsumer;

  private ThreadFactory threadFactory = r -> {
    Thread t = new Thread(r);
    t.setDaemon(true);
    return t;
  };

  public DisruptorCoordinator(int poolSize, int bufferSize, Consumer<Long> consumeFunction) {

    executor = Executors.newFixedThreadPool(poolSize);
    longConsumer = new LongConsumer(consumeFunction);

    disruptor = new Disruptor<>(Event::new, bufferSize, executor);
    disruptor.handleEventsWith(longConsumer);
    ringBuffer = disruptor.start();
  }


  public void produceAndConsumeMessage(int numberEvent) {

    var producer = new LongProducer(ringBuffer);

    var feature = executor.submit(() -> producer.produce(numberEvent));

    try {
      feature.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

  }

  public void teardown() {
    disruptor.shutdown();
    executor.shutdown();
  }
}
