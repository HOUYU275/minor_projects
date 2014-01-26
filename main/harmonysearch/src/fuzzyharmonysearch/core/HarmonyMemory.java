package fuzzyharmonysearch.core;

import fuzzy.FuzzyNumber;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19-Mar-2010
 * Time: 15:44:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class HarmonyMemory extends TreeSet<Harmony> {

    private TreeSet<Harmony> backupHarmonyMemory = new TreeSet<Harmony>();

    private double bestMerit = Double.MAX_VALUE;
    private int bestIteration = 0;

    private boolean fuzzify = true;

    private int additionCounter;
    private String intermediateResult;
    private boolean needCheck = true;

    private HarmonyComparator harmonyComparator;
    private Random random;

    private ValueRange[] parameterRanges;
    private int HMS;
    private double HMCR;
    private double PAR;
    private double BW;
    private int dimension;
    private boolean spread = false;
    private boolean influence = false;
    private Musician[] musicians;
    private double[][] historicalValues;
    private double[][] historicalAcceptedValues;

    public HarmonyMemory() {
        super();
    }

    public HarmonyMemory(ValueRange[] parameterRanges, ValueRange[] noteRanges, HarmonyComparator harmonyComparator, Random random) throws Exception {
        super();
        this.harmonyComparator = harmonyComparator;
        this.dimension = harmonyComparator.getDimension();
        this.random = random;
        this.parameterRanges = parameterRanges;
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
        this.musicians = new Musician[noteRanges.length];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(this, noteRanges[i]);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < musicians.length; i++) {
            musicians[i].clearNotes();
        }
        super.clear();
        if (fuzzify) backupHarmonyMemory.clear();
    }

    public void resetParameters() {
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
    }

    private void calculateParameters(int currentIteration, int maxIteration) {
        this.HMS = (int) (parameterRanges[0].getMin() + (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
        this.HMCR = parameterRanges[1].getMin() + (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
        this.PAR = parameterRanges[2].getMin() + (parameterRanges[2].getMax() - parameterRanges[2].getMin()) / maxIteration * currentIteration;
        this.BW = parameterRanges[3].getMax() * Math.exp(Math.log(parameterRanges[3].getMin() / parameterRanges[3].getMax()) / maxIteration * currentIteration);
    }

    public HarmonyComparator getHarmonyComparator() {
        return harmonyComparator;
    }

    public ValueRange[] getParameterRanges() {
        return parameterRanges;
    }

    public void setParameterRanges(ValueRange[] parameterRanges) {
        this.parameterRanges = parameterRanges;
    }

    public void setHarmonyComparator(HarmonyComparator harmonyComparator) {
        this.harmonyComparator = harmonyComparator;
    }

    public Harmony best() {
        return this.last();
    }

    public Harmony worst() {
        return this.first();
    }

    public void fill() throws Exception {
        while (this.size() < HMS) {
            Harmony randomHarmony = randomHarmony();
            while (harmonyComparator.checkConstraint(randomHarmony) != 0) {
                randomHarmony = randomHarmony();
            }
            this.add(randomHarmony);
        }
    }

    public void iterate(int maxIteration) throws Exception {

        historicalAcceptedValues = new double[musicians.length][maxIteration];
        int currentIteration = 0;
        while (currentIteration < maxIteration) {
            //System.out.println("Main " + this.size() + " - Backup " + backupHarmonyMemory.size());
            calculateParameters(currentIteration, maxIteration);
            Harmony newHarmony = newHarmony();
            while (harmonyComparator.checkConstraint(newHarmony) != 0) {
                newHarmony = newHarmony();
                System.out.print(".");
            }
            if (fuzzify) {
                this.fuzzyAdd(newHarmony);
                updateDistanceMesure(currentIteration, maxIteration);
            } else {
                this.add(newHarmony);
            }
            /*for (Note n : newHarmony.getNotes()) {
                System.out.println(n.getValue().getRepresentativeValue() + " " + n.getDistance() + " - ");
            }
            System.out.println(); */
            /*for (Musician musician : musicians) {
                System.out.println(musician.getNotes().size() + " - ");
            }
            System.out.println();*/
            record(newHarmony, currentIteration);
            currentIteration = currentIteration + 1;
            if (fuzzify) {
                if (checkConverge()) {
                    intermediateResult = intermediateResult +
                            (currentIteration + " " + best().getMerit().getRepresentativeValue()) + "\n";
                    /*for (Musician musician : musicians) {
                        musician.fillNoteDomain();
                    }*/
                    //fill();
                    //needCheck = false;
                }
            }
            recordMerit(best().getMerit().getRepresentativeValue(), currentIteration);
        }
    }

    @Override
    public boolean add(Harmony harmony) {
        try {
            harmonyComparator.evaluate(harmony);
            //System.out.println(harmony.getMerit().getRepresentativeValue());
        } catch (Exception e) {
            e.printStackTrace();  //TODO: Automatically Generated Catch Statement
        }
        if (super.size() < HMS) {
            for (int i = 0; i < harmony.getSize(); i++) {
                musicians[i].addNote(harmony.getNote(i));
            }
            super.add(harmony);
            return true;
        } else if (harmonyComparator.compare(harmony, worst()) > 0) {
            for (int i = 0; i < harmony.getSize(); i++) {
                musicians[i].replaceNote(worst().getNote(i), harmony.getNote(i));
            }
            super.remove(worst());
            super.add(harmony);
            return true;
        }
        return false;
    }

    private boolean fuzzyAdd(Harmony harmony) {
        boolean returnValue = false;
        try {
            harmonyComparator.evaluate(harmony);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (super.size() < HMS) {
            for (int i = 0; i < harmony.getSize(); i++) {
                musicians[i].addNote(harmony.getNote(i));
            }
            super.add(harmony);
            returnValue = true;
        } else if (harmonyComparator.compare(harmony, this.worst()) > 0) {
            for (int i = 0; i < harmony.getSize(); i++) {
                musicians[i].replaceNote(worst().getNote(i), harmony.getNote(i));
            }
            super.remove(worst());
            super.add(harmony);
            returnValue = true;
        } else {
            for (int i = 0; i < harmony.getSize(); i++) {
                musicians[i].addNote(harmony.getNote(i));
            }
            backupHarmonyMemory.add(harmony);
            returnValue = false;
        }
        return returnValue;
    }

    public boolean checkConverge() {
        //if (needCheck) {
        boolean converged = true;
        for (Musician musician : musicians) {
            converged = converged & (musician.getNotes().size() == 0);
        }
        return converged;
        //}
        //return false;
    }

    private void record(Harmony harmony, int currentIteration) {
        for (int i = 0; i < musicians.length; i++) {
            historicalAcceptedValues[i][currentIteration] = harmony.getDoubleNotes()[i];
        }
    }

    /*public void sort() {
        Collections.sort(this, harmonyComparator);
    }*/

    public void resize(int newNumHarmonies) throws Exception {
        if (newNumHarmonies < HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            //subtraction case
            HMS = newNumHarmonies;
            while (this.size() > HMS) {
                //this.remove(this.first());
                this.remove(0);
            }
        } else if (newNumHarmonies > HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            //addition case
            HMS = newNumHarmonies;
        }
    }

    public double getPAR() {
        return PAR;
    }

    public void setPAR(double PAR) {
        this.PAR = PAR;
    }

    public double getHMCR() {
        return HMCR;
    }

    public void setHMCR(double HMCR) {
        this.HMCR = HMCR;
    }

    public double getBW() {
        return BW;
    }

    public void setBW(double BW) {
        this.BW = BW;
    }

    public void printHarmonies() {
        for (Harmony harmony : this) {
            System.out.print("(");
            for (FuzzyNumber merit : harmony.getMerits()) {
                System.out.print(" " + merit + " of " + merit.getRepresentativeValue());
            }
            try {
                System.out.print(" ) + [" + this.harmonyComparator.checkConstraint(harmony) + "]" + harmony.toString());
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println();
        }
    }

    public int getNumMusicians() {
        //return numMusicians;
        return musicians.length;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Musician[] getMusicians() {
        return musicians;
    }

    public void setMusicians(Musician[] musicians) {
        this.musicians = musicians;
    }

    public int getHMS() {
        return HMS;
    }//true if added and false otherwise

    public void setHMS(int HMS) {
        this.HMS = HMS;
    }

    /*public boolean evaluateAndAdd(Harmony harmony) throws Exception {
        harmonyComparator.evaluate(harmony);
        return add(harmony);
    }*/

    //TODO: need the multi-D version now!

    /*public boolean add(Harmony e) {
        if (contains(e)) {
            return false;
        } else if (super.size() < HMS) {
            for (int i = 0; i < e.getSize(); i++) {
                musicians[i].addNote(e.getNote(i));
            }
            super.add(e);
            sort();
            return true;
        } else if (harmonyComparator.compare(e, this.get(0)) > 0) {
            //System.out.println("add");
            additionCounter++;
            for (int i = 0; i < e.getSize(); i++) {
                if (fuzzify) {
                    musicians[i].addNote(e.getNote(i));
                    //e.getNote(i).setDistance(1);
                    if (this.get(0).getNote(i).qualityDecrease()) musicians[i].removeNote(this.get(0).getNote(i));
                }
                else {
                    musicians[i].replaceNote(this.get(0).getNote(i), e.getNote(i));
                }
            }
            super.remove(0);
            super.add(e);
            sort();
            return true;
        } else {
            for (int i = 0; i < e.getSize(); i++) {
                if (fuzzify) {
                    e.getNote(i).qualityDecrease();
                    //if (e.getNote(i).qualityDecrease()) musicians[i].removeNote(e.getNote(i));
                }
                else {
                    //musicians[i].removeNote(e.getNote(i));
                }
                //musicians[i].replaceNote(this.get(0).getNote(i), e.getNote(i));
            }
            return false;
        }
    }
*/

    /*public boolean add(Harmony e) {
        Harmony tempHarmony = null;
        if (contains(e)) {
            return false;
        } else if (super.size() < HMS) {
            for (int i = 0; i < e.getSize(); i++) {
                musicians[i].addNote(e.getNote(i));
            }
            super.add(e);
            sort();
            return true;
        } else if (harmonyComparator.compare(e, this.get(0)) > 0) {
            //System.out.println("add");
            additionCounter++;
            for (int i = 0; i < e.getSize(); i++) {
                if (fuzzify) {
                    musicians[i].addNote(e.getNote(i));
                    //e.getNote(i).setDistance(1);
                    //if (this.get(0).getNote(i).qualityDecrease())
                    musicians[i].removeNote(this.get(0).getNote(i));
                    tempHarmony = this.get(0);
                } else {
                    musicians[i].replaceNote(this.get(0).getNote(i), e.getNote(i));
                }
            }
            super.remove(0);
            super.add(e);
            sort();
            *//*for (int i = 0; i < e.getSize(); i++) {
                if (fuzzify) {
                    if (calculateDistance(tempHarmony) == 0) {
                        musicians[i].removeNote(tempHarmony.getNote(i));
                    }
                    else {
                        tempHarmony.getNote(i).setDistance(calculateDistance(tempHarmony));
                    }

                }
            }*//*
            return true;
        } else {
            //System.out.println("Distance = " + calculateDistance(e));
            for (int i = 0; i < e.getSize(); i++) {
                if (fuzzify) {
                    if (calculateDistance(e) == 0) {
                        musicians[i].removeNote(e.getNote(i));
                    } else {
                        e.getNote(i).setDistance(calculateDistance(e));
                    }
                    //e.getNote(i).qualityDecrease();

                    //if (e.getNote(i).qualityDecrease()) musicians[i].removeNote(e.getNote(i));
                }
                //else {
                //musicians[i].removeNote(e.getNote(i));
                //}
                //musicians[i].replaceNote(this.get(0).getNote(i), e.getNote(i));
            }
            return false;
        }
    }
*/

    public void updateDistanceMesure(int currentIteration, int maxIteration) {
        Iterator<Harmony> iterator = backupHarmonyMemory.iterator();
        double distance;
        while (iterator.hasNext()) {
            Harmony harmony = iterator.next();
            distance = calculateDistance(harmony, currentIteration, maxIteration);
            if (distance == 0) {
                for (int i = 0; i < harmony.getSize(); i++) {
                    musicians[i].removeNote(harmony.getNote(i));

                    //System.out.println(musicians[i].getNotes().size() + " ");
                }
                //System.out.println();
                iterator.remove();
            } else {
                harmony.setDistance(distance);
            }
            //System.out.println(distance);

        }
        //System.out.println(backupHarmonyMemory.size());
    }

    public double calculateDistance(Harmony harmony, int currentIteration, int maxIteration) {
        double distance = 0d;
        double bestMerit = best().getMerit().getRepresentativeValue();
        double worstMerit = worst().getMerit().getRepresentativeValue();
        double range = Math.abs(bestMerit - worstMerit) * (maxIteration - currentIteration) / maxIteration;
        double difference = Math.abs(harmony.getMerit().getRepresentativeValue() - worstMerit);
        distance = (difference > range) ? 0 : (range - difference) / range;
        return distance;
    }

    public double calculateDistance(double bestMerit, double worstMerit, double currentMerit) {
        double distance = 0d;
        //double bestMerit = this.get(this.size() - 1).getMerit().getRepresentativeValue();
        //double worstMerit = this.get(0).getMerit().getRepresentativeValue();
        double range = Math.abs(bestMerit - worstMerit);
        double difference = Math.abs(currentMerit - worstMerit);
        distance = (difference > range) ? 0 : (range - difference) / range;
        return distance;
    }

    public int getAdditionCounter() {
        return additionCounter;
    }

    public void setAdditionCounter(int additionCounter) {
        this.additionCounter = additionCounter;
    }

    public boolean remove(Harmony e) {
        if (!super.contains(e)) return false;
        for (int i = 0; i < e.getSize(); i++) {
            musicians[i].removeNote(e.getNote(i));
            super.remove(e);
            //sort();
            return true;
        }
        return false;
    }

    public Harmony newHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < musicians.length; i++) {
            harmony.setNote(i, musicians[i].pickNote(harmony));
        }
        return harmony;
    }

    public Harmony randomHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < musicians.length; i++) {
            harmony.setNote(i, musicians[i].randomNote(harmony));
        }

        return harmony;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public void reset() {
        for (int i = 0; i < musicians.length; i++) {
            musicians[i].clearNotes();
            musicians[i].setBestNote(null);
        }
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
        super.clear();
    }

    public boolean isInfluence() {
        return influence;
    }

    public void setInfluence(boolean influence) {
        this.influence = influence;
    }

    public double[][] getHistoricalValues() {
        return historicalValues;
    }

    public double[][] getHistoricalAcceptedValues() {
        return historicalAcceptedValues;
    }

    public boolean isFuzzify() {
        return fuzzify;
    }

    public void setFuzzify(boolean fuzzify) {
        this.fuzzify = fuzzify;
    }

    public String getIntermediateResult() {
        return intermediateResult;
    }

    public void setIntermediateResult(String intermediateResult) {
        this.intermediateResult = intermediateResult;
    }

    public void recordMerit(double merit, int iteration) {
        if (merit < bestMerit) {
            bestMerit = merit;
            bestIteration = iteration;
        }
    }

    public double getBestMerit() {
        return bestMerit;
    }

    public void setBestMerit(double bestMerit) {
        this.bestMerit = bestMerit;
    }

    public int getBestIteration() {
        return bestIteration;
    }

    public void setBestIteration(int bestIteration) {
        this.bestIteration = bestIteration;
    }

    //true = do HMCR
    //false = no HMCR - straight pick
    public boolean testHMCR() {
        return random.nextDouble() > HMCR;
    }

    //true = do PAR
    public boolean testPAR() {
        return random.nextDouble() < PAR;
    }

    public TreeSet<Harmony> getBackupHarmonyMemory() {
        return backupHarmonyMemory;
    }

    public void setBackupHarmonyMemory(TreeSet<Harmony> backupHarmonyMemory) {
        this.backupHarmonyMemory = backupHarmonyMemory;
    }
}
