package originalharmonysearch.core;

/**
 * Created by IntelliJ IDEA.
 * User: endario
 * Date: 28/10/2012
 * Time: 18:26
 */
public class ImprovisationThread implements Runnable {
	private HarmonyMemory harmonyMemory;
	private int currentIteration;
	private int maxIteration;

	/*public ImprovisationThread(int currentIteration, int maxIteration, HarmonyMemory harmonies) {
		this.currentIteration = currentIteration;
		this.maxIteration = maxIteration;
		this.harmonyMemory = harmonies;
	}

	@Override
	public void run() {
		System.out.println(currentIteration);
		harmonyMemory.calculateParameters(currentIteration, maxIteration);
		try {
			harmonyMemory.resize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Harmony newHarmony = harmonyMemory.newHarmony();
		try {
			while (harmonyMemory.getHarmonyComparator().checkConstraint(newHarmony) != 0) {
				newHarmony = harmonyMemory.newHarmony();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			harmonyMemory.evaluateAndAdd(newHarmony);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public void run() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
