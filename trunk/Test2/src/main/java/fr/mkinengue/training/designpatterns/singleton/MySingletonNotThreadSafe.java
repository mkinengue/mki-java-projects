package fr.mkinengue.training.designpatterns.singleton;

/**
 * Implementation class of a Singleton<br />
 * The class is final so that there can be no subclasses of current class<br />
 * In certain case of JVM optimization, this solution might not work : indeed, in case of thread concurrence the compilator can decide to
 * allow a second thread to execute the getInstance method entirely while the instanciation of the instance being done in the first thread
 * is not done yet<br />
 * Not Thread-safe
 */
public final class MySingletonNotThreadSafe {

    /** volatile prevents the case where instance is not null and not yet instanced */
    private static volatile MySingletonNotThreadSafe instance;

    /**
     * Private constructor to prevent instanciation of this class
     */
    private MySingletonNotThreadSafe() {
    }

    /**
     * Accessor of the only instance of this class
     * 
     * @return MySingleton
     */
    public final static MySingletonNotThreadSafe getInstance() {
        if (instance == null) {
            synchronized (MySingletonNotThreadSafe.class) {
                if (instance == null) {
                    instance = new MySingletonNotThreadSafe();
                }
            }
        }
        return instance;
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
