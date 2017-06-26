import json, random
from flask import Flask, request, jsonify
from flask_cors import CORS
from db import Db

app = Flask(__name__)
app.debug = True
CORS(app)

budget = 10

# DATABASE_URL=postgres://<username>@localhost/<dbname> python main.py

# Fonction pour les reponses en Json
def json_response(data="OK", status=200):
  return json.dumps(data), status, { "Content-Type": "application/json" }


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /reset avec GET
# Reinitialise une partie
#TODO
@app.route("/reset", methods=["GET"])
def reset_partie():
  db = Db()
  sqlDeleteMenu = "DELETE FROM menu;"
  sqlDeleteVentes = "DELETE FROM ventes;"
  sqlDeletePub = "DELETE FROM pub;"
  sqlDeleteJoueur = "DELETE FROM joueur;"
  sql = sqlDeleteMenu + sqlDeleteVentes + sqlDeletePub + sqlDeleteJoueur
  db.execute(sql)
  db.close()
  return json_response(result)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /players avec GET
# Renvoie tout ce qu'il y a dans la table players
@app.route("/players", methods=["GET"])
def get_players():
  db = Db()
  sql = "SELECT * FROM joueur"
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
  sql = "SELECT * FROM joueur"
  joueurs = db.select(sql)
  db.close()

  if joueurs == []:
    coordX = random.randrange(330,670,1)
    coordY = random.randrange(130,470,1)
    sqlDeleteMenu = "DELETE FROM menu;"
    sqlDeleteVentes = "DELETE FROM ventes;"
    sqlDeletePub = "DELETE FROM pub;"
    sqlDeleteJoueur = "DELETE FROM joueur;"
    sqlInsertJoueur = "INSERT INTO joueur(j_pseudo, j_budget, j_coordX, j_coordY, m_id) VALUES('"+ name +"','"+ str(budget) +"','"+ str(coordX) +"','"+ str(coordY) +"',(SELECT m_id FROM map LIMIT 1));"
    sql = sqlDeleteMenu + sqlDeleteVentes + sqlDeletePub + sqlDeleteJoueur + sqlInsertPlayer 
    db = Db()
    db.execute(sql)
    db.close()

  else:
    db = Db()
    sql = "SELECT j_id FROM joueur WHERE j_pseudo = '"+ name +"';"
    joueur = db.select(sql)
    db.close()

    if joueur == []:
      db = Db()
      sql = "INSERT INTO joueur(j_pseudo, j_budget, j_coordX, j_coordY, m_id) VALUES('"+ name +"','"+ str(budget) +"','"+ str(coordX) +"','"+ str(coordY) +"',(SELECT m_id FROM map LIMIT 1));"
      joueur = db.select(sql)
      db.close()

  sqlName = "SELECT j_coordX, j_coordY FROM joueur WHERE j_pseudo = '"+ name +"';"
  db = Db()
  coord = db.select(sql)
  db.close()

  #message = {"name": name, "location": coord, "info":}

  return json_response({"success": True})


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /players/<player_name> avec DELETE
# Supprime un joueur de la partie
# OPTIONNEL
@app.route("/players/<player_name>", methods=["DELETE"])
def delete_player():
  db = Db()
  sql = "SELECT * FROM joueur;"
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
  result = db.select(sql)
  db.close()
  print result
  return json_response(result)


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
  hour = db.select(sqlHour)
  sqlWeather = "SELECT di_weather FROM dayinfo;"
  weather = db.select(sqlWeather)
  sqlJId = "SELECT j_id FROM joueur WHERE j_pseudo = '" + player + "';"
  j_id = db.select(sqlJId)
  sqlBId = "SELECT b_id FROM boisson WHERE b_nom = '" + item + "';"
  b_id = db.select(sqlBId)
  sql = "INSERT INTO ventes(v_qte, v_hour, v_weather, j_id, b_id) VALUES('"+ quantity +"','"+ hour +"','"+ weather + "','"+j_id+"','"+b_id+"');"
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
def action_player():
  content = request.get_json()

  return json_response({"success": True})


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /map avec GET
# JAVA : recupere les coordonnees de la map
@app.route('/map', methods=['GET'])
def envoieMapJava():
  db = Db()
  sql = "SELECT * FROM map;"
  infoMap = db.select(sql)
  db.close()
  return json_response(infoMap)


#------------------------------------------------------------------------------------------------------------------------------------------------

# Fonction pour la route /map/<player_name> avec GET
# Recupere les details d'une partie
@app.route('/map/<player_name>', methods=['GET'])
def getMapPlayer():
  db = Db()
  sql = "SELECT * FROM map, joueur, boisson;"
  infoMap = db.select(sql)
  db.close()
  return json_response(infoMap)


#------------------------------------------------------------------------------------------------------------------------------------------------


# Fonction pour la route /ingredients avec GET
# Recupere la liste des ingredients
@app.route('/ingredients', methods=['GET'])
def get_ingredients():
  db = Db()
  sql = "SELECT * FROM ingredient;"
  infoMap = db.select(sql)
  db.close()
  return json_response(infoMap)


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
  sql = "INSERT INTO boisson(b_nom, b_alcool, b_chaud) VALUES('"+ nom +"','"+ str(alcool) +"','"+ str(hot) + "');"
  db.execute(sql)
  db.close()
  return json_response(content)

# Fonction pour la route /inscrire/boisson avec GET
# SELECT toutes les boissons
@app.route('/inscrire/boisson', methods=['GET'])
def getBoisson():
  db = Db()
  sql = "SELECT * FROM boisson;"
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
