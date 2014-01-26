package weka.attributeSelection;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 08/12/12
 * Time: 12:59
 */
public class AntColonySearch extends ASSearch implements OptionHandler, TechnicalInformationHandler {

    private static final long serialVersionUID = -4747744394262415727L;
    private SubsetEvaluator evaluator;
    private Random random;
    private int seed;
    private int numAttributes;

    private BitSet best = null;
    private double bestMerit = -Double.MAX_VALUE;

    private double initialPheromone = 0.5;
    private Graph graph;
    private double alpha = 1;
    private double beta = 2;
    private double evaporationRate = 0.8;
    private int numAnts = 20;
    private int numGenerations = 500;

    private Ant[] ants;

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {

        try {
            numAttributes = data.numAttributes() - 1;
            evaluator = NatureInspiredCommon.convert(ASEvaluator);
            best = new BitSet(numAttributes);
            bestMerit = -Double.MAX_VALUE;
            random = new Random(seed);
            numGenerations = 1000;
            if (evaluator instanceof  InterruptingEvaluator) ((InterruptingEvaluator) evaluator).turnOff();
            if (evaluator instanceof FuzzyRoughSubsetEval) numGenerations /= 5;

            if (graph == null) graph = new Graph(numAttributes, evaluator, initialPheromone);
            ants = new Ant[numAnts];
            for (int a = 0; a < numAnts; a++) ants[a] = new Ant();
            if (evaluator instanceof  InterruptingEvaluator) ((InterruptingEvaluator) evaluator).reset();
            int currentGeneration = 0;
            do {
                graph.evaporate(evaporationRate, false);
                for (Ant a : ants) a.move();
                for (Ant a : ants) {
                    a.offlineUpdate();
                    if (bestMerit < a.getMerit()) {
                        bestMerit = a.getMerit();
                        best = a.toBitSet();
                    } else if ((bestMerit == a.getMerit()) && best.cardinality() > a.size()) {
                        bestMerit = a.getMerit();
                        best = a.toBitSet();
                    }
                }
            } while (currentGeneration++ < numGenerations);
        } catch (MaximumEvaluationReachedException e) {
            return NatureInspiredCommon.toIntArray(best, numAttributes);
        }

        return NatureInspiredCommon.toIntArray(best, numAttributes);
    }

    @Override
    public Enumeration listOptions() {
        return null;
    }

    @Override
    public void setOptions(String[] options) throws Exception {

    }

    @Override
    public String[] getOptions() {
        return new String[0];
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        return null;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setEvaporationRate(double evaporationRate) {
        this.evaporationRate = evaporationRate;
    }

    public void setNumAnts(int numAnts) {
        this.numAnts = numAnts;
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
    }

    protected class Graph {
        private int dimension;
        private double[][] heuristicValues;
        private double[][] pheromoneValues;

        public Graph(int dimension, SubsetEvaluator evaluator, double initialPheromone) throws Exception {
            heuristicValues = new double[dimension][dimension];
            pheromoneValues = new double[dimension][dimension];
            //ThreadPool threadPool = new ThreadPool(2000, 2000);
            for (int i = 0; i < dimension - 1; i++) {
                for (int j = i + 1; j < dimension; j++) {
                    BitSet set = new BitSet(dimension);
                    set.set(i);
                    set.set(j);
                    //Future<Double> result = threadPool.submitSubsetEvaluation(new ThreadedSubsetEvaluator(evaluator, set));
                    pheromoneValues[i][j] = initialPheromone;
                    heuristicValues[i][j] = evaluator.evaluateSubset(set);
                }
            }
            //threadPool.shutdown();
        }

        public Graph() {
            heuristicValues = new double[dimension][dimension];
            pheromoneValues = new double[dimension][dimension];
        }

        public void initialise() throws Exception {
            for (int i = 0; i < numAttributes - 1; i++) {
                for (int j = i + 1; j < numAttributes; j++) {
                    BitSet set = new BitSet(numAttributes);
                    set.set(i);
                    set.set(j);
                    heuristicValues[i][j] = evaluator.evaluateSubset(set);
                    pheromoneValues[i][j] = initialPheromone;
                }
            }
        }

        public double getHeuristicValue(int i, int j) throws Exception {
            if (i == j) throw new Exception("Invalid Edge");
            return (i < j) ? heuristicValues[i][j] : heuristicValues[j][i];
        }

        public double getPheromoneValue(int i, int j) throws Exception {
            if (i == j) throw new Exception("Invalid Edge");
            return (i < j) ? pheromoneValues[i][j] : pheromoneValues[j][i];
        }

        public void setHeuristicValue(int i, int j, double value) throws Exception {
            if (i == j) throw new Exception("Invalid Edge");
            if (i < j) heuristicValues[i][j] = value;
            else heuristicValues[j][i] = value;
        }

        public void setPheromoneValue(int i, int j, double value) throws Exception {
            if (i == j) throw new Exception("Invalid Edge");
            if (i < j) pheromoneValues[i][j] = value;
            else pheromoneValues[j][i] = value;
        }

        public void addPheromoneValue(int i, int j, double value) throws Exception {
            if (i == j) throw new Exception("Invalid Edge");
            if (i < j) pheromoneValues[i][j] += value;
            else pheromoneValues[j][i] += value;
        }

        public void evaporate(int i, int j, double rate) throws Exception {
            if (i == j) throw new Exception("Invalid Edge");
            if (i < j) pheromoneValues[i][j] *= rate;
            else pheromoneValues[j][i] *= rate;
        }

        public void evaporate(double rate) throws Exception {
            for (int i = 0; i < numAttributes - 1; i++) {
                for (int j = i + 1; j < numAttributes; j++) {
                    pheromoneValues[i][j] *= rate;
                }
            }
        }

        public void evaporate(double rate, boolean normalise) throws Exception {
            double sum = 0;
            double top = -Double.MAX_VALUE;
            for (int i = 0; i < numAttributes - 1; i++) {
                for (int j = i + 1; j < numAttributes; j++) {
                    pheromoneValues[i][j] *= rate;
                    if (normalise) top = (top < pheromoneValues[i][j] ? pheromoneValues[i][j] : top);
                }
            }
            if (!normalise) return;
            for (int i = 0; i < numAttributes - 1; i++) {
                for (int j = i + 1; j < numAttributes; j++) {
                    pheromoneValues[i][j] = pheromoneValues[i][j] / top;
                    pheromoneValues[i][j] = pheromoneValues[i][j] < 0.001 ? 0.001 : pheromoneValues[i][j];
                }
            }
        }

        public void evaporate() throws Exception {
            for (int i = 0; i < numAttributes - 1; i++) {
                for (int j = i + 1; j < numAttributes; j++) {
                    pheromoneValues[i][j] *= evaporationRate;
                }
            }
        }

        public int getDimension() {
            return dimension;
        }

        public void setDimension(int dimension) {
            this.dimension = dimension;
        }
    }

    protected class Ant extends Vector<Integer> {
        private static final long serialVersionUID = -4896283754166904888L;
        private double merit;

        public Ant() {
            super();
        }

        public void assignMerit() throws Exception {
            merit = NatureInspiredCommon.getMerit(toBitSet(), evaluator, numAttributes);
        }

        public BitSet toBitSet() {
            BitSet bitSet = new BitSet(numAttributes);
            for (Integer i : this) bitSet.set(i);
            return bitSet;
        }

        public void move() throws Exception {
            clear();
            int randomStart = random.nextInt(numAttributes);
            int previous;
            if (best.nextSetBit(randomStart) < 0) previous = randomStart;
            else previous = best.nextSetBit(randomStart);
            add(previous);
            int next;
            while (size() < numAttributes) {
                BitSet set = toBitSet();
                next = getNext(set, previous);
                if (next < 0) break;
                double score = NatureInspiredCommon.getMerit(set, evaluator, numAttributes);
                if (score <= merit) break;
                merit = score;
                add(next);
                onlineUpdate(previous, next);
                previous = next;
            }
        }

        public boolean canContinue() throws Exception {
            return (size() < numAttributes);// || (!((merit < bestMerit) || ((merit == bestMerit) && (size() < best.cardinality()))));
        }

        private int getNext(BitSet current, int feature) throws Exception {
            double score = 0;
            double[] scores = new double[numAttributes];
            for (int f = current.nextClearBit(0); f < numAttributes && f != -1; f = current.nextClearBit(f + 1)) {
                //score += Math.pow(graph.getPheromoneValue(feature, f), alpha) * Math.pow(graph.getHeuristicValue(feature, f), beta);
                score += graph.getPheromoneValue(feature, f) * Math.pow(graph.getHeuristicValue(feature, f), beta);
                scores[f] = score;
            }
            double probability = random.nextDouble() * score;
            for (int f = current.nextClearBit(0); f < numAttributes && f != -1; f = current.nextClearBit(f + 1)) {
                if (scores[f] >= probability) return f;
            }
            System.out.println(score);
            return -1;
        }

        private void onlineUpdate(int i, int j) throws Exception {
            graph.setPheromoneValue(i, j, (1 - merit) * 0.5 + merit * graph.getPheromoneValue(i, j));
        }

        public void offlineUpdate() throws Exception {
            for (int i = 0; i < this.size() - 1; i++) {
                for (int j = i + 1; j < this.size(); j++) {
                    graph.addPheromoneValue(i, j, merit);
                }
            }
        }

        public double getMerit() {
            return merit;
        }

        @Override
        public void clear() {
            super.clear();
            merit = -Double.MAX_VALUE;
        }
    }

    public void reset() {
        graph = null;
    }
}
