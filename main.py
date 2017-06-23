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
testc = "toto"

# DATABASE_URL=postgres://<username>@localhost/<dbname> python main.py

def json_response(data="OK", status=200):
  return json.dumps(data), status, { "Content-Type": "application/json" }

@app.route("/phrases/random", methods=["GET"])
def random_phrase():
  db = Db()
  name = db.select("SELECT * FROM partie")
  db.close()
  return json_response(name)

@app.route("/pseudo", methods=["POST"])
def add_elements():
  elements = request.get_json()
  pseudo = elements['pseudo']
  
  db = Db()
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
    testc = content['meteo']
    return jsonify(content)

@app.route('/test/c', methods=['GET'])
def messageGetC():
    
    return jsonify(testc)

@app.route('/sales', methods=['POST'])
def messageRecuJava():
    content = request.get_json()

    return jsonify({"success": True})

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
