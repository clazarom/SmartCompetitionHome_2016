package iit.ece.edu.smartcompetitionhouse;

import android.content.Context;
import android.preference.PreferenceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Cat on 8/28/2016.
 */
public class ServerConnector {

    //Constant values:
    public static final String _JSON_ID = "competitionJSON";
    //IP - http://50.178.148.186/
    //Domain - http://competitionvault.wow64.net/
    public static final String _IIT_SERVER_UPDATE_VALUES_URL = "";

    public static final String _PORT  = ":";
    //Access the database
    private DatabaseManager dbManager;
    private Context databaseContext;
    //Sending fields
    private AsyncHttpClient httpClient;
    private boolean sending;
    public String WRITE_URL;
    public String READ_URL;




    /**
     * Constructors: create the server connector object
     * Initializes the httpClient, the table names and the JSON_ID
     * @param jsonID
     * @param writeURL - server url tp write values
     * @param readURL - server url to read values
     */
    public ServerConnector(String jsonID, String writeURL, String readURL, DatabaseManager manager, Context ctx){
        httpClient =  new AsyncHttpClient();
        WRITE_URL = writeURL;
        READ_URL = readURL;
        dbManager = manager;
        databaseContext =  ctx;
        sending = false;


    }

    /**
     * sendToIITServer()
     * Function to send a JSON object to IIT server
     */

    public void sendToCentralNode(String json, String url, AsyncHttpResponseHandler client){

        if (url != null && !url.equals("")) {
            System.out.println("Sending: " + json);
            System.out.println(url);

            //Set parameters
            RequestParams params = new RequestParams();
            params.put(_JSON_ID, json);

            //Send http request
            httpClient.post(url, params, client);

            sending = true;
        }else{
            System.out.println("Empty URL - Not sending");
        }

    }


    /**
     * convertToJSON()
     * Function to convert the array of key maps to a JSON String format
     *
     */
    public   String convertToJSON( List<Map<String, String>> args){
        String json = "";
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        try {
            json = gson.toJson(args);
        }catch (Exception e){
            System.out.println("Could not convert to JSON!: "+e);
        }

        return json;
    }

    /****************************************************
     * HTTP ASYNC CLIENT
     */
    public final AsyncHttpResponseHandler asyncHTTPClient = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            String cont = convertToString(bytes);
            onSuccess(cont);

        }

        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
            String cont = convertToString(bytes);


            System.out.println("Failed! server:" + statusCode);

            if (statusCode == 404) {
                System.out.println("Page not found");

            } else if (statusCode == 500) {
                System.out.println("Server failure");

            } else {
            }
            sending = false;

        }

        //Handle succesful response
        public void onSuccess(String response) {
            System.out.println("Server response: "+response);

            try {

                //Convert to a JSON Array and get the arguments
                JSONArray arr = new JSONArray(response);
                //List<String> args = new ArrayList();
                //Analyze each JSON object
                for(int i=0; i<arr.length();i++){
                    JSONObject jsonObj = (JSONObject)arr.get(i);
                    System.out.println("Update db!: " +(String) jsonObj.get("updated"));
                    dbManager.updateSyncStatus(databaseContext, (String) jsonObj.get("table_name"),
                            DatabaseManager.upDateColumn, (String) jsonObj.get("updated"), (String) jsonObj.get("time_stamp"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            sending = false;
        }

        //Handle failing response
        public void onFailure(int statusCode, Throwable error, String content) {


        }


    };

    /**
     * convertToString()
     */
    public static String convertToString(byte[] args){
        String str = "";
        try{
            str = new String(args, "UTF-8"); // for UTF-8 encoding
        }catch (Exception e){

        }
        return str;
    }
}
