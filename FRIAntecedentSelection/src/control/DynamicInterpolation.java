package control;

import model.*;
import test.Main;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:41
 */
public class DynamicInterpolation {

    protected Block[] originalBlocks;

    public Block[] generateBlocks(RuleBase base, int numIntervals) {
        int numAntecedents = base.getMinimums().length;
        Block[] blocks = new Block[(int) Math.pow(numIntervals, numAntecedents)];
        for (int b = 0; b < blocks.length; b++) {
            blocks[b] = new Block(numAntecedents);
            int temp = b;
            for (int a = 0; a < numAntecedents; a++) {
                blocks[b].getMinimums()[a] = base.getMinimums()[a] + base.getRange(a) * (temp % numIntervals) / numIntervals;
                blocks[b].getMaximums()[a] = base.getMinimums()[a] + base.getRange(a) * (temp % numIntervals + 1) / numIntervals;
                temp /= numIntervals;
            }
        }
        return blocks;
    }

    public Block[] assignBlocks(RuleBase base, Block[] blocks) {
        int numAntecedents = base.getMinimums().length;
        for (Block block : blocks) {
            for (Rule rule : base) {
                boolean add = true;
                for (int a = 0; a < numAntecedents; a++)
                    add = add & ((rule.getAntecedents()[a].rep() > block.getMinimums()[a]) && (rule.getAntecedents()[a].rep() < block.getMaximums()[a]));
                if (add) block.add(rule);
            }
        }
        return blocks;
    }

    public Clusters getClusters(Block block, int k) {

        Random random = new Random();
        Clusters clusters = new Clusters(k);
        for (int c = 0; c < k; c++) {
            Cluster cluster = new Cluster(block.get(random.nextInt(block.size())));
            clusters.getClusters()[c] = cluster;
            //System.out.println("Cluster " + c + " " + clusters.getClusters()[c]);
        }
        do {
            clusters.updateCentroids();
            for (Cluster c : clusters.getClusters()) c.clear();
            for (Rule rule : block) {
                double min = Double.MAX_VALUE;
                Cluster bestCluster = null;
                for (Cluster c : clusters.getClusters()) {
                    if (rule.distanceTo(c.getCentroid()) < min) {
                        min = rule.distanceTo(c.getCentroid());
                        bestCluster = c;
                    }
                }
                if (bestCluster != null) bestCluster.add(rule);
            }
        } while (!clusters.isConverged());
        for (int c = 0; c < k; c++) if (clusters.getClusters()[c].isEmpty()) return getClusters(block, k);
        //for (int c = 0; c < k; c++) System.out.println("Cluster " + c + " (" + clusters.clusters[c].size() + ") " + clusters.clusters[c]);
        //System.out.println("Block Size = " + block.size());
        //for (Rule rule : block) System.out.println(rule);
        //System.out.println("K = " + k);
        for (int c = 0; c < k; c++) {
            //System.out.println("Cluster " + c + " (" + clusters.getClusters()[c].size() + ")");
            //for (Rule rule : clusters.getClusters()[c]) System.out.println(rule);
        }
        return clusters;
    }

    public RuleBase fill(RuleBase original, RuleBase interpolated, int size) {
        while (interpolated.size() < size) try {
            Rule observation = original.getRuleBaseGenerator().generateObservation(original, false);
            Rule interpolatedRule = null;
            if (original.check(observation) != null) interpolatedRule = original.check(observation);
            else interpolatedRule = TransformationBasedInterpolation.interpolate(original, observation);
            double error = interpolatedRule.getConsequent().rep() - original.getRuleBaseGenerator().getFunction().calculate(interpolatedRule.getAntecedents());
            //System.out.println(error / 20);
            Main.t.push(Math.abs(error / 20));
            interpolated.add(interpolatedRule);
            //System.out.println(interpolated.size() + " " + interpolated.get(interpolated.size() - 1));
        } catch (Exception e) {
            //System.err.println("No Matching Rules, Skipped Observation.");
        }
        return interpolated;
    }

    public Clusters iterativeClustering(Block block) {
        Clusters bestClusters = null;
        //System.out.println(block);
        int k = block.size() / 2 + 1;
        double bestDunnIndex = -Double.MAX_VALUE;
        do {
            Clusters clusters = getClusters(block, k);
            double dunnIndex = clusters.getDunnIndex();
            //System.out.println("Dunn Index (k = " + k + "): " + dunnIndex);
            if (bestDunnIndex < dunnIndex) {
                bestDunnIndex = dunnIndex;
                bestClusters = clusters;
            }
        } while (k-- > 2);
        return bestClusters;
    }

    public Block[] getBlocks(RuleBase base, int numIntervals) {
        Block[] blocks = generateBlocks(base, numIntervals);
        assignBlocks(base, blocks);
        /*for (Block block : blocks) {
            System.out.println(block);
            for (Rule rule : block) System.out.println(rule);
        }*/
        return blocks;
    }

    public int dynamicInterpolation(RuleBase original, RuleBase interpolated, int numIntervals, int targetBlockSize) {
        Block[] blocks = getBlocks(interpolated, numIntervals);
        int count = 0;
        originalBlocks = getBlocks(original, numIntervals);
        for (Block block : originalBlocks) {
            if (block.size() >= targetBlockSize) {
                //System.out.println("[*] " + block);
                count++;
            }
            //else System.out.println("[ ] " + block);
        }
        for (int b = 0; b < blocks.length; b++) {
            if (originalBlocks[b].size() < targetBlockSize && blocks[b].size() > targetBlockSize) {
                Clusters bestClusters = iterativeClustering(blocks[b]);
                if (bestClusters != null && bestClusters.getLargest().size() >= 2) {
                    //System.out.println(bestClusters.report());
                    Rule merged = bestClusters.getLargest().merge();
                    //System.out.println(validate(original, merged));
                    original.add(merged);
                    for (Rule rule : bestClusters.getLargest()) interpolated.remove(rule);
                }
            }
        }
        return count;
    }

    public String validate(RuleBase original, Rule merged) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(merged).append("\t");
        //System.out.println("New Rule\t" + merged);
        //System.out.println("Offset to Ground Truth: " +

        try {
            Rule mergedInterpolated = TransformationBasedInterpolation.interpolate(original, merged);
            //System.out.println("Interpolation: \t" + mergedInterpolated);
            double offsetInterpolation = Math.abs(mergedInterpolated.getConsequent().rep() - merged.getConsequent().rep());
            buffer.append(offsetInterpolation).append("\t");
        } catch (Exception e) {
            buffer.append("error").append("\t").append("error").append("\t");
        }
        double groundTruth = original.getRuleBaseGenerator().getFunction().calculate(merged.reps());
        //System.out.println("Ground Truth: \t" + groundTruth);
        double offsetGroundTruth = Math.abs(original.getRuleBaseGenerator().getFunction().calculate(merged.reps()) - merged.getConsequent().rep());
        buffer.append(offsetGroundTruth).append("\t");
        try {
            Rule mergedInterpolated = TransformationBasedInterpolation.interpolate(original, merged);
            double interpolationToGroundTruth = Math.abs(original.getRuleBaseGenerator().getFunction().calculate(merged.reps()) - mergedInterpolated.getConsequent().rep());
            buffer.append(interpolationToGroundTruth).append("\t");
        } catch (Exception e) {
            buffer.append("error").append("\t").append("error").append("\t");
        }
        return buffer.toString();
        //System.out.println("- - -");
    }

}
