/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/07/11
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Connect {

    private static String rootDirectory = "C:\\";
    private static int maxThreads = 25;
    private static ThreadPoolExecutor threadPoolExecutor;// = new ThreadPoolExecutor(maxThreads, maxThreads, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(maxThreads, true), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String args[]) {
        //String imageURL = "http://www.beautyleg.com/member/album/560/0001.jpg";
        //Connect connect = new Connect();
        for (int i = 913; i <= 913; i++) {
            String webPage = "http://www.beautyleg.com/member/show.php?no=" + i;
            String pageString = fetchPage(webPage);
            //System.out.println(pageString);
            String headLine;
            try {
                headLine = fetchLine(pageString);
                downloadImages(i, rootDirectory + headLine);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Not Found for No." + i);
            }
            System.out.println("- - - - - - - -");
            System.gc();
        }

    }

    private static void downloadImages(int index, String folderName) {
        String picsCount = folderName.substring(folderName.indexOf("(") + 1, folderName.indexOf(")"));
        int picsCountInt = Integer.parseInt(picsCount);

        if (new File(folderName).mkdir())
            System.out.println("Folder: [" + folderName + "] has been created");

        String imageURL = "http://www.beautyleg.com/member/album/" + index + "/";

        threadPoolExecutor = new ThreadPoolExecutor(maxThreads, maxThreads, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(maxThreads, true), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < picsCountInt; i++) {
            threadPoolExecutor.submit(new SingleImageDownloader(imageURL + String.format("%04d", i) + ".jpg", folderName + File.separator + String.format("%04d", i) + ".jpg", i));
        }
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPoolExecutor.shutdownNow();
            }
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException ex) {
            threadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println();
        System.out.println("" + picsCountInt + " pics saved");
        /*try {
            for (int i = 0; i < picsCountInt; i++) {
                saveImage(imageURL + String.format("%04d", i) + ".jpg", folderName + File.separator + String.format("%04d", i) + ".jpg");
                System.out.print(i + " ");
            }
            System.out.println("[" + folderName + "] " + picsCountInt + " pics saved");
        } catch (IOException e) {
            e.printStackTrace();  //TODO: Automatically Generated Catch Statement
        }*/
    }

    private static String fetchLine(String pageString) throws Exception {

        //System.out.println(pageString);
        int beginIndex = pageString.indexOf("<td align=\"center\">No.") + "<td align=\"center\">".length();
        if (beginIndex == 19)
            throw new Exception("Line not Found");
        //System.out.println("Begin Index = " + beginIndex);
        int endIndex = pageString.indexOf("</span></td>", beginIndex);
        //System.out.println("End Index = " + endIndex);
        String subString = pageString.substring(beginIndex, endIndex);
        //System.out.println(subString);
        subString = subString.replaceAll("<span style=\"color: #900\">", "\t");
        subString = subString.replaceAll("</span>", "\n");
        //System.out.println(subString);
        String[] infos = new String[5];
        String[] lines = subString.split("\n");
        infos[0] = lines[0].split("\t")[1];
        infos[1] = lines[1].split("\t")[1];
        String[] datePicsSplits = lines[2].split("\t")[1].split(" ");
        infos[2] = datePicsSplits[0];
        infos[3] = datePicsSplits[2];
        infos[4] = lines[4].split("\t")[1];

        infos[0] = infos[0].length() < 3 ? "0" + infos[0] : infos[0];
        infos[0] = infos[0].length() < 3 ? "0" + infos[0] : infos[0];

        return "No." + infos[0] + " - " + infos[1] + " (" + infos[3] + ") - [" + infos[2] + "] by " + infos[4];
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);

        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + getAuthenticationString());
        InputStream is = urlConnection.getInputStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    private static String fetchImage(String imageURL) {
        String result = "";

        try {
            URL url = new URL(imageURL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + getAuthenticationString());
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();

            //System.out.println("*** BEGIN ***");
            //System.out.println(result);
            //System.out.println("*** END ***");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String fetchPage(String webPage) {
        String result = "";

        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            try {
                defaultHttpClient.getCredentialsProvider().setCredentials(
                        new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                        new UsernamePasswordCredentials("endario", "2X8v4Ha"));

                HttpPost httpPost = new HttpPost("http://www.beautyleg.com/member/");
                //System.out.println("executing request" + httpPost.getRequestLine());
                HttpResponse response = defaultHttpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                //System.out.println("----------------------------------------");
                //System.out.println(response.getStatusLine());
                if (entity != null) {
                    //System.out.println("Response content length: " + entity.getContentLength());
                    InputStreamReader isr = new InputStreamReader(entity.getContent());
                    int numCharsRead;
                    char[] charArray = new char[1024];
                    StringBuffer sb = new StringBuffer();
                    while ((numCharsRead = isr.read(charArray)) > 0) {
                        sb.append(charArray, 0, numCharsRead);
                    }
                    result = sb.toString();
                }

                httpPost = new HttpPost(webPage);
                //System.out.println("executing request" + httpPost.getRequestLine());
                response = defaultHttpClient.execute(httpPost);
                entity = response.getEntity();

                //System.out.println("----------------------------------------");
                //System.out.println(response.getStatusLine());
                if (entity != null) {
                    //System.out.println("Response content length: " + entity.getContentLength());
                    InputStreamReader isr = new InputStreamReader(entity.getContent());
                    int numCharsRead;
                    char[] charArray = new char[1024];
                    StringBuffer sb = new StringBuffer();
                    while ((numCharsRead = isr.read(charArray)) > 0) {
                        sb.append(charArray, 0, numCharsRead);
                    }
                    result = sb.toString();
                }
                EntityUtils.consume(entity);
            } finally {
                defaultHttpClient.getConnectionManager().shutdown();
            }












            /*URL url = new URL(webPage);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization", "Basic " + getAuthenticationString());
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);*/



            //System.out.println("*** BEGIN ***");
            //System.out.println(result);
            //System.out.println("*** END ***");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String getAuthenticationString() {
        String name = "endario";
        String password = "19860902";
        String authString = name + ":" + password;
        //System.out.println("auth string: " + authString);
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        //System.out.println("Base64 encoded auth string: " + authStringEnc);
        return authStringEnc;
    }

}
