package com.htetznaing.mediafire;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MFireFucker {
    private final String GET_MAIN_PATTERN = "aria-label=\"Download file\"(.*?)<\\/a>";//Regex pattern for get link and size
    private final String LINK_PATTERN = "href=\"(.*)\""; //Regex pattern for get direct link
    private final String SIZE_PATTERN = "Download ?\\((.*)\\)";//Regex pattern for get size
    private final String MEDIAFIRE = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";//Regex for check mediafire link!
    private OnFinished onFinished;
    private String url;

    public MFireFucker(String url,OnFinished onFinished) {
        this.onFinished = onFinished;
        this.url = url;
    }

    public void find() {
        if (check(url)) {
            new AsyncTask<Void,Void,String>(){
                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        URLConnection connection = new URL(url).openConnection();
                        connection.addRequestProperty("User-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36");
                        connection.connect();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String result = "", line;
                        while ((line = reader.readLine()) != null) {
                            result += line;
                        }
                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    String srcAndSize = parseRegex(GET_MAIN_PATTERN, result);//Get Link and size
                    if (srcAndSize != null) {
                        String src = parseRegex(LINK_PATTERN, srcAndSize);//Get Direct LINK
                        String size = parseRegex(SIZE_PATTERN, srcAndSize);//Get Size
                        onFinished.done(src, size);
                    } else onFinished.error("May be your link broken or try again!");
                }
            }.execute();
        }else onFinished.error("Please input valid mediafire link!");
    }

    private static String parseRegex(String regex,String string){
        if (regex!=null && string!=null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private boolean check(String url) {
        if (url!=null) {
            final Pattern pattern = Pattern.compile(MEDIAFIRE, Pattern.CASE_INSENSITIVE);
            final Matcher matcher = pattern.matcher(url);
            return matcher.find();
        }return false;
    }

    public interface OnFinished{
        void done(String src,String size);
        void error(String message);
    }
}
