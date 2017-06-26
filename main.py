import json, random
from flask import Flask, request, jsonify
from flask_cors import CORS
from db import Db

app = Flask(__name__)
app.debug = True
CORS(app)

players = []
budget = 10

# DATABASE_URL=postgres://<username>@localhost/<dbname> python main.py

# Fonction pour les reponses en Json
def json_response(data="OK", status=200):
  return json.dumps(data), status, { "Content-Type": "application/json" }

# Fonction pour la route /phrases/random avec GET
# Fonction de TEST : renvoie tout ce qu'il y a dans la table PARTIE
@app.route("/parties", methods=["GET"])
def get_parties():
  db = Db()
  sql = "SELECT * FROM partie"
  result = db.select(sql)
  db.close()
  return json_response(result)

# Fonction pour la route /reset avec GET
# Reinitialise une partie
@app.route("/reset", methods=["GET"])
def reset_partie():
  db = Db()
  sql = "SELECT * FROM joueur"
  result = db.select(sql)
  db.close()
  return json_response(result)

# Fonction pour la route /phrases/random avec GET
# Fonction de TEST : renvoie tout ce qu'il y a dans la table PARTIE
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
  global players
  print players

  if len(players) == 0:
	  db = Db()
	  sqlDeleteMap = "DELETE FROM map;"
	  sqlDeleteJoueur = "DELETE FROM joueur;"
	  sqlDeletePartie = "DELETE FROM partie;"
	  sqlInsertPartie = "INSERT INTO partie(p_nom) VALUES('" + "partie" +"');"
	  sqlInsertMap = "INSERT INTO map(m_centreX, m_centreY, m_coordX, m_coordY, p_id) VALUES(100,100,50,50,(SELECT p_id FROM partie LIMIT 1));"
	  sqlInsertPlayer = "INSERT INTO joueur(j_pseudo, j_budget, p_id) VALUES('"+ name +"','"+ str(budget) +"', (SELECT p_id FROM partie LIMIT 1));"
	  sql = sqlDeleteMap + sqlDeleteJoueur + sqlDeletePartie + sqlInsertPartie + sqlInsertMap + sqlInsertPlayer
	  db.execute(sql)
	  db.close()
	  players.append(name)

  else:
  	  db = Db()
  	  sqlInsertPlayer = "INSERT INTO joueur(j_pseudo, j_budget, p_id) VALUES('"+ name +"','"+ str(budget) +"', (SELECT p_id FROM partie LIMIT 1));"
	  db.execute(sqlInsertPlayer)
	  db.close()
	  players.append(name)

  
  
  return json_response()

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
  sql = "SELECT * FROM boisson WHERE b_id = 12"
  result = db.select(sql)
  db.close()
  if result == []:
    print "INSIDE"
  return json_response(result)

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
  sql = "SELECT * FROM ingredient"
  result = db.select(sql)
  db.close()
  return json_response(result)

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
  sql = "SELECT * FROM recette"
  result = db.select(sql)
  db.close()
  return json_response(result)

@app.route('/test/c', methods=['POST'])
def messageRecuC():
  content = request.get_json()
  print content['hour']
  print content
  global testc 
  testc = content
  return json_response(content)

@app.route('/test/c', methods=['GET'])
def messageGetC():
    
  return json_response(testc)

@app.route('/sales', methods=['POST'])
def messageRecuJava():
  content = request.get_json()

  return json_response({"success": True})

@app.route('/map', methods=['GET'])
def envoieMapJava():
  db = Db()
  infoMap = db.select("SELECT * FROM map")
  db.close()
  return json_response(infoMap)

@app.route('/metrology', methods=['GET','POST'])
def meteo():
  global meteo
  if request.method == 'POST':
      content = request.get_json()
      meteo = content['meteo']
      return jsonify({"success": True})
  else:
      return json_response(meteo)

if __name__ == "__main__":
  app.run()
