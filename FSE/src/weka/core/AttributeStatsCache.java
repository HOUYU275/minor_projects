package weka.core;

import weka.core.Instances;
import weka.experiment.Stats;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 25/11/11
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class AttributeStatsCache implements Serializable {
    public double[] attrSTD;
    public double[] mean;
    public double[] max;
    public double[] min;
    public double[] attrDifference;

    public AttributeStatsCache(Instances m_Data) {
        int m_numAttribs = m_Data.numAttributes();

		attrDifference = new double[m_numAttribs];
		attrSTD = new double[m_numAttribs]; // standard deviation
		mean = new double[m_numAttribs]; // mean
		max = new double[m_numAttribs];
		min = new double[m_numAttribs];

        Stats stats;
        //AttributeStatsCache attributeStats = null;
		for (int a = 0; a < m_numAttribs; a++) {
			if (!m_Data.attribute(a).isNominal()) {
                stats = m_Data.attributeStats(a).numericStats;
				min[a] = stats.min;
				max[a] = stats.max;
				mean[a] = stats.mean;
				attrDifference[a] = max[a] - min[a];
				attrSTD[a] = stats.stdDev;
				if (attrSTD[a]==0) attrSTD[a]=0.000001;
				if (attrDifference[a]==0) attrDifference[a]=1;

				//System.err.println(a+": "+min[a]+" "+max[a]+" "+attrSTD[a]+" "+attrDifference[a]);
			}
		}
    }
}
