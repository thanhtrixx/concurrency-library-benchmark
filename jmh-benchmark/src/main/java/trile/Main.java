package trile;

import disruptor.DisruptorCoordinator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trile.coroutines.CoroutineCoordinator;

public class Main {

  private static Logger log = LogManager.getLogger(Main.class);

  public static void main(String[] args) {

    log.info("Starting");
    var coroutine = new CoroutineCoordinator(8, 8, (data) -> log.info("coroutine {}", data));
    var scope = new CoroutineCoordinator(8, 8, (data) -> log.info("scope {}", data));
    var disruptor = new DisruptorCoordinator(8, 8, (data) -> log.info("disruptor {}", data));

    log.info("coroutine");
    coroutine.produceAndConsumeMessage(100);
    log.info("coroutine done");
    coroutine.teardown();

    log.info("scope");
    scope.produceAndConsumeInScope(100);
    log.info("scope done");
    scope.teardown();

    log.info("disruptor");
    disruptor.produceAndConsumeMessage(100);
    log.info("disruptor done");
    disruptor.teardown();

    log.info("Done");
  }


}
