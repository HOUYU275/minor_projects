import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

import java.io.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 13/11/2013
 * Time: 12:31
 */
public class Main {

    public static void main(String[] args) throws WeiboException, IOException {
        String token = "2.00P2dl2DiiT81B1eb1bcde8cQiDNaD";
        Timeline tm = new Timeline();
        tm.client.setToken(token);

        StatusWapper status;
        Paging paging;


        for (int p = 1; p < 100; p++) {
            System.err.println(" - - - " + "Page " + p + " - - - ");
            paging = new Paging(p, 100);
            status = tm.getFriendsTimeline(2, paging);
            for (Status s : status.getStatuses()) {
                if (s.getPictureURLs() == null && s.getRetweetedStatus() == null) continue;
                System.out.println(s.getUser().getScreenName() + " - " + s.getText());
                String[] urls = s.getPictureURLs();
                if (urls == null) urls = s.getRetweetedStatus().getPictureURLs();
                if (urls == null) continue;
                int existCount = 0;
                int savedCount = 0;
                for (String url : urls) {
                    //System.out.print(url);
                    String path = "pre" + File.separator + url.substring(url.indexOf("large") + 6);
                    File f = new File(path);
                    if(f.exists()) {
                        existCount++;
                        //System.out.println(" exist");
                    }
                    else {
                        saveImage(url, path);
                        savedCount++;
                        //System.out.println(" saved");
                    }
                }
                System.out.println("Total: " + urls.length + " - Exist: " + existCount + " - Saved:" + savedCount);
                //System.out.println();
            }
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

}
