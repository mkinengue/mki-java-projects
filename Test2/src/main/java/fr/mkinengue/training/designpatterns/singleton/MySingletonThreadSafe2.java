package fr.mkinengue.training.designpatterns.singleton;

/**
 * Implementation class of a Singleton<br />
 * The class is final so that there can be no subclasses of current class<br />
 * The drawback of this implementation is that in case the instanciation of instance fails, the application will fail loading
 */
public final class MySingletonThreadSafe2 {

    private MySingletonThreadSafe2() {
    }

    private static class MySingletonThreadSafe2Wrapper {

        private final static MySingletonThreadSafe2 instance = new MySingletonThreadSafe2();
    }

    public static MySingletonThreadSafe2 getInstance() {
        return MySingletonThreadSafe2Wrapper.instance;
    }
}
