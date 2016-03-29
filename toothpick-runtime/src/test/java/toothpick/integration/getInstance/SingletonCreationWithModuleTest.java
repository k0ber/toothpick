package toothpick.integration.getInstance;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Test;
import toothpick.Factory;
import toothpick.Injector;
import toothpick.InjectorImpl;
import toothpick.config.Module;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

/*
 * Creates a singleton in the simplest possible way
  * without any module.
 */
public class SingletonCreationWithModuleTest {

  @Test public void testIsProducingSingleton() throws Exception {
    //GIVEN
    Injector injector = new InjectorImpl(null, new SimpleModule());

    //WHEN
    Foo instance = injector.createInstance(Foo.class);
    Foo instance2 = injector.createInstance(Foo.class);

    //THEN
    assertThat(instance, notNullValue());
    assertThat(instance2, notNullValue());
    assertThat(instance, sameInstance(instance2));
  }

  private static class SimpleModule extends Module {
    public SimpleModule() {
      bind(Foo.class);
    }
  }

  @Singleton //annotation is not needed, but it's a better example
  public static class Foo {
    @Inject //annotation is not needed, but it's a better example
    public Foo() {
    }
  }

  @SuppressWarnings("unused") public static class Foo$$Factory implements Factory<Foo> {
    @Override public Foo createInstance(Injector injector) {
      return new Foo();
    }

    @Override public boolean hasSingletonAnnotation() {
      return true;
    }

    @Override public boolean hasProducesSingletonAnnotation() {
      return false;
    }
  }
}
