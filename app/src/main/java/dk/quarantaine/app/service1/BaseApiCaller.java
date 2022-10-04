package dk.quarantaine.app.service;

import android.util.Log;

import dk.quarantaine.app.datamodel.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class BaseApiCaller {


    public APIResponse getAPIData(String baseURL,String methodType ,String apiEndpoint, Map<String,String> header, String body){
        APIResponse response = null;
        HttpsURLConnection connection = null;
        try{
            URL urlobj = new URL(baseURL + apiEndpoint);
            connection = (HttpsURLConnection) urlobj.openConnection();

            if(connection == null){
                Log.i("app", "WHy is connection null");
            }

            connection.setRequestMethod(methodType);
            for (Map.Entry<String,String> entry: header.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            //connection.setRequestProperty("content-type", "application/json");
            //extra data
            //connection.setRequestProperty(map.key, map.data);

            ObjectMapper mapper = new ObjectMapper();

            connection.setDoOutput(true);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            Log.i("JSON", "Converted JSON OBJECT");

            wr.write(body);
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
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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
