import json, random
from flask import Flask, request, jsonify
from flask_cors import CORS
from db import Db

app = Flask(__name__)
app.debug = True
CORS(app)

budget_depart = 10
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
	db.execute("DELETE FROM ventes;")
	db.execute("DELETE FROM joueur;")
	db.execute("DELETE FROM zone;")
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
	db.execute("""INSERT INTO boisson(b_nom, b_alcool, b_chaud, b_prixvente, b_prixprod, j_id) 
		VALUES ('Limonade', 0,0,0,0.8, @(p_id)), ('The vert', 0,1,0,0.8, @(p_id)), 
		('Mojito',1,0,0,2, @(p_id));""", {"p_id": playerId})
	db.execute("""INSERT INTO recette(r_qte, b_id, i_id) VALUES 
		(2,(SELECT b_id FROM boisson WHERE b_nom = 'Limonade' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'citron')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'Limonade' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'eau gazeuse')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'Mojito' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'rhum')),
		(2,(SELECT b_id FROM boisson WHERE b_nom = 'Mojito' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'menthe')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'The vert' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'the')),
		(1,(SELECT b_id FROM boisson WHERE b_nom = 'The vert' AND j_id = @(p_id)),
		(SELECT i_id FROM ingredient WHERE i_nom = 'menthe'));""", {"p_id": playerId})

# Fonction pour la route /players avec la methode POST
# Permet d'ajouter un utilisateur a la table joueur avec le budget de base
@app.route("/players", methods=["POST"])
def add_player():
	elements = request.get_json()
	name = elements['name']

	joueur = db.select("SELECT j_id FROM joueur WHERE j_pseudo = '"+ name +"';")

	if joueur == []:
		ajoutJoueur(name)
  
	coord = db.select("""SELECT z_centerX as latitude, z_centerY as longitude FROM zone WHERE j_id = 
					(SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": name})[0]
	budgetBase = db.select("""SELECT j_budget FROM joueur WHERE j_pseudo = @(nom);""",{"nom": name})[0]['j_budget']
	nbSales = db.select("""SELECT COALESCE(0,SUM(v_qte)) as nbSales FROM ventes WHERE j_id = 
						(SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": name})[0]['nbsales']
	drinksInfo = db.select("""SELECT b_nom as name, b_prixprod as price, b_alcool as hasAlcohol, b_chaud as isHot FROM boisson 
							WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = @(nom));""",{"nom": name})
	profit = budgetBase - budget_depart;

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
	result = db.select("""DELETE FROM joueur WHERE j_pseudo =  @(nom);""", {"nom": player_name})
	result = db.select(sql)
	return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------

#C: poste la meteo et l heure
#JAVA: fait un get regulier pour recupere la meteo et l heure.
@app.route('/metrology', methods=['GET','POST'])
def meteo():
	if request.method == 'POST':
		content = request.get_json()

		weather = content['meteo']
		heure = content['hour']
		forecast = content['forecast']

		result = db.select("SELECT * FROM dayinfo;")

	if result == []:
		sql = "INSERT INTO dayinfo(di_hour, di_weather, di_forecast) VALUES('"+ str(hour) +"','"+ str(meteo) +"','"+ str(forecast) +"');"
		db.execute(sql)
	else:
		sql = "UPDATE dayinfo SET (di_hour, di_weather, di_forecast) = ('"+ str(hour) +"','"+ str(meteo) +"','"+ str(forecast) +"');"
		db.execute(sql)

	result = db.select("SELECT di_hour, di_weather, di_forecast FROM dayinfo;")[0]

	print result
	return json_response({"timestamp": heure, "weather": [ {"dfn": 0, "weather": weather}, {"dfn": 1, "weather": forecast } ] })


#------------------------------------------------------------------------------------------------------------------------------------------------

# JAVA: post la trame suivante au serveur {"joueur": String, "item": String, "quantity": int }
@app.route('/sales', methods=['POST'])
def messageRecuJava():

  content = request.get_json()
  player = content['player']
  item = content['item']
  quantity = content['quantity']

  for i in dicoTest:
  	
	if i == player:
  		
		for j in dicoTest[i]['actions']:
  			
			if j['kind'] == 'drinks':
				recette = j['prepare']
  				
				if item in recette:

					if recette[item] != 0:
						
						if quantity > recette[item]:
							quantity = recette[item]
							recette[item] = 0
						else:
							recette[item] = recette[item] - quantity

						prixVente = j['price'][item]

						db = Db()
						sqlPrixVente = "UPDATE boisson SET (b_prixvente) = ('"+ str(prixVente) +"') WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "') AND b_nom = '" + item + "';"
						db.execute(sqlPrixVente)
						sqlHour = "SELECT di_hour FROM dayinfo;"
						hour = db.select(sqlHour)[0]['di_hour']
						sqlWeather = "SELECT di_weather FROM dayinfo;"
						weather = db.select(sqlWeather)[0]['di_weather']
						sqlJId = "SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "';"
						j_id = db.select(sqlJId)[0]['j_id']
						sqlBId = "SELECT b_id FROM boisson WHERE b_nom = '" + item + "' AND j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "');"
						b_id = db.select(sqlBId)[0]['b_id']
						sqlPrix = "SELECT b_prixvente FROM boisson WHERE b_nom = '" + item + "' AND j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "');"
						prixVente = db.select(sqlPrix)[0]['b_prixvente']
						sqlGetBudget = "SELECT j_budget FROM joueur WHERE j_pseudo = '"+ player +"';"
						budget = db.select(sqlGetBudget)[0]['j_budget']
						print quantity
						print prixVente
						calBudget = budget + (quantity*prixVente)
						print calBudget
						sqlBudget = "UPDATE joueur SET (j_budget) = ('"+ str(calBudget) +"') WHERE j_pseudo = '" + player + "';"
						db.execute(sqlBudget)
						sql = "INSERT INTO ventes(v_qte, v_hour, v_weather, v_prix, j_id, b_id) VALUES('" + str(quantity) + "','" + str(hour) + "','" + str(weather) + "','" + str(prixVente) + "','" + str(j_id) + "','" + str(b_id) + "');"
						db.execute(sql)
					

  			else:
  				if j['kind'] == 'ad':
  					latitude = j['location']['latitude']
  					longitude = j['location']['longitude']
  					rayon = j['radius']
  					price = j['price']

  					db = Db()
					sqlJId = "SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "';"
					j_id = db.select(sqlJId)[0]['j_id']
					print "ad"
					sqlGetBudget = "SELECT j_budget FROM joueur WHERE j_pseudo = '"+ player +"';"
					budget = db.select(sqlGetBudget)[0]['j_budget']
					calBudget = budget - price
					sqlBudget = "UPDATE joueur SET (j_budget) = ('"+ str(calBudget) +"') WHERE j_pseudo = '"+ player +"';"
					db.execute(sqlBudget)
					sql = "INSERT INTO zone(z_type, z_centerX, z_centerY, z_rayon, j_id) VALUES('ad','" + str(latitude) + "','" + str(longitude) + "','" + str(rayon) + "','" + str(j_id) + "');"
					db.execute(sql)
				
  					j = ""

  return json_response(dicoTest)



#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /actions/<player_name> avec POST
# Actions pour le lendemain
# Ne s'ajoute pas aux actions mais les remplace les actions du joueur
# Repeter chaque jour pour le lendemain
# Par defaut le serveur suppose qu'on ne veut rien faire
@app.route('/actions/<player_name>', methods=['POST'])
def action_player(player_name):
  content = request.get_json()
  dicoAction[player_name] = content
  return json_response(dicoAction)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /map avec GET
# JAVA : recupere les coordonnees de la map
@app.route('/map', methods=['GET'])
def envoieMapJava():

	db = Db()
	sql = "SELECT m_centreX as latitude, m_centreY as longitude FROM map;"
	coordinates = db.select(sql)[0]
	sqlSpan = "SELECT m_coordX as latitudeSpan, m_coordY as longitudeSpan FROM map;"
	coordinatesSpan = db.select(sqlSpan)[0]
	sqlRank = "SELECT j_pseudo as name FROM JOUEUR ORDER BY j_budget DESC;"
	ranking = db.select(sqlRank)


	region = {"center": coordinates, "span": coordinatesSpan}


	playerInfo = {}
	itemsByPlayer = {}
	rank = []
	drinksOffered = []

	print ranking
	print "----------"
	for i in ranking:
  		rank.append(i['name'])
		sqlCoord = "SELECT z_centerX as latitude, z_centerY as longitude FROM zone WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + i['name'] + "');"
		sqlBudget = "SELECT j_budget FROM joueur WHERE j_pseudo = '"+ i['name'] +"';"
		sqlSales = "SELECT COALESCE(0,SUM(v_qte)) as nbSales FROM ventes WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '"+ i['name'] +"');"
		sqlDrinks = "SELECT b_nom as name, b_prixvente as price, b_alcool as hasAlcohol, b_chaud as isHot FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + i['name'] +"');"
		coord = db.select(sqlCoord)[0]
		budgetBase = db.select(sqlBudget)[0]['j_budget']
		nbSales = db.select(sqlSales)[0]['nbsales']
		drinksInfo = db.select(sqlDrinks)[0]

		drinksOffered.append({"price": drinksInfo['price'], "name": drinksInfo['name'], "hasAlcohol": drinksInfo['hasalcohol'], "isHot": drinksInfo['ishot']})
		

		profit = budgetBase - budget_depart;
		info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksOffered}
		playerInfo[i['name']] = info
		drinksOffered = []

		sqlItems = "SELECT z_type as kind, z_centerX as latitude, z_centerY as longitude, z_rayon as influence, j_pseudo as owner FROM zone INNER JOIN joueur ON joueur.j_id = zone.j_id WHERE j_pseudo = '" + i['name'] +"';"
		items = db.select(sqlItems)
		itemsByPlayer[i['name']] = items

	mapInfo = {"region" : region, "ranking" : rank, "itemsByPlayer": itemsByPlayer,"playerInfo": playerInfo}

	return json_response(mapInfo)

#------------------------------------------------------------------------------------------------------------------------------------------------

# Fonction pour la route /map/<player_name> avec GET
# Recupere les details d'une partie
@app.route('/map/<player_name>', methods=['GET'])
def getMapPlayer(player_name):
  db = Db()
  sql = "SELECT b_nom as boisson, b_alcool as hasAlcool, b_chaud as isHot, i_nom as ingredient, i_prix as ingPrix, r_qte as quantite FROM ingredient INNER JOIN recette ON recette.i_id = ingredient.i_id INNER JOIN boisson ON boisson.b_id = recette.b_id WHERE boisson.b_id IN (SELECT b_id FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player_name +"'));"
  ingredients = db.select(sql)
 

  db = Db()
  sql = "SELECT m_centreX as latitude, m_centreY as longitude FROM map;"
  coordinates = db.select(sql)[0]
  sqlSpan = "SELECT m_coordX as latitudeSpan, m_coordY as longitudeSpan FROM map;"
  coordinatesSpan = db.select(sqlSpan)[0]
  sqlRank = "SELECT j_pseudo FROM JOUEUR ORDER BY j_budget DESC;"
  ranking = db.select(sqlRank)
 

  sqlItems = "SELECT z_type as kind, z_centerX as latitude, z_centerY as longitude, z_rayon as influence, j_pseudo as owner FROM zone INNER JOIN joueur ON joueur.j_id = zone.j_id WHERE j_pseudo = '" + player_name +"';"
  db = Db()
  items = db.select(sqlItems)
 

  print items

  region = {"center": coordinates, "span": coordinatesSpan}

  mapInfo = {"region" : region, "ranking" : ranking, "itemsByPlayer": items}
  print region
  db = Db()
  sqlCoord = "SELECT z_centerX as latitude, z_centerY as longitude FROM zone WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player_name + "');"
  sqlBudget = "SELECT j_budget FROM joueur WHERE j_pseudo = '"+ player_name +"';"
  sqlSales = "SELECT COALESCE(0,SUM(v_qte)) as nbSales FROM ventes WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '"+ player_name +"');"
  sqlDrinks = "SELECT b_nom as name, b_prixprod as price, b_alcool as hasAlcohol, b_chaud as isHot FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player_name +"');"
  coord = db.select(sqlCoord)[0]
  budgetBase = db.select(sqlBudget)[0]['j_budget']
  nbSales = db.select(sqlSales)[0]['nbsales']
  drinksInfo = db.select(sqlDrinks)
 

  profit = budgetBase - budget_depart;
  info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}

  message = {"availableIngredients": ingredients, "map": mapInfo, "playerInfo": info}
  return json_response(message)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/ingredients', methods=['GET'])
def get_ingredients():
  db = Db()
  sql = "SELECT i_nom, i_prix FROM ingredient;"
  ingredients = db.select(sql)
 
  print ingredients[1]
  return json_response(ingredients)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /inscrire/boisson avec POST
# Ajout d'une boisson en BDD
@app.route('/inscrire/boisson', methods=['POST'])
def inscriptionBoisson():
  content = request.get_json()
  nom = content['nom']
  alcool = content['alcool']
  hot = content['hot']

  db = Db()
  sql = "INSERT INTO boisson(b_nom, b_alcool, b_chaud, b_prixvente, b_prixprod, j_id) VALUES('"+ nom +"','"+ str(alcool) +"','"+ str(hot) + "', 0);"
  db.execute(sql)
 
  return json_response(content)

# Fonction pour la route /inscrire/boisson avec GET
# SELECT toutes les boissons
@app.route('/inscrire/boisson', methods=['GET'])
def getBoisson():
  db = Db()
  sql = "SELECT z_type as kind, z_centerX as X, z_centerY as Y, z_rayon as influence, j_pseudo as owner FROM zone INNER JOIN joueur ON zone.j_id = joueur.j_id WHERE zone.j_id = (SELECT j_id FROM joueur WHERE j_pseudo = 'Erwann');"
  result = db.select(sql)
 
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /inscrire/ingredient avec POST
# Ajout d'un ingredient en BDD
@app.route('/inscrire/ingredient', methods=['POST'])
def inscriptionIngredient():
  content = request.get_json()
  nom = content['nom']
  prix = content['prix']

  db = Db()
  sql = "INSERT INTO ingredient(i_nom, i_prix) VALUES('"+ nom +"','"+ str(prix) +"');"
  db.execute(sql)
 
  return json_response(content)

# Fonction pour la route /inscrire/ingredient avec GET
# SELECT tous les ingredients
@app.route('/inscrire/ingredient', methods=['GET'])
def getIngredient():
  db = Db()
  sql = "SELECT * FROM ingredient;"
  result = db.select(sql)
 
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /inscrire/recette avec POST
# Ajout d'une recette en BDD
@app.route('/inscrire/recette', methods=['POST'])
def inscriptionRecette():
  content = request.get_json()
  ing = content['ing']
  drink = content['drink']
  qte = content['qte']

  db = Db()
  sql = "INSERT INTO recette(b_id, i_id, r_qte) VALUES((SELECT b_id FROM boisson WHERE b_nom = '" + drink + "'),(SELECT i_id FROM ingredient WHERE i_nom = '" + ing + "'),'"+ str(qte) +"');"
  db.execute(sql)
 
  return json_response(content)

# Fonction pour la route /inscrire/recette avec GET
# SELECT toutes les recettes
@app.route('/inscrire/recette', methods=['GET'])
def getRecette():
  db = Db()
  sql = "SELECT * FROM recette;"
  result = db.select(sql)
 
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------

if __name__ == "__main__":
  app.run()
