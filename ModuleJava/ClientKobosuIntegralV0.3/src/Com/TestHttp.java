package Com;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.net.HttpURLConnection;
import java.net.URL;
public class TestHttp {

	///////////////////////////////////////////////Methode post sales///////////////////////////////////////
	public static void sendPost(URL url , String speudo, String boisson, int nb) throws Exception {

		try {

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");// indique le type de la requête
			conn.setRequestProperty("Content-Type", "application/json");// indique la propriete de la requete

			Gson gson = new Gson();
			Data obj = new Data();
			obj.player = speudo; // affectation data1
			obj.item = boisson; // affectation data2
			obj.quantity = nb; // affectation data3
			String json = gson.toJson(obj);// convertion en JSON
			System.out.println(json);  
			OutputStream os = conn.getOutputStream();
			os.write(json.getBytes());
			os.flush();

			if (conn.getResponseCode() !=200) {
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
	//////////////////////////////////////////////Methode get map/////////////////////////////////////////

	public static String getMap(URL url ) throws Exception {
		int i =0;
		StringBuilder result = new StringBuilder();
		// crée un nouveau objet url
		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //
		conn.setRequestMethod("GET");// indique le type de la requête
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));// envoie le Get et place le retour dans un buffer
		String line;
		while ((line = rd.readLine()) != null) {

			result.append(line);
		}
		rd.close();
		System.out.println(result);
		return result.toString();
	}
	//main


	public static DataTrameMap traitementTrameMap(String trame) throws IOException {
		DataTrameMap  data = new DataTrameMap ();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject jsonObject = obj.getAsJsonObject();
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		data.setMapY(response.get("region").getAsJsonObject().get("center").getAsJsonObject().get("longitude").getAsInt());
		data.setMapX(response.get("region").getAsJsonObject().get("center").getAsJsonObject().get("latitude").getAsInt());
		data.setMapSy(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("longitudespan").getAsInt());	
		data.setMapSx(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("latitudespan").getAsInt());
		return data;

	}
	public static DataTramePlayerInfo traitementTramePlayerInfo(String trame) throws IOException {
		DataTramePlayerInfo  data = new DataTramePlayerInfo  ();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject jsonObject = obj.getAsJsonObject();
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		//data.pseudo(response.get("playerInfo").getAsString());
		//data.drink.setPrice(response.get("playerInfo").getAsJsonObject().get("drinksOffered").getAsJsonObject().get("price").getAsFloat());
		//data.setMapSy(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("longitudespan").getAsInt());	
		//data.setMapSx(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("latitudespan").getAsInt());
		return data;

	}
	public static DataTrameRank traitementTrameRank(String trame) throws IOException {
		DataTrameRank  data = new DataTrameRank ();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject jsonObject = obj.getAsJsonObject();
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		for(int i =0; i<5 ;i++){
			data.rank.add((response.get("ranking").getAsJsonObject().get("name").getAsString()));
		}
		//data.drink.setPrice(response.get("playerInfo").getAsJsonObject().get("drinksOffered").getAsJsonObject().get("price").getAsFloat());
		//data.setMapSy(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("longitudespan").getAsInt());	
		//data.setMapSx(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("latitudespan").getAsInt());
		return data;

	}
	public static DataTrameMeteo traitementTrameMetrology(String trame){
		DataTrameMeteo  data = new DataTrameMeteo ();
		trame = trame.replaceAll("[\\[\\]]", "");
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonElement jelement = new JsonParser().parse(reader);
		JsonObject json = jelement.getAsJsonObject();
		System.out.println(json.toString());
		data.setHeure(json.get("hour").getAsInt());
		data.setWeather(json.get("weather").getAsInt());

		return data;
	}
	public static void main(String[] args) {
		String speudo ="John Cena";
		String boisson = "Limonade";
		int nb = 4;
		try {
			URL urlPost = new URL("https://kabosu.herokuapp.com/sales");
			URL urlGet = new URL("https://kabosu.herokuapp.com/map"); 

			//sendPost(urlPost,speudo,boisson,nb); // post vers https://kabosu.herokuapp.com/sales
			System.out.print(traitementTrameRank(getMap(urlGet))); // get vers https://kabosu.herokuapp.com/map
			//getMap(urlGet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
