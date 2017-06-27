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
dicoTest = {}

# DATABASE_URL=postgres://<username>@localhost/<dbname> python main.py

# Fonction pour les reponses en Json
def json_response(data="OK", status=200):
  return json.dumps(data), status, { "Content-Type": "application/json" }


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /reset avec GET
# Reinitialise une partie
@app.route("/reset", methods=["GET"])
def reset_partie():
  db = Db()
  sqlDeleteVentes = "DELETE FROM ventes;"
  sqlDeleteJoueur = "DELETE FROM joueur;"
  sqlDeleteZone = "DELETE FROM zone;"
  sqlDeleteDayInfo = "DELETE FROM dayinfo;"
  sql = sqlDeleteZone + sqlDeleteVentes + sqlDeleteJoueur + sqlDeleteDayInfo
  db.execute(sql)
  db.close()
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /players avec GET
# Renvoie tout ce qu'il y a dans la table players
@app.route("/players", methods=["GET"])
def get_players():
  db = Db()
  sql = "SELECT * FROM joueur;"
  result = db.select(sql)
  db.close()
  return json_response(result)

# Fonction pour la route /players avec la methode POST
# Permet d'ajouter un utilisateur a la table joueur avec le budget de base
@app.route("/players", methods=["POST"])
def add_player():
  elements = request.get_json()
  name = elements['name']

  db = Db()
  sql = "SELECT j_id FROM joueur WHERE j_pseudo = '"+ name +"';"
  joueur = db.select(sql)
  db.close()

  if joueur == []:
    coordX = random.randrange(330,670,1)
    coordY = random.randrange(130,470,1)
    db = Db()
    sqlInsertJoueur = "INSERT INTO joueur(j_pseudo, j_budget) VALUES('"+ name +"','"+ str(budget_depart) +"');"
    db.execute(sqlInsertJoueur)
    playerId = db.select("SELECT j_id FROM joueur WHERE j_pseudo = '" + name + "';")[0]['j_id']
    sqlInsertZone = "INSERT INTO zone(z_type, z_centerX, z_centerY, z_rayon, j_id) VALUES('"+ "stand" +"','"+ str(coordX) +"','"+ str(coordY) +"','"+ str(rayonInfluenceStand) +"',(SELECT j_id FROM joueur WHERE j_pseudo = '" + name + "'));"
    sqlInsertBoisson = "INSERT INTO boisson(b_nom, b_alcool, b_chaud, b_prixvente, b_prixprod, j_id) VALUES ('Limonade', 0,0,0,0.8, "+str(playerId)+"), ('The vert', 0,1,0,0.8, "+str(playerId)+"), ('Mojito',1,0,0,2, "+str(playerId)+");"
    sqlInsertRecette = "insert into recette(r_qte, b_id, i_id) values (2,(select b_id from boisson where b_nom = 'Limonade' and j_id = (select j_id from joueur where j_pseudo = '"+ name +"')),(select i_id from ingredient where i_nom = 'citron')),(1,(select b_id from boisson where b_nom = 'Limonade' and j_id = (select j_id from joueur where j_pseudo = '"+ name +"')),(select i_id from ingredient where i_nom = 'eau gazeuse')),(1,(select b_id from boisson where b_nom = 'Mojito' and j_id = (select j_id from joueur where j_pseudo = '"+ name +"')),(select i_id from ingredient where i_nom = 'rhum')),(2,(select b_id from boisson where b_nom = 'Mojito' and j_id = (select j_id from joueur where j_pseudo = '"+ name +"')),(select i_id from ingredient where i_nom = 'menthe')),(1,(select b_id from boisson where b_nom = 'The vert' and j_id = (select j_id from joueur where j_pseudo = '"+ name +"')),(select i_id from ingredient where i_nom = 'the')),(1,(select b_id from boisson where b_nom = 'The vert' and j_id = (select j_id from joueur where j_pseudo = '"+ name +"')),(select i_id from ingredient where i_nom = 'menthe'));";
    sql = sqlInsertZone + sqlInsertBoisson + sqlInsertRecette
    db.execute(sql)
    db.close()

  
  db = Db()
  sqlCoord = "SELECT z_centerX as latitude, z_centerY as longitude FROM zone WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + name + "');"
  sqlBudget = "SELECT j_budget FROM joueur WHERE j_pseudo = '"+ name +"';"
  sqlSales = "SELECT COALESCE(0,SUM(v_qte)) as nbSales FROM ventes WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '"+ name +"');"
  sqlDrinks = "SELECT b_nom as name, b_prixprod as price, b_alcool as hasAlcohol, b_chaud as isHot FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + name +"');"
  coord = db.select(sqlCoord)[0]
  budgetBase = db.select(sqlBudget)[0]['j_budget']
  nbSales = db.select(sqlSales)[0]['nbsales']
  drinksInfo = db.select(sqlDrinks)
  db.close()
  print nbSales
  print budgetBase
  print drinksInfo
  print coord
  profit = budgetBase - budget_depart;
  info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}

  message = {"name": name, "location": coord, "info": info}

  print message;

  return json_response(message)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /players/<player_name> avec DELETE
# Supprime un joueur de la partie
# OPTIONNEL
@app.route("/players/<player_name>", methods=["DELETE"])
def delete_player(player_name):
  db = Db()
  sql = "DELETE FROM joueur WHERE j_pseudo = '" + player_name + "';"
  result = db.select(sql)
  db.close()
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------

#C: poste la meteo et l heure
#JAVA: fait un get regulier pour recupere la meteo et l heure.
@app.route('/metrology', methods=['GET','POST'])
def meteo():
  if request.method == 'POST':
    content = request.get_json()
    print content
    meteo = content['meteo']
    hour = content['hour']
    forecast = content['forecast']

    db = Db()
    sql = "SELECT * FROM dayinfo;"
    result = db.select(sql)
    db.close()

    if result == []:
      db = Db()
      sql = "INSERT INTO dayinfo(di_hour, di_weather, di_forecast) VALUES('"+ str(hour) +"','"+ str(meteo) +"','"+ str(forecast) +"');"
      db.execute(sql)
      db.close()
    else:
      db = Db()
      sql = "UPDATE dayinfo SET (di_hour, di_weather, di_forecast) = ('"+ str(hour) +"','"+ str(meteo) +"','"+ str(forecast) +"');"
      db.execute(sql)
      db.close()

  db = Db()
  sql = "SELECT di_hour, di_weather, di_forecast FROM dayinfo;"
  result = db.select(sql)[0]
  db.close()
  print result
  return json_response({"hour": result['di_hour'], "weather": result['di_weather'], "forecast": result['di_forecast']})


#------------------------------------------------------------------------------------------------------------------------------------------------

# JAVA: post la trame suivante au serveur {"joueur": String, "item": String, "quantity": int }
@app.route('/sales', methods=['POST'])
def messageRecuJava():
  content = request.get_json()
  player = content['player']
  item = content['item']
  quantity = content['quantity']
  db = Db()
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
  calBudget = budget + (quantity*prixVente)
  sqlBudget = "UPDATE joueur SET (j_budget) = ('"+ str(calBudget) +"') WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "');"
  db.execute(sqlBudget)
  sql = "INSERT INTO ventes(v_qte, v_hour, v_weather, v_prix, j_id, b_id) VALUES('" + str(quantity) + "','" + str(hour) + "','" + str(weather) + "','" + str(prixVente) + "','" + str(j_id) + "','" + str(b_id) + "');"
  db.execute(sql)
  db.close()
  return json_response({"success": True})


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
  return json_response({"success": True})


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /map avec GET
# JAVA : recupere les coordonnees de la map
@app.route('/map', methods=['GET'])
def envoieMapJava():
  db = Db()
  sqlMap = "SELECT * FROM map;"
  infoMap = db.select(sqlMap)
  sqlItem = "SELECT z_type, z_centerX, z_centerY, z_rayon, j_pseudo FROM zone INNER JOIN joueur ON joueur.j_id = zone.j_id;"
  item = db.select(sqlItem)
  sqlBudget = "SELECT j_id , j_budget FROM joueur;"
  joueurId = db.select(sqlBudget)[0]['j_id']
  sqlSales = "SELECT COALESCE(0,SUM(v_qte)) as nbSales FROM ventes WHERE j_id = '"+str(joueurId)+"';"
  sqlDrinks = "SELECT b_nom as name, b_prixprod as price, b_alcool as hasAlcohol, b_chaud as isHot FROM boisson WHERE j_id = '"+str(joueurId)+"';"
  budgetBase = db.select(sqlBudget)[0]['j_budget']
  nbSales = db.select(sqlSales)[0]['nbsales']
  drinksInfo = db.select(sqlDrinks)
  db.close()
  print nbSales
  print budgetBase
  print drinksInfo
  profit = budgetBase - budget_depart;
  info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}
  sqlRank = "SELECT j_pseudo FROM joueur ORDER BY j_budget;"
  ranking = db.select(sqlRank)[0]['j_pseudo']
  map = infoMap+item
  db.close()
  print [ranking]
  print infoMap
  print playerInfo
  print map
  return json_response({"map": map, "playerInfo": info, "Rank": rank})


#------------------------------------------------------------------------------------------------------------------------------------------------

# Fonction pour la route /map/<player_name> avec GET
# Recupere les details d'une partie
@app.route('/map/<player_name>', methods=['GET'])
def getMapPlayer(player_name):
  db = Db()
  sql = "SELECT b_nom as boisson, b_alcool as hasAlcool, b_chaud as isHot, i_nom as ingredient, i_prix as ingPrix, r_qte as quantite FROM ingredient INNER JOIN recette ON recette.i_id = ingredient.i_id INNER JOIN boisson ON boisson.b_id = recette.b_id WHERE boisson.b_id IN (SELECT b_id FROM boisson WHERE j_id = (SELECT j_id FROM joueur WHERE j_pseudo = '" + player_name +"'));"
  ingredients = db.select(sql)
  db.close()

  db = Db()
  sql = "SELECT m_centreX as latitude, m_centreY as longitude FROM map;"
  coordinates = db.select(sql)[0]
  sqlSpan = "SELECT m_coordX as latitudeSpan, m_coordY as longitudeSpan FROM map;"
  coordinatesSpan = db.select(sqlSpan)[0]
  sqlRank = "SELECT j_pseudo FROM JOUEUR ORDER BY j_budget DESC;"
  ranking = db.select(sqlRank)
  db.close()

  region = {"center": coordinates, "span": coordinatesSpan}

  mapInfo = {"region" : region, "ranking" : ranking}
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
  db.close()

  print nbSales
  print budgetBase
  print drinksInfo
  print coord
  profit = budgetBase - budget_depart;
  info = {"cash": budgetBase, "sales": nbSales, "profit": profit, "drinksOffered": drinksInfo}

  message = {"availableIngredients": ingredients, "map": mapInfo,"playerInfo": info}
  return json_response(message)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/testDico/<player>', methods=['POST'])
def test_dico(player):
  content = request.get_json()
  dicoTest[player] = content
  print dicoTest
  print "--------"
  return json_response(dicoTest)


# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/testDico', methods=['GET'])
def test_lecture():
  for i in dicoTest:
  	print dicoTest[i]
  	print "---------"
  	print i

  return json_response(dicoTest)

# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/testDico', methods=['POST'])
def test_lecturePost():
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
						sqlBudget = "UPDATE joueur SET (j_budget) = ('"+ str(calBudget) +"') WHERE j_pseudo = '" + player + "'';"
						db.execute(sqlBudget)
						sql = "INSERT INTO ventes(v_qte, v_hour, v_weather, v_prix, j_id, b_id) VALUES('" + str(quantity) + "','" + str(hour) + "','" + str(weather) + "','" + str(prixVente) + "','" + str(j_id) + "','" + str(b_id) + "');"
						db.execute(sql)
						db.close()

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
					db.close()
  					j = ""

  return json_response(dicoTest)


# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/ingredients', methods=['GET'])
def get_ingredients():
  db = Db()
  sql = "SELECT i_nom, i_prix FROM ingredient;"
  ingredients = db.select(sql)
  db.close()
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
  db.close()
  return json_response(content)

# Fonction pour la route /inscrire/boisson avec GET
# SELECT toutes les boissons
@app.route('/inscrire/boisson', methods=['GET'])
def getBoisson():
  db = Db()
  sql = "SELECT z_type as kind, z_centerX as X, z_centerY as Y, z_rayon as influence, j_pseudo as owner FROM zone INNER JOIN joueur ON zone.j_id = joueur.j_id WHERE zone.j_id = (SELECT j_id FROM joueur WHERE j_pseudo = 'Erwann');"
  result = db.select(sql)
  db.close()
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
  db.close()
  return json_response(content)

# Fonction pour la route /inscrire/ingredient avec GET
# SELECT tous les ingredients
@app.route('/inscrire/ingredient', methods=['GET'])
def getIngredient():
  db = Db()
  sql = "SELECT * FROM ingredient;"
  result = db.select(sql)
  db.close()
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
  db.close()
  return json_response(content)

# Fonction pour la route /inscrire/recette avec GET
# SELECT toutes les recettes
@app.route('/inscrire/recette', methods=['GET'])
def getRecette():
  db = Db()
  sql = "SELECT * FROM recette;"
  result = db.select(sql)
  db.close()
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------

if __name__ == "__main__":
  app.run()
