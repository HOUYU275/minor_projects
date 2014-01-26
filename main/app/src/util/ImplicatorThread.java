package util;

import weka.fuzzy.measure.WeakGamma;
import weka.fuzzy.similarity.Relation;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 28/10/12
 * Time: 12:36
 */
public class ImplicatorThread implements Runnable {

    //private Implicator m_Implicator;
    private ThreadPool threadPool;
    private Relation current;
    private int x;
    private int y;
    private WeakGamma weakGamma;
    private int m_classIndex;
    private int d;

    public ImplicatorThread(ThreadPool threadPool, Relation current, int x, int y, WeakGamma weakGamma, int m_classIndex, int d) {
        this.threadPool = threadPool;
        this.current = current;
        this.x = x;
        this.y = y;
        this.weakGamma = weakGamma;
        this.m_classIndex = m_classIndex;
        this.d = d;
    }

    @Override
    public void run() {
        double currLower = weakGamma.m_Implicator.calculate(current.getCell(x, y), weakGamma.fuzzySimilarity(m_classIndex, d, y));
        threadPool.setTemp(Math.min(currLower, threadPool.getTemp()));
    }
}
