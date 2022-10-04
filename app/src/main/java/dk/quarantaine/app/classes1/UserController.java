package dk.quarantaine.app.classes;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.quarantaine.commons.dto.RegisterUserDTO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class UserController implements Callable<String> {

    RegisterUserDTO userDTO;

    public UserController(RegisterUserDTO userDTO){
        super();
        this.userDTO = userDTO;
    }


    @Override
    public String call() throws Exception{

        String fullresponse = null;
        HttpsURLConnection connection = null;
        try{
            
            URL urlobj = new URL("https://quarantaine.baage-it.dk/api/register");
            connection = (HttpsURLConnection) urlobj.openConnection();

            if(connection == null){
                Log.i("app", "WHy is connection null");
            }

            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");

            //writeToStream
            ObjectMapper mapper = new ObjectMapper();

            byte[] contentBytes = mapper.writeValueAsString(userDTO).getBytes(StandardCharsets.UTF_8);

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(contentBytes,0,contentBytes.length);
            wr.flush();
            wr.close();

            Log.i("app", "wrote data to stream");
            connection.connect();
            Log.i("app", "connected");

            int responsecode = connection.getResponseCode();
            StringBuffer response = new StringBuffer();

            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8));

                String input;

                while ((input = in.readLine()) != null){
                    response.append(input);
                }

                in.close();
                Log.i("app", "Read data from stream");
            }
            catch (Exception e){
                Log.i("app", "tried to read data from stream failed");
            }

            Log.i("app", "full response :" + (response == null ? "null": response));

            fullresponse = Integer.toString(responsecode);

        }

        catch(Exception e)
        {
            Log.i("app",e.getCause().toString());
            if(connection != null){
                connection.getErrorStream().close();
            }

        }
        finally {
            if(connection != null){
                connection.disconnect();
                Log.i("app","Closed connection");
            }
        }
        Log.i("app",fullresponse);

        return fullresponse;
    }

}
