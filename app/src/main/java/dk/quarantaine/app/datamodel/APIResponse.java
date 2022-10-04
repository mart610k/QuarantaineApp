package dk.quarantaine.app.datamodel;


public class APIResponse {
    int responseCode;
    String responseBody;

    public APIResponse(String responseBody, int responseCode){
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
