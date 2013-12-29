package fr.mkinengue.training.designpatterns.singleton;

/**
 * Implementation class of a Singleton<br />
 * The class is final so that there can be no subclasses of current class<br />
 * The drawback of this implementation is that in case the instanciation of instance fails, the application will fail loading<br />
 * Due to the JVM mechanism which obliges Java to initialize a static variable before its first use, it is a guaranteed that this
 * implementation is Thead-safe
 */
public final class MySingletonThreadSafe1 {

    private static MySingletonThreadSafe1 instance = new MySingletonThreadSafe1();

    public static MySingletonThreadSafe1 getlnstance() {
        return instance;
    }

    private MySingletonThreadSafe1() {
    }

    /**
     * {@inheritDoc}<br />
     * Override clone() method so that it can't be used
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
