import json, random
from flask import Flask, request, jsonify
from flask_cors import CORS
from db import Db

app = Flask(__name__)
app.debug = True
CORS(app)

budget_depart = 100
rayonInfluenceStand = 25
dicoAction = {}
db = Db()

# DATABASE_URL=postgres://<username>@localhost/<dbname> python main.py

# Fonction pour les reponses en Json
def json_response(data="OK", status=200):
	return json.dumps(data), status, { "Content-Type": "application/json" }


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /reset avec GET
# Reinitialise une partie
# Delete all from ventes, joueur, zone, dayinfo
@app.route("/reset", methods=["GET"])
def reset_partie():
	db.execute("DELETE FROM recette;")
	db.execute("DELETE FROM ventes;")
	db.execute("DELETE FROM boisson;")
	db.execute("DELETE FROM zone;")
	db.execute("DELETE FROM joueur;")
	db.execute("DELETE FROM dayinfo;")
	return json_response({"Success": True})


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /players avec GET
# Renvoie tout ce qu'il y a dans la table players
@app.route("/players", methods=["GET"])
def get_players():
	result = db.select("SELECT * FROM joueur;")
	return json_response(result)

#Ajout d'un joueur en base et attibution des boissons si le joueur n'existe pas
def ajoutJoueur(name):
	coordX = random.randrange(330,670,1)
	coordY = random.randrange(130,470,1)
	db.execute("""INSERT INTO joueur(j_pseudo, j_budget) VALUES(@(nom),@(budget));""", {"nom": name, "budget": budget_depart})
	playerId = db.select("""SELECT j_id FROM joueur WHERE j_pseudo = @(nom);""", {"nom": name})[0]['j_id']
	db.execute("""INSERT INTO zone(z_type, z_centerX, z_centerY, z_rayon, j_id) VALUES 
		('stand',@(coordX),@(coordY),@(influence),
		(SELECT j_id FROM joueur WHERE j_pseudo = @(nom)));""", 
		{"nom": name, "coordX": coordX, "coordY": coordY, "influence": rayonInfluenceStand})
	db.execute("""INSERT INTO boisson(b_nom, b_hasAlcohol, b_isCold, b_prixvente, b_prixprod, j_id) 
		VALUES ('lemonade', FALSE,TRUE,0,0.8, @(p_id)), ('tea', FALSE,FALSE,0,0.8, @(p_id)), 
		('mojito',TRUE,TRUE,0,2.4, @(p_id));""", {"p_id": playerId})
	db.execute("""INSERT INTO recette(r_qte, b_id, i_id) VALUES 
		(2,(SELECT b_id FROM boisson WHERE b_nom = 'lemonade' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'lemon')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'lemonade' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'eau gazeuse')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'mojito' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'rhum')),
		(2,(SELECT b_id FROM boisson WHERE b_nom = 'mojito' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'menthe')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'tea' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'tea')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'tea' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'menthe'));""", {"p_id": playerId})

# Fonction pour la route /players avec la methode POST
# Permet d'ajouter un utilisateur a la table joueur avec le budget de base
@app.route("/players", methods=["POST"])
def add_player():
	elements = request.get_json()
	name = elements['name']

	joueur = db.select("""SELECT j_id FROM joueur WHERE j_pseudo = @(nom);""", {"nom": name})

	if joueur == []:
		ajoutJoueur(name)
  
	coord = db.select("""SELECT z_centerX as latitude, z_centerY as longitude FROM zone WHERE j_id = 
					(SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": name})[0]
	budgetBase = db.select("""SELECT j_budget FROM joueur WHERE j_pseudo = @(nom);""",{"nom": name})[0]['j_budget']
	nbSales = db.select("""SELECT COALESCE(SUM(v_qte),0) as nbSales FROM ventes WHERE j_id = 
						(SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": name})[0]['nbsales']
	drinksInfo = db.select("""SELECT b_nom as name, b_prixprod as price, b_hasAlcohol as hasAlcohol, b_isCold as isCold FROM boisson 
							WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": name})
	profit = budgetBase - budget_depart;

	for j in drinksInfo:
		j['hasAlcohol'] = j['hasalcohol']
		del j['hasalcohol']
		j['isCold'] = j['iscold']
		del j['iscold']

	info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}
	# Message de retour
	message = {"name": name, "location": coord, "info": info}
	print message
	return json_response(message)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /players/<player_name> avec DELETE
# Supprime un joueur de la partie
# OPTIONNEL
@app.route("/players/<player_name>", methods=["DELETE"])
def delete_player(player_name):
	db.execute("""DELETE FROM recette WHERE b_id IN (SELECT b_id FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom)));""", {"nom": player_name})
	db.execute("""DELETE FROM ventes WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": player_name})
	db.execute("""DELETE FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": player_name})
	db.execute("""DELETE FROM zone WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": player_name})
	db.execute("""DELETE FROM joueur WHERE j_pseudo =  @(nom);""", {"nom": player_name})
	return json_response({"delete": True})


#------------------------------------------------------------------------------------------------------------------------------------------------

#C: poste la meteo et l heure
#JAVA: fait un get regulier pour recupere la meteo et l heure.
@app.route('/metrology', methods=['GET','POST'])
def meteo():
	if request.method == 'POST':

		weather = request.get_json()
		print weather

		if "timestamp" not in weather:
			return json_response({ "error" : "Missing timestamp" }, 400)
		if "dfn" not in weather["weather"][0]:
			return json_response({ "error" : "Missing dfn"}, 400)
		if "weather" not in weather["weather"][0]:
			return json_response({ "error" : "Missing weather"}, 400)
		timestamp = weather["timestamp"]
		currentWeather = weather["weather"][0]["weather"]
		previsionWeather = weather["weather"][1]["weather"]

		result = db.select("SELECT di_hour, di_weather, di_forecast FROM dayinfo;")

		if result == []:
			db.execute("""INSERT INTO dayinfo(di_hour, di_weather, di_forecast) 
					VALUES(@(heure),@(meteo),@(forecast));""", 
					{"heure": timestamp, "meteo": currentWeather, "forecast": previsionWeather})
		else:
			db.execute("""UPDATE dayinfo SET (di_hour, di_weather, di_forecast) = (@(heure),@(meteo),@(forecast));""",
			{"heure": timestamp, "meteo": currentWeather, "forecast": previsionWeather})

	result = db.select("SELECT di_hour, di_weather, di_forecast FROM dayinfo;")
	print result
	return json_response({"timestamp": result[0]['di_hour'], "weather": [ {"dfn": 0, "weather": result[0]['di_weather']}, {"dfn": 1, "weather": result[0]['di_forecast'] } ] })


#------------------------------------------------------------------------------------------------------------------------------------------------

def sales_drinks_update(j, content):

	player = content['player']
	item = content['item']
	quantity = content['quantity']
	prixVente = j['price'][item]
	prixProd = db.select("""SELECT b_prixprod FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom)) 
		AND b_nom = @(boisson); """, {"nom": player, "boisson": item})[0]['b_prixprod']
	db.execute("""UPDATE boisson SET (b_prixvente) = (@(prixvente)) 
		WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom)) 
		AND b_nom = @(boisson);""", {"prixvente": prixVente, "nom": player, "boisson": item})
	hour = db.select("""SELECT di_hour FROM dayinfo;""")[0]['di_hour']
	weather = db.select("""SELECT di_weather FROM dayinfo;""")[0]['di_weather']
	j_id = db.select("""SELECT j_id FROM joueur 
		WHERE j_pseudo = @(nom);""", {"nom": player})[0]['j_id']
	b_id = db.select("""SELECT b_id FROM boisson WHERE b_nom = @(boisson) 
		AND j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",
		{"nom": player, "boisson": item})[0]['b_id']
	prixVente = db.select("""SELECT b_prixvente FROM boisson WHERE b_nom = @(boisson) 
		AND j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",
		{"nom": player, "boisson": item})[0]['b_prixvente']
	budget = db.select("""SELECT j_budget FROM joueur WHERE j_pseudo = @(nom);""",
		{"nom": player})[0]['j_budget']

	calBudget = budget + (quantity*prixVente)
	print calBudget;
	db.execute("""UPDATE joueur SET (j_budget) = (@(budget)) 
		WHERE j_pseudo = @(nom);""", {"budget": calBudget, "nom": player})
	db.execute("""INSERT INTO ventes(v_qte, v_hour, v_weather, v_prix, j_id, b_id) 
		VALUES(@(qty), @(hour), @(weather), @(prixvente), @(j_id), @(b_id));""",
		{"qty": quantity, "hour": hour, "weather": weather, "prixvente": prixVente, "j_id": j_id, "b_id": b_id})
	return True

def sales_drinks(j, content):

	player = content['player']
	item = content['item']
	quantity = content['quantity']
	recette = j['prepare']
	print "RECETTE"
	print recette
	if item in recette:
		if recette[item] != 0:
			if quantity > recette[item]:
				quantity = recette[item]
				recette[item] = 0
				
				sales_drinks_update(j, content)
			else:
				print "RECETTE ITEM"
				print recette[item]
				recette[item] = int(recette[item]) - int(quantity)
				sales_drinks_update(j, content)

	return True



# JAVA: post la trame suivante au serveur {"joueur": String, "item": String, "quantity": int }
@app.route('/sales', methods=['POST'])
def messageRecuJava():

	content = request.get_json()
	print "SALES CONTENT"
	print content

	player = content['player']
	item = content['item']
	quantity = content['quantity']

	print "DICOACTION"
	print dicoAction
	for i in dicoAction:
		if i == player:
			for j in dicoAction[i]['actions']:
				if j['kind'] == 'drinks':
					print "J"
					print j
					sales_drinks(j, content)

	return json_response(dicoAction)



#------------------------------------------------------------------------------------------------------------------------------------------------

def sales_ad(j, player_name):

	latitude = j['location']['latitude']
	longitude = j['location']['longitude']
	rayon = j['radius']
	price = j['price']

	j_id = db.select("""SELECT j_id FROM joueur 
		WHERE j_pseudo = @(nom);""", {"nom": player_name})[0]['j_id']

	budget = db.select("""SELECT j_budget FROM joueur 
	WHERE j_pseudo = @(nom);""", {"nom": player_name})[0]['j_budget']

	calBudget = budget - price

	db.execute("""UPDATE joueur SET (j_budget) = (@(budget)) 
		WHERE j_pseudo = @(nom);""", {"nom": player_name, "budget": calBudget})

	db.execute("""INSERT INTO zone(z_type, z_centerX, z_centerY, z_rayon, j_id) 
		VALUES('ad',@(latitude),@(longitude),@(rayon),@(j_id));""", 
		{"latitude": latitude, "longitude": longitude, "rayon": rayon, "j_id": j_id})

	j = ""

	return True
# Fonction pour la route /actions/<player_name> avec POST
# Actions pour le lendemain
# Ne s'ajoute pas aux actions mais les remplace les actions du joueur
# Repeter chaque jour pour le lendemain
# Par defaut le serveur suppose qu'on ne veut rien faire
@app.route('/actions/<player_name>', methods=['POST'])
def action_player(player_name):
	content = request.get_json()
	print "CONTENT"
	print content
	if content == []:
		return json_response({"success": False})

	dicoAction[player_name] = content
	for i in dicoAction:
		if i == player_name:
			for j in dicoAction[i]['actions']:
				print dicoAction[i]['actions']
				if j['kind'] == 'ad':
  					sales_ad(j, player_name)
				
				else:

					bname = j['prepare'].keys()[0]
					budget = db.select("""SELECT j_budget FROM joueur WHERE j_pseudo = @(nom);""",
					{"nom": player_name})[0]['j_budget']

					prixProd = db.select("""SELECT b_prixprod FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom)) 
					AND b_nom = @(boisson); """, {"nom": player_name, "boisson": bname})[0]['b_prixprod']
					qte = j['prepare'][bname]
					depenses = float(qte) * float(prixProd)
					calBudget = budget - depenses
					db.execute("""UPDATE joueur SET (j_budget) = (@(budget)) 
					WHERE j_pseudo = @(nom);""", {"budget": calBudget, "nom": player_name})
	
	return json_response(dicoAction)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /map avec GET
# JAVA : recupere les coordonnees de la map
@app.route('/map', methods=['GET'])
def envoieMapJava():

	
	coordinates = db.select("""SELECT m_centreX as latitude, m_centreY as longitude FROM map;""")[0]
	coordinatesSpan = db.select("""SELECT m_coordX as latitudeSpan, m_coordY as longitudeSpan FROM map;""")[0]
	ranking = db.select("""SELECT j_pseudo as name FROM JOUEUR ORDER BY j_budget DESC;""")
	
	coordinatesSpan['longitudeSpan'] = coordinatesSpan['longitudespan']
	del coordinatesSpan['longitudespan']
	coordinatesSpan['latitudeSpan'] = coordinatesSpan['latitudespan']
	del coordinatesSpan['latitudespan']

	region = {"center": coordinates, "span": coordinatesSpan}

	playerInfo = {}
	itemsByPlayer = {}
	rank = []
	drinksOffered = []

	for i in ranking:
  		rank.append(i['name'])
		coord = db.select("""SELECT z_centerX as latitude, z_centerY as longitude FROM zone 
			WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": i['name']})[0]
		budgetBase = db.select("""SELECT j_budget FROM joueur WHERE j_pseudo = @(nom);""", {"nom": i['name']})[0]['j_budget']
		nbSales = db.select("""SELECT COALESCE(SUM(v_qte),0) as nbSales FROM ventes 
			WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": i['name']})[0]['nbsales']
		drinksInfo = db.select("""SELECT b_nom as name, b_prixvente as price, b_hasAlcohol as hasAlcohol, 
			b_isCold as isCold FROM boisson 
			WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": i['name']})
		
		for j in drinksInfo:
			j['hasAlcohol'] = j['hasalcohol']
			del j['hasalcohol']
			j['isCold'] = j['iscold']
			del j['iscold']

		profit = budgetBase - budget_depart;
		info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}
		playerInfo[i['name']] = info
		drinksOffered = []

		items = db.select("""SELECT z_type as kind, z_centerX as latitude, z_centerY as longitude, 
		z_rayon as influence, j_pseudo as owner FROM zone INNER JOIN joueur 
		ON joueur.j_id = zone.j_id WHERE j_pseudo = @(nom);""", {"nom": i['name']})
		itemsPlayer = []
		for y in items:
			itemsPlayer.append({"kind": y['kind'], "owner": y['owner'], "influence": y['influence'], "location": {"latitude": y['latitude'], "longitude": y['longitude']}})

		itemsByPlayer[i['name']] = itemsPlayer

	mapInfo = {"region" : region, "ranking" : rank, "itemsByPlayer": itemsByPlayer, "playerInfo": playerInfo, "drinksByPlayer": []}

	return json_response(mapInfo)

#------------------------------------------------------------------------------------------------------------------------------------------------

# Fonction pour la route /map/<player_name> avec GET
# Recupere les details d'une partie
@app.route('/map/<player_name>', methods=['GET'])
def getMapPlayer(player_name):
	
	ingredients = db.select("""SELECT i_nom as nom, i_prix as ingprix FROM ingredient;""")

	availableIngredients = []
	for y in ingredients:
		availableIngredients.append({"name": y['nom'], "cost": y['ingprix'], "hasAlcohol": False, "isCold": True})

	
	coordinates = db.select("""SELECT m_centreX as latitude, m_centreY as longitude FROM map;""")[0]
	coordinatesSpan = db.select("""SELECT m_coordX as latitudeSpan, m_coordY as longitudeSpan FROM map;""")[0]
	ranking = db.select("SELECT j_pseudo as name FROM JOUEUR ORDER BY j_budget DESC;")

	rank = []
	for i in ranking:
  		rank.append(i['name'])

	items = db.select("""SELECT z_type as kind, z_centerX as latitude, z_centerY as longitude, 
	z_rayon as influence, j_pseudo as owner FROM zone INNER JOIN joueur 
	ON joueur.j_id = zone.j_id WHERE j_pseudo = @(nom);""", {"nom": player_name})

	itemsPlayer = []
	for y in items:
		itemsPlayer.append({"kind": y['kind'], "owner": y['owner'], "influence": y['influence'], "location": {"latitude": y['latitude'], "longitude": y['longitude']}})

	coordinatesSpan['longitudeSpan'] = coordinatesSpan['longitudespan']
	del coordinatesSpan['longitudespan']
	coordinatesSpan['latitudeSpan'] = coordinatesSpan['latitudespan']
	del coordinatesSpan['latitudespan']

	region = {"center": coordinates, "span": coordinatesSpan}

	mapInfo = {"region" : region, "ranking" : rank, "itemsByPlayer": itemsPlayer}
	
	coord = db.select("""SELECT z_centerX as latitude, z_centerY as longitude FROM zone 
		WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": player_name})[0]
	budgetBase = db.select("""SELECT j_budget FROM joueur WHERE j_pseudo = @(nom);""", {"nom": player_name})[0]['j_budget']
	nbSales = db.select("""SELECT COALESCE(SUM(v_qte),0) as nbSales FROM ventes 
		WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": player_name})[0]['nbsales']
	drinksInfo = db.select("""SELECT b_nom as name, b_prixprod as price, b_hasAlcohol as hasAlcohol, b_isCold as isCold FROM boisson 
		WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""", {"nom": player_name})

	for j in drinksInfo:
		j['hasAlcohol'] = j['hasalcohol']
		del j['hasalcohol']
		j['isCold'] = j['iscold']
		del j['iscold']
 

	profit = budgetBase - budget_depart;
	info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}

	message = {"availableIngredients": availableIngredients, "map": mapInfo, "playerInfo": info}
	return json_response(message)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/ingredients', methods=['GET'])
def get_ingredients():
  
	ingredients = db.select("""SELECT i_nom, i_prix FROM ingredient;""")

	listIngredients = []
	for i in ingredients:
		listIngredients.append({"name": i['i_nom'], "cost": i['i_prix'], "hasAlcohol": False, "isCold": True})

	print ingredients[1]
	return json_response({"ingredients": listIngredients})



###########################################################"""
###########################################################"""
###########################################################"""
###########################################################"""
###########################################################"""
#------------------------------------------------------------------------------------------------------------------------------------------------
###########################################################"""
###########################################################"""
###########################################################"""
###########################################################"""
###########################################################"""

#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /inscrire/ingredient avec POST
# Ajout d'un ingredient en BDD
@app.route('/ingredients/<player_name>', methods=['GET'])
def getIngredientsJoueur(player_name):

	listIngredients = db.select("""SELECT r_qte, b_nom, b_hasAlcohol, b_isCold, b_prixprod, i_nom, i_prix 
		FROM recette INNER JOIN boisson ON boisson.b_id = recette.b_id 
		INNER JOIN ingredient ON ingredient.i_id = recette.i_id 
		WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": player_name})


	return json_response(listIngredients)


#------------------------------------------------------------------------------------------------------------------------------------------------

# Fonction pour la route /inscrire/ingredient avec POST
# Ajout d'un ingredient en BDD
@app.route('/sales/<player_name>', methods=['GET'])
def getVentesByBoissons(player_name):

	listVentes = db.select("""SELECT SUM(v_qte) as quantite, b_nom as boisson, v_weather as weather FROM ventes 
		INNER JOIN boisson ON boisson.b_id = ventes.b_id WHERE ventes.j_id = 
		(SELECT j_id FROM joueur WHERE j_pseudo = @(nom)) GROUP BY v_weather, b_nom;""",{"nom": player_name})


	return json_response(listVentes)


#------------------------------------------------------------------------------------------------------------------------------------------------

if __name__ == "__main__":
	app.run()
