import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21/11/11
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class SingleImageDownloader implements Runnable {
	private String imageUrl;
	private String destinationFile;
	private int pictureIndex;

	SingleImageDownloader(String imageUrl, String destinationFile, int pictureIndex) {
		this.imageUrl = imageUrl;
		this.destinationFile = destinationFile;
		this.pictureIndex = pictureIndex;
	}

	public void run() {
		try {
			Connect.saveImage(imageUrl, destinationFile);
			System.out.print(pictureIndex + " ");
		} catch (IOException e) {
			e.printStackTrace();  //TODO: Automatically Generated Catch Statement
		}
	}
}