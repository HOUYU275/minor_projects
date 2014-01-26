package survey;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 06/12/12
 * Time: 18:56
 */
public class FeatureSelectionResult implements Serializable {
    private static final long serialVersionUID = 2658873575874809677L;
    private String dataset;
	private int topRepeat;
	private int fold;
	private int botRepeats;
	private String search;
	private String evaluator;

	private FeatureSelectionDetail[] results;

	public FeatureSelectionResult(String dataset, int topRepeat, int fold, int botRepeats, String search, String evaluator) {
		this.dataset = dataset;
		this.topRepeat = topRepeat;
		this.fold = fold;
		this.botRepeats = botRepeats;
		this.search = search;
		this.evaluator = evaluator;
		this.results = new FeatureSelectionDetail[botRepeats];
	}

	/*public void set(int top, int fold, int bot, FeatureSelectionDetail featureSelectionDetail) {
		results[top][fold][bot] = featureSelectionDetail;
	}

	public void set(int top, int fold, int bot, double merit, int size, long time, int[] subset) {
		results[top][fold][bot] = new FeatureSelectionDetail(merit, size, time, subset);
	}

    public FeatureSelectionDetail get(int top, int fold, int bot) {
        return results[top][fold][bot];
    }*/

    public void set(int bot, FeatureSelectionDetail featureSelectionDetail) {
        results[bot] = featureSelectionDetail;
    }

    public void set(int bot, double merit, int size, long time, int[] subset) {
        results[bot] = new FeatureSelectionDetail(merit, size, time, subset);
    }

    public FeatureSelectionDetail get(int bot) {
        return results[bot];
    }

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public int getTopRepeat() {
		return topRepeat;
	}

	public void setTopRepeat(int topRepeat) {
		this.topRepeat = topRepeat;
	}

	public int getFold() {
		return fold;
	}

	public void setFold(int fold) {
		this.fold = fold;
	}

	public int getBotRepeats() {
		return botRepeats;
	}

	public void setBotRepeats(int botRepeats) {
		this.botRepeats = botRepeats;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(String evaluator) {
		this.evaluator = evaluator;
	}

	public FeatureSelectionDetail[] getResults() {
		return results;
	}

	public void setResults(FeatureSelectionDetail[] results) {
		this.results = results;
	}

    @Override
    public String toString() {
        String s = "";
        for (FeatureSelectionDetail d : results) s += "\t" + d;
        return s;
    }

    public FeatureSelectionDetailDouble aggregate() {
        FeatureSelectionDetailDouble detail = new FeatureSelectionDetailDouble();
        double sumMerit = 0;
        double sumSize = 0d;
        long sumTime = 0;
        for (FeatureSelectionDetail result : results) {
            sumMerit += result.merit;
            sumSize += result.size;
            sumTime += result.time;
        }
        detail.setMerit(sumMerit / results.length);
        detail.setSize(sumSize / results.length);
        detail.setTime(sumTime / results.length);
        return detail;
    }
}
