package weka.fuzzy.measure;

import java.util.ArrayList;
import java.util.BitSet;

import weka.core.Instances;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;


public class Gamma extends FuzzyMeasure  implements TechnicalInformationHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1063606253458807903L;

	public Gamma() {
		super();
	}




	@Override
	public double calculate(BitSet subset) {
		generatePartition(subset);

		double lower;
		double currLower = 0;
		double ret = 0;
		double val = 0;

		// for optimization
		indexes = new int[m_numInstances];
		vals = new double[m_numInstances];

		// optimisation - see if other objects in the dataset are identical/have
		// identical similarity
		setIndexes(current);

		for (int x = 0; x < m_numInstances; x++) {
			if (indexes[x] != 0) {
				vals[x] = vals[indexes[x] - 1];
			} else {
				val = 0;
				decVals = new double[m_numInstances];
				for (int d = 0; d < m_numInstances; d++) {
					if (decIndexes[d] != 0) {
						decVals[d] = decVals[decIndexes[d] - 1];
					} else {
						lower = 1;

						// lower approximations of object x
						for (int y = 0; y < m_numInstances; y++) {
							double condSim = current.getCell(x, y);

							currLower = m_Implicator.calculate(condSim, fuzzySimilarity(m_classIndex, d, y));

							lower = Math.min(currLower, lower);
							if (lower == 0)
								break;
						}


						decVals[d] = lower;
					}
					val = Math.max(decVals[d], val);

					if (val == 1) break;

				}

				vals[x] = val;

			}

			ret += vals[x];
			mems[x]=vals[x];
		}

		return ret / n_objects_d;
	}


    public final void generatePartition(BitSet reduct, Instances instances) {
        int nextBit = reduct.nextSetBit(0);
        if (nextBit==m_classIndex) nextBit=reduct.nextSetBit(nextBit+1);

        for (int i = 0; i < m_numInstances; i++) {

            current.setCell(i, i, 1);

            for (int j = i + 1; j < m_numInstances; j++) {
                double rel = fuzzySimilarity(nextBit, i, j);

                current.setCell(i, j, rel);
                //current.setCell(j, i, rel);

            }
        }


        if (reduct.cardinality() > 1) {
            for (int o = 0; o < m_numInstances; o++) {
                for (int o1 = o + 1; o1 < m_numInstances; o1++) {
                    for (int a = reduct.nextSetBit(nextBit + 1); a >= 0; a = reduct.nextSetBit(a + 1)) {
                        if (a!=m_classIndex) {
                            double rel = m_composition.calculate(fuzzySimilarity(a, o, o1), current.getCell(o, o1));
                            current.setCell(o, o1, rel);
                            //current.setCell(o1, o, rel);
                            if (rel == 0) break;
                        }
                    }
                }
            }
        }
    }

    public double calculate(BitSet subset, Instances instances) {
        generatePartition(subset, instances);

        double lower;
        double currLower = 0;
        double ret = 0;
        double val = 0;

        // for optimization
        indexes = new int[m_numInstances];
        vals = new double[m_numInstances];

        // optimisation - see if other objects in the dataset are identical/have
        // identical similarity
        setIndexes(current);

        for (int x = 0; x < m_numInstances; x++) {
            if (indexes[x] != 0) {
                vals[x] = vals[indexes[x] - 1];
            } else {
                val = 0;
                decVals = new double[m_numInstances];
                for (int d = 0; d < m_numInstances; d++) {
                    if (decIndexes[d] != 0) {
                        decVals[d] = decVals[decIndexes[d] - 1];
                    } else {
                        lower = 1;

                        // lower approximations of object x
                        for (int y = 0; y < instances.numInstances(); y++) {
                            double condSim = current.getCell(x, y);

                            currLower = m_Implicator.calculate(condSim, fuzzySimilarity(m_classIndex, d, y));

                            lower = Math.min(currLower, lower);
                            if (lower == 0)
                                break;
                        }


                        decVals[d] = lower;
                    }
                    val = Math.max(decVals[d], val);

                    if (val == 1) break;

                }

                vals[x] = val;

            }

            ret += vals[x];
            mems[x]=vals[x];
        }

        return ret / m_numInstances;
    }

    /**
	 * Returns an instance of a TechnicalInformation object, containing
	 * detailed information about the technical background of this class,
	 * e.g., paper reference or book this class is based on.
	 *
	 * @return the technical information about this class
	 */
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation        result;
		TechnicalInformation        additional;

		result = new TechnicalInformation(Type.ARTICLE);
		result.setValue(Field.AUTHOR, "R. Jensen, Q. Shen");
		result.setValue(Field.YEAR, "2009");
		result.setValue(Field.TITLE, "New Approaches to Fuzzy-rough Feature Selection. IEEE Transactions on Fuzzy Systems");
		result.setValue(Field.SCHOOL, "Aberystwyth University");

		additional = result.add(Type.INPROCEEDINGS);
		additional.setValue(Field.AUTHOR, "C. Cornelis, G. Hurtado Martin, R. Jensen and D. Slezak");
		additional.setValue(Field.TITLE, "Feature Selection with Fuzzy Decision Reducts");
		additional.setValue(Field.BOOKTITLE, "Third International Conference on Rough Sets and Knowledge Technology (RSKT'08)");
		additional.setValue(Field.YEAR, "2008");
		additional.setValue(Field.PAGES, "284-291");
		additional.setValue(Field.PUBLISHER, "Springer");

		return result;
	}

	@Override
	public String globalInfo() {
		return "Gamma evaluator, using the fuzzy rough lower approximations.\n\n"
		+  getTechnicalInformation().toString();
	}

	public String toString() {
		return "Gamma";
	}
}
