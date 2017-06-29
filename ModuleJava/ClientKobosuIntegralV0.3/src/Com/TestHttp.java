package Com;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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

		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		data.setMapY(response.get("region").getAsJsonObject().get("center").getAsJsonObject().get("longitude").getAsInt());
		data.setMapX(response.get("region").getAsJsonObject().get("center").getAsJsonObject().get("latitude").getAsInt());
		data.setMapSy(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("longitudeSpan").getAsInt());	
		data.setMapSx(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("latitudeSpan").getAsInt());
		reader.close();
		return data;

	}
public static DataTramePlayerInfo traitementTramePlayerInfo(String trame, String joueur) throws IOException {
		//ArrayList<String> joueur = new ArrayList<String>();
		//ArrayList<DataTramePlayerInfo> data = new ArrayList<DataTramePlayerInfo>();
		DataTramePlayerInfo  data = new DataTramePlayerInfo();
		DatadrinksOffered drinkInfo = new DatadrinksOffered();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		//System.out.println(response.get("playerInfo").getAsJsonObject().get(joueur));
		data.setBudget(response.get("playerInfo").getAsJsonObject().get(joueur).getAsJsonObject().get("cash").getAsFloat());
		data.setProfit(response.get("playerInfo").getAsJsonObject().get(joueur).getAsJsonObject().get("profit").getAsFloat());
		data.setVente(response.get("playerInfo").getAsJsonObject().get(joueur).getAsJsonObject().get("sales").getAsInt());
		JsonArray JArray = new JsonArray();
		JArray = (response.get("playerInfo").getAsJsonObject().get(joueur).getAsJsonObject().get("drinksOffered").getAsJsonArray());
		if (JArray != null) { 
			for (int i=0;i<JArray.size();i++){ 
				drinkInfo.nom =JArray.get(i).getAsJsonObject().get("name").getAsString();
				drinkInfo.price=JArray.get(i).getAsJsonObject().get("price").getAsFloat();
				drinkInfo.chaud =JArray.get(i).getAsJsonObject().get("isCold").getAsBoolean();
				drinkInfo.alcool =JArray.get(i).getAsJsonObject().get("hasAlcohol").getAsBoolean();
				data.drink.add(drinkInfo);
				System.out.println(data.drink.get(i).nom);
				System.out.println(data.drink.get(i).alcool);
				System.out.println(data.drink.get(i).chaud);
				System.out.println(data.drink.get(i).price);
			}
		}

		//data.drink.setAlcool(response.get("playerInfo").getAsJsonObject().get(joueur).getAsJsonObject().get("drinksOffered").getAsJsonObject().get("hasAlcohol").getAsBoolean());
		//TODO faire les drink info
		System.out.println(data.getBudget());
		System.out.println(data.getProfit());
		System.out.println(data.getVente());
		//System.out.println(data.drink.isAlcool());
		//data.setPseudo(response.get("playerInfo").getAsJsonObject().get(joueur.get(1)).getAsString());
		//data.drink.setPrice(response.get("playerInfo").getAsJsonObject().get("drinksOffered").getAsJsonObject().get("price").getAsFloat());
		//data.setMapSy(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("longitudespan").getAsInt());	
		//data.setMapSx(response.get("region").getAsJsonObject().get("span").getAsJsonObject().get("latitudespan").getAsInt());
		reader.close();
		return data;

	}
	public static DataTrameItemJoueur traitementTrameItem(String trame, String joueur) throws IOException {
	
		DataTrameItemJoueur  data = new DataTrameItemJoueur();
		DataItemJoueur item = new DataItemJoueur();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		data.joueur = joueur;
		JsonArray JArray = new JsonArray();
		JArray = (response.get("itemsByPlayer").getAsJsonObject().get(joueur).getAsJsonArray());
		if (JArray != null) { 
			for (int i=0;i<JArray.size();i++){ 
				
				item.setTypeItem(JArray.get(i).getAsJsonObject().get("kind").getAsString());
				item.setInfluence(JArray.get(i).getAsJsonObject().get("influence").getAsFloat());
				item.setNomjoueur(JArray.get(i).getAsJsonObject().get("owner").getAsString());
				item.setLatitude(JArray.get(i).getAsJsonObject().get("location").getAsJsonObject().get("latitude").getAsFloat());
				item.setLongitude(JArray.get(i).getAsJsonObject().get("location").getAsJsonObject().get("longitude").getAsFloat());
				data.item.add(item);
				System.out.println("data.item: "+data.item);
		
			}
		}
		reader.close();
		return data;

	}
	public static DataTrameRank traitementTrameRank(String trame) throws IOException {
		DataTrameRank  data = new DataTrameRank ();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		JsonArray JArray = new JsonArray();
		JArray = ((response.get("ranking")).getAsJsonArray());
		if (JArray != null) { 
			for (int i=0;i<JArray.size();i++){ 
				data.rank.add(JArray.get(i).getAsString());
			}
		}
		
		System.out.println(data.getRank());
		reader.close();
		return data;

	}
	public static DataTrameMeteo traitementTrameMetrology(String trame) throws IOException{
		DataTrameMeteo  data = new DataTrameMeteo ();
		JsonReader reader = new JsonReader(new StringReader(trame));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(trame).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject response = new JsonObject();
		response = gson.fromJson(obj,JsonObject.class);
		JsonArray JArray = new JsonArray();
		System.out.println(obj.toString());
		data.setHeure(obj.get("timestamp").getAsInt());
		System.out.println(data.getHeure());
		JArray = ((response.get("weather")).getAsJsonArray());
		if (JArray != null) { 
			for (int i=0;i<JArray.size();i++){ 
				
				data.setWeather(JArray.get(i).getAsJsonObject().get("weather").getAsInt());
				System.out.println(data.getWeather());
		
			}
		}
		reader.close();
		return data;
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		String speudo ="John Cena";
		@SuppressWarnings("unused")
		String boisson = "Limonade";
		@SuppressWarnings("unused")
		int nb = 4;
		try {
			@SuppressWarnings("unused")
			URL urlPost = new URL("https://kabosu.herokuapp.com/sales");
			@SuppressWarnings("unused")
			URL urlGet = new URL("https://kabosu.herokuapp.com/map"); 
			URL urlGet2 = new URL("https://kabosu.herokuapp.com/metrology"); 
TestHttp get = new TestHttp();
			//sendPost(urlPost,speudo,boisson,nb); // post vers https://kabosu.herokuapp.com/sales
			//System.out.print(traitementTramePlayerInfo(getMap(urlGet))); // get vers https://kabosu.herokuapp.com/map
			//getMap(urlGet);
			//System.out.println(getMap(urlGet));
			//traitementTramePlayerInfo(getMap(urlGet),"Erwann");
			//traitementTrameRank(getMap(urlGet));
			traitementTrameItem(getMap(urlGet), "Erwann");
			//traitementTrameMetrology(getMap(urlGet2));
			get.traitementTrameItem(getMap(urlGet),"Erwann");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
