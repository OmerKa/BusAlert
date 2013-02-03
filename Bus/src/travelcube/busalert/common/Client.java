/**
 * 
 */
package travelcube.busalert.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

/**
 * @author omer
 * 
 */
public class Client extends AsyncTask<Object, Void, String> {

    protected static String      serverAddress = "http://54.243.252.111";
    protected ICallBackWithError _cb;

    public Client(ICallBackWithError cb) {
        _cb = cb;
    }

    private static HttpClient httpclient = new DefaultHttpClient();

    private String connectToServer(String url, List<NameValuePair> postData) {
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
            HttpResponse response = httpclient.execute(httpPost);
            return handleHttpResponse(response);
        } catch (IOException e) {
            return ("No internet connection");
        }
    }

    private String GetFromServer(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            return handleHttpResponse(response);
        } catch (IOException e) {
            return ("No internet connection");
        }
    }

    private String handleHttpResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() != 200) { return ("Server: " + String
                .valueOf(response.getStatusLine().getStatusCode())); }
        InputStream instream = entity.getContent();
        String result = convertStreamToString(instream);
        return result;
    }

    /**
     * 
     * @param is
     * @return String
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String doInBackground(Object... args) {
        switch (args.length) {
            case 1:
                return GetFromServer((String) args[0]);
            case 2:
                return connectToServer((String) args[0],
                        (List<NameValuePair>) args[1]);
            default:
                return null;
        }
    }

}
