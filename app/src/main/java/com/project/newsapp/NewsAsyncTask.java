package com.project.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAsyncTask {


        /** Sample JSON response for a USGS query */

        private static final String SAMPLE_JSON_RESPONSE = "https://content.guardianapis.com/search?page=40&q=debate&api-key=test";
                //"https://www.googleapis.com/books/v1/volumes?q=android&maxResults=40";
//volumeInfo

        /**
         * Create a private constructor because no one should ever create a { EarthquackAysc...} object.
         * This class is only meant to hold static variables and methods, which can be accessed
         * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
         */

        private NewsAsyncTask() {

        }


        /**
         * Return a list of {@link } objects that has been built up from
         * parsing a JSON response.
         */

        public static ArrayList<NewsInfo> extractData() {

            // Create an empty ArrayList that we can start adding Datas to
            ArrayList<NewsInfo> newsdata = new ArrayList<NewsInfo>();
            //Working with JSon
            String link =SAMPLE_JSON_RESPONSE;
            String url;

            try {
                JSONObject root = new JSONObject(link);
                JSONArray item = root.getJSONArray("results");
                for(int i=0;i<item.length();i++){
                    JSONObject date1 = item.getJSONObject(i).getJSONObject("webPublicationDate");
                    JSONObject title1= item.getJSONObject(i).getJSONObject("webTitle");
                    JSONObject preview1 = item.getJSONObject(i).getJSONObject("webUrl");
                    JSONObject type1 = item.getJSONObject(i).getJSONObject("webUrl");

                    String date, title, preview,type;
                    date = date1.toString();
                    title = title1.toString();
                    preview = preview1.toString();
                    type = type1.toString();
/*
                long timeInMilliseconds =Long.parseLong(volume.getString("publishedDate"));
                //url = prop.getString("url");

                Date dateObject = new Date(timeInMilliseconds);
                String times = formatTime(dateObject);

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);
*/





                    newsdata.add(new NewsInfo(title,type,date,preview));
/*                newsdata.add(new NewsInfo(title,authors.getString(0),date,publisher,img,preview));
                newsdata.add(new NewsInfo(title,authors.getString(0),date,publisher,img,preview));
                newsdata.add(new NewsInfo(title,authors.getString(0),date,publisher,img,preview));
                newsdata.add(new NewsInfo(title,authors.getString(0),date,publisher,img,preview));*/
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Data objects with the corresponding data.

            // Return the list of Datas
            return newsdata;
        }

        private static String formatTime(Date dateObject) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            return timeFormat.format(dateObject);
        }

        public static ArrayList<NewsInfo> fetchNewsData(String requestUrl) {
            // Create URL object
            URL url = createUrl(requestUrl);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e("LOG_TAG", "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
            ArrayList<NewsInfo> newsdata = extractFeatureFromJson(jsonResponse);

            // Return the list of {@link Earthquake}s
            return newsdata;
        }


        private static URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e("LOG_TAG", "Problem building the URL ", e);
            }
            return url;
        }


        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */

        private static String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /*milliseconds */);
                urlConnection.setConnectTimeout(15000/* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e("LOG_TAG", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("LOG_TAG", "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    inputStream.close();
                }
            }
            return jsonResponse;
        }


        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */

        private static String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


        /**
         * Return a list of {@link MainActivity} objects that has been built up from
         * parsing the given JSON response.
         */

        private static ArrayList<NewsInfo> extractFeatureFromJson(String bookJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(bookJSON)) {
                return null;
            }

            // Create an empty ArrayList that we can start adding earthquakes to
            ArrayList<NewsInfo> allNews = new ArrayList<>();

            // Try to parse the JSON response string. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(bookJSON);

                JSONObject obj = baseJsonResponse.getJSONObject("response");
                JSONArray item = obj.getJSONArray("results");
                for(int i=0;i<item.length();i++){
                    JSONObject jsonObject = item.getJSONObject(i);

                    String date = jsonObject.getString("webPublicationDate");
                    String title= jsonObject.getString("webTitle");
                    String preview = jsonObject.getString("webUrl");
                    String type = jsonObject.getString("type");

                /*
                long timeInMilliseconds =Long.parseLong(volume.getString("publishedDate"));
                //url = prop.getString("url");

                Date dateObject = new Date(timeInMilliseconds);
                String times = formatTime(dateObject);

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);
*/


                    // Create a new {@link Earthquake} object with the magnitude, location, time,
                    // and url from the JSON response.
                    NewsInfo books = new NewsInfo(title,type,date,preview);

                    // Add the new {@link Earthquake} to the list of earthquakes.
                    allNews.add(books);
/*
                allNews.add(books);
                allNews.add(books);
                allNews.add(books);
                allNews.add(books);
*/

                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("Updated", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return allNews;
        }



    }



