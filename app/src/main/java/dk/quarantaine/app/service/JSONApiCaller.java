package dk.quarantaine.app.service;

import android.util.Log;

import dk.quarantaine.app.datamodel.APIResponse;
import dk.quarantaine.commons.interfaces.JSONConvertable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class JSONAPICaller implements BaseAPICaller,Callable<APIResponse> {

    String endpoint;
    JSONConvertable jsonConvertable;

    public JSONAPICaller(String endpoint, JSONConvertable jsonConvertable){
        this.endpoint = endpoint;
        this.jsonConvertable = jsonConvertable;

    }

    //TODO Match the code of which the BaseAPICaller uses.
    @Override
    public APIResponse call() throws Exception{



            APIResponse response = null;
            HttpsURLConnection connection = null;
            try{
                URL urlobj = new URL("https://quarantaine.baage-it.dk" + endpoint);
                connection = (HttpsURLConnection) urlobj.openConnection();

                if(connection == null){
                    Log.i("app", "WHy is connection null");
                }

                connection.setRequestMethod("POST");
                connection.setRequestProperty("content-type", "application/json");
                //extra data
                //connection.setRequestProperty(map.key, map.data);

                ObjectMapper mapper = new ObjectMapper();

                connection.setDoOutput(true);
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                Log.i("JSON", "Converted JSON OBJECT");

                wr.write(jsonConvertable.toJson().toString());
                wr.flush();
                wr.close();

                Log.i("app", "wrote data to stream");
                connection.connect();
                Log.i("app", "connected");

                int responsecode = connection.getResponseCode();
                StringBuffer responseString = new StringBuffer();

                try{
                    BufferedReader in;
                    if(responsecode >= 200 && responsecode < 300){
                        in = new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8));
                    }
                    else{
                        in = new BufferedReader(new InputStreamReader(connection.getErrorStream(),StandardCharsets.UTF_8));
                    }

                    String input;

                    while ((input = in.readLine()) != null){
                        responseString.append(input);
                    }

                    in.close();
                    Log.i("app", "Read data from stream");
                }
                catch (Exception e){
                    Log.i("app", "tried to read data from stream failed");
                }

                Log.i("app", "full response :" + (responseString == null ? "null": responseString));
            response = new APIResponse(responseString.toString(),responsecode);
        }
        catch(Exception e)
        {
            Log.i("app", e.getLocalizedMessage());
        }
        finally {
            if(connection != null){
                connection.disconnect();
                Log.i("app","Closed connection");
            }
        }
        Log.i("app",response.getResponseBody());
        Log.i("app",new Integer(response.getResponseCode()).toString());

        return response;
    }
}
