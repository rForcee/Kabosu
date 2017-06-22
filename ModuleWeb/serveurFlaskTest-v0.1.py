from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route('/sales', methods=['POST'])
def messageRecuJava():
    content = request.json
    print (content)
    return jsonify({"success": True})

@app.route('/player', methods=['POST'])
def messageRecuCWeb():
    content = request.json
    print (content)
    return jsonify({"success": True})

@app.route('/metrology', methods=['GET', 'POST'])
def messageRecuC():
    content = request.json
    print (content)
    return jsonify(content)

@app.route('/map', methods=['GET'])
def envoieMapJava():
    content = "{toto}"
    print (content)
    return jsonify(content)
if __name__ == '__main__':
    app.run(host= '0.0.0.0',debug=True)
