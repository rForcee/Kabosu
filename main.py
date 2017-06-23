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
@app.route("/phrases/random", methods=["GET"])
def random_phrase():
  db = Db()
  name = db.select("SELECT * FROM partie")
  db.close()
  return json_response(name)

# Fonction pour la route /players avec la methode POST
# Permet d'ajouter un utilisateur a la table joueur avec le budget de base
@app.route("/players", methods=["POST"])
def add_elements():
  elements = request.get_json()
  name = elements['name']
  
  print players

  if len(players) == 0:
	  db = Db()
	  sqlInsertPartie = "INSERT INTO partie(p_nom) VALUES('" + "partie" +"');"
	  sqlInsertMap = "INSERT INTO map(m_centreX, m_centreY, m_coordX, m_coordY, p_id) VALUES(100,100,50,50,(SELECT p_id FROM partie LIMIT 1));"
	  sqlInsertPlayer = "INSERT INTO joueur(j_pseudo, j_budget, p_id) VALUES('"+ name +"','"+ str(budget) +"', (SELECT p_id FROM partie LIMIT 1));"
	  sql = sqlInsertPartie + sqlInsertMap + sqlInsertPlayer
	  db.execute(sql)
	  db.close()
	  players.append(name)

  else:
  	  db = Db()
  	  sqlInsertPlayer = "INSERT INTO joueur(j_pseudo, j_budget, p_id) VALUES('"+ name +"','"+ budget +"', (SELECT p_id FROM partie LIMIT 1));"
	  db.execute(sqlInsertPlayer)
	  db.close()
	  players.append(name)

  return json_response(players)

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
