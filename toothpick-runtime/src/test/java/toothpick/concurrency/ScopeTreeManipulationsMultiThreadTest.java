package toothpick.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import toothpick.ToothPick;
import toothpick.concurrency.threads.AddNodeThread;
import toothpick.concurrency.threads.RemoveNodeThread;
import toothpick.concurrency.threads.TestableThread;
import toothpick.concurrency.utils.ThreadTestUtil;

import static org.junit.Assert.assertTrue;
import static toothpick.concurrency.utils.ThreadTestUtil.STANDARD_THREAD_COUNT;

public class ScopeTreeManipulationsMultiThreadTest {

  static final String ROOT_SCOPE = "ROOT_SCOPE";

  @Before
  public void setUp() throws Exception {
    ToothPick.openScope(ROOT_SCOPE);
  }

  @After
  public void tearDown() throws Exception {
    ToothPick.reset();
  }

  @Test
  public void concurrentScopeAdditions_shouldNotCrash() throws InterruptedException {
    //GIVEN
    final int addNodeThreadCount = STANDARD_THREAD_COUNT;
    List<TestableThread> threadList = new ArrayList<>();

    //WHEN
    for (int indexThread = 0; indexThread < addNodeThreadCount; indexThread++) {
      AddNodeThread runnable = new AddNodeThread(ROOT_SCOPE);
      threadList.add(runnable);
      ThreadTestUtil.submit(runnable);
    }

    //THEN
    //we simply should not have crashed when all threads are done
    ThreadTestUtil.shutdown();
    for (TestableThread thread : threadList) {
      assertTrue(String.format("test of thread %s failed", thread.getName()), thread.isSuccessful());
    }
  }

  @Test
  public void concurrentScopeRemovals_shouldNotCrash() throws InterruptedException {
    //GIVEN
    final int removalNodeThreadCount = STANDARD_THREAD_COUNT;
    List<TestableThread> threadList = new ArrayList<>();

    //WHEN
    for (int indexThread = 0; indexThread < removalNodeThreadCount; indexThread++) {
      RemoveNodeThread runnable = new RemoveNodeThread(ROOT_SCOPE);
      threadList.add(runnable);
      ThreadTestUtil.submit(runnable);
    }

    //THEN
    //we simply should not have crashed when all threads are done
    ThreadTestUtil.shutdown();
    for (TestableThread thread : threadList) {
      assertTrue(String.format("test of thread %s failed", thread.getName()), thread.isSuccessful());
    }
  }

  @Test
  public void concurrentScopeAdditionsAndRemovals_shouldNotCrash() throws InterruptedException {
    //GIVEN
    final int removalNodeThreadCount = STANDARD_THREAD_COUNT / 2;
    final int addNodeThreadCount = STANDARD_THREAD_COUNT / 2;
    List<TestableThread> threadList = new ArrayList<>();
    final Random random = new Random();

    //WHEN
    for (int indexThread = 0; indexThread < addNodeThreadCount + removalNodeThreadCount; indexThread++) {
      final TestableThread runnable;
      if (random.nextInt(100) < 50) {
        runnable = new RemoveNodeThread(ROOT_SCOPE);
      } else {
        runnable = new AddNodeThread(ROOT_SCOPE);
      }
      threadList.add(runnable);
      ThreadTestUtil.submit(runnable);
    }

    //THEN
    //we simply should not have crashed when all threads are done
    ThreadTestUtil.shutdown();
    for (TestableThread thread : threadList) {
      assertTrue(String.format("test of thread %s failed", thread.getName()), thread.isSuccessful());
    }
  }
}
