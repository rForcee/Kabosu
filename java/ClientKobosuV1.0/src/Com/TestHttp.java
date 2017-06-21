package Com;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class TestHttp {

	
	public static void sendPost(String urlToRead) throws Exception {
			
			try {

		        URL url = new URL(urlToRead);  // cr�e un nouveau objet url
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setDoOutput(true);
		        conn.setRequestMethod("POST");// indique le type de la requ�te
		        conn.setRequestProperty("Content-Type", "application/json");// indique la propriete de la requete
		        
		        Gson gson = new Gson();
		        Data obj = new Data();
		        //obj.username = user; // affectation data1
		       // obj.password = pass; // affectation data2
		        String json = gson.toJson(obj);// convertion en JSON
		        System.out.println(json);  
		        OutputStream os = conn.getOutputStream();
		        os.write(json.getBytes());
		        os.flush();

		        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
		            throw new RuntimeException("Failed : HTTP error code : "
		                + conn.getResponseCode());
		        }

		        BufferedReader br = new BufferedReader(new InputStreamReader(
		                (conn.getInputStream())));

		        String output;
		        System.out.println("Output from Server .... \n");
		        while ((output = br.readLine()) != null) {
		            System.out.println(output);
		        }

		        conn.disconnect();

		      } catch (MalformedURLException e) {

		        e.printStackTrace();

		      } catch (IOException e) {

		        e.printStackTrace();

		     }
	
		}
	public static void main(String[] args) {
		String urlToRead = "http://localhost:5000";
		try {
			sendPost(urlToRead);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
