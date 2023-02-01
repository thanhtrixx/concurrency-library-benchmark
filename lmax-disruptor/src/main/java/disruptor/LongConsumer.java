package disruptor;

import com.lmax.disruptor.EventHandler;
import java.util.function.Consumer;

public class LongConsumer implements EventHandler<Event> {

  private final Consumer<Long> consumeFunction;

  public LongConsumer(Consumer<Long> consumeFunction) {
    this.consumeFunction = consumeFunction;
  }

  @Override
  public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
    consumeFunction.accept(event.getData());
  }
}

