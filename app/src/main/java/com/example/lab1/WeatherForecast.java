package com.example.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ProgressBar pbar=findViewById(R.id.pBar);

        ForecastQuery temp=new ForecastQuery();
        temp.execute();
    }

    private class ForecastQuery extends AsyncTask<String,Integer,String> {

        Double UV;
        String min;
        String max;
        String temperature;
        String icon;
        ProgressBar pb = findViewById(R.id.pBar);

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s==null) {
                TextView tv = findViewById(R.id.txtCurrent);
                tv.setText(temperature + "⁰C");
                tv = findViewById(R.id.txtMin);
                tv.setText("Min: " + min + "⁰C");
                tv = findViewById(R.id.txtMax);
                tv.setText("Max: " + max + "⁰C");
                tv = findViewById(R.id.txtUV);
                tv.setText("UV index: " + UV.toString());
                ImageView iv = findViewById(R.id.imgWeatherIcon);
                File imageFile = new File(icon + ".png");
                System.out.println(imageFile.getAbsolutePath());
                Bitmap bp = BitmapFactory.decodeFile("/data/data/com.example.lab1/files/"+icon+".png");
                iv.setImageBitmap(bp);



            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //ProgressBar pb = findViewById(R.id.progressBar);
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            String ret = null;


            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

            try {
                // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;

                //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature"))
                            {
                                temperature = xpp.getAttributeValue(null,"value");
                                publishProgress(25);
                                min = xpp.getAttributeValue(null,"min");
                                publishProgress(50);
                                max = xpp.getAttributeValue(null,"max");
                                publishProgress(75);
                            }
                            if(tagName.equals("weather")){
                                icon = xpp.getAttributeValue(null, "icon");
                                String temp = "http://openweathermap.org/img/w/" + icon + ".png";
                                Bitmap image = null;
                                URL imageUrl = new URL(temp);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.connect();
                                int responseConde = connection.getResponseCode();
                                if(responseConde==200) {
                                    if(fileExistance(icon+".png") ==false) {
                                        InputStream is = imageUrl.openStream();
                                        image = BitmapFactory.decodeStream(is);
                                        FileOutputStream outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                        Log.i("FILE NAME::::","file name: "+icon+".png\nDOWNLOADED AND SAVED");

                                    }
                                    else{
                                        FileInputStream fis=null;
                                        try{fis = openFileInput(icon+".png"); }
                                        catch (FileNotFoundException e){e.printStackTrace();}
                                        Bitmap bm= BitmapFactory.decodeStream(fis);
                                        Log.i("FILE NAME::::","file name: "+icon+".png\nFOUND LOCALLY");
                                    }

                                    publishProgress(100);
                                }
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }

                url = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"UTF-8"),8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while((line = reader.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }

                String result = stringBuilder.toString();
                JSONObject json = new JSONObject(result);
                UV =  json.getDouble("value");


            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch (JSONException jse){ret = "json exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}



            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }


        private boolean fileExistance(String fileName){
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }
    }

}
