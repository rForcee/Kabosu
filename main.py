import json, random
from flask import Flask, request, jsonify
from flask_cors import CORS
from db import Db

app = Flask(__name__)
app.debug = True
CORS(app)

names = ["tree", "apple", "computer", "phone", "fruit"]
verbs = ["eats", "cuts", "sleeps", "dies", "stops"]
adjectives = ["beautiful", "cute", "gross", "horrible", "ugly"]
testc = ""

# DATABASE_URL=postgres://<username>@localhost/<dbname> python main.py

# Fonction pour les réponses en Json
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

# Fonction pour la route /players avec la méthode POST
# Permet d'ajouter un 
@app.route("/players", methods=["POST"])
def add_elements():
  elements = request.get_json()
  pseudo = elements['pseudo']
  
  db = Db()
  db.execute("""
    DELETE * FROM partie;
  """)
  db.execute("""
    INSERT INTO partie(p_nom) VALUES (@(pseudo));
  """, elements)
  db.close()

  return json_response(pseudo)

@app.route("/phrases/test", methods=['GET'])
def randomSentence():
	name = random.choice(names) 
	verb = random.choice(verbs) 
	adj = random.choice(adjectives)
	message = [name, adj, verb]
	return json_response(message)

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
