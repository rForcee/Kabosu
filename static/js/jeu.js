var playerName;
var tableWeather = ['rainy', 'cloudy', 'sunny', 'heatwave', 'thunderstorm']
var ingredientsListe;


$.urlParam = function(name){
  var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
  if (results==null){
     return null;
  }
  else{
     return decodeURI(results[1]) || 0;
  }
}

$( document ).ready(function() {
    playerName = decodeURIComponent($.urlParam('player'));
    metrology();
    $('#name').text(playerName);
    mapPlayer();
    $('#validation').on('click', getActions);
  	$('[data-toggle="popover"]').popover();   
  	tableActions = localStorage.getItem("tableActions");
});

$(document).ready(function(){
});

function metrology() {
	$.ajax('https://kabosu.herokuapp.com/metrology')
       .done(function(data){
			var hour = data.timestamp;
			var currentWeather;
			var previsionWeather;
			for (var time in data.weather)
			{
				if (data.weather[time].dfn == 0)
				{
					currentWeather = data.weather[time].weather;
				}
				else
				{
					previsionWeather = data.weather[time].weather;
				}
			}
			var dayPrev = localStorage.getItem("dayPrev")
			var day = Math.floor(hour / 24) + 1;
			if(dayPrev != day)
			{
				//sendActions();
				alert("Changement de jour")
			}
			dayPrev = day;
			localStorage.setItem("dayPrev", dayPrev)
			$('#weather').text(tableWeather[previsionWeather]);
			$('#day').text("Jour " + day);
			$('#hour').text(hour % 24 + ":00");

	});
}


function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function mapPlayer() {
	$.ajax('https://kabosu.herokuapp.com/map/'+ playerName)
       .done(function(data){
       		$('#money').text(data.playerInfo.cash + "€");
			$.ajax('https://kabosu.herokuapp.com/ingredients/'+ playerName)
		       .done(function(ingredients){
		       	ingredientsListe = ingredients;
			

				for(drinks in data.playerInfo.drinksOffered)
				{
					nom = data.playerInfo.drinksOffered[drinks].name;
					console.log(nom)
					price = data.playerInfo.drinksOffered[drinks].price;

					var hasAlcohol;
					var isCold;

					if(data.playerInfo.drinksOffered[drinks].hasAlcohol == true)
					{
						hasAlcohol = "Y"
					}
					else
					{
						hasAlcohol = "N"
					}

					if(data.playerInfo.drinksOffered[drinks].isCold == true)
					{
						isCold = "Y"
					}
					else
					{
						isCold = "N"
					}

					var dataContent = "";
					console.log(ingredientsListe)
					for(ingredient in ingredientsListe)
					{
						console.log(ingredient)
						if(ingredientsListe[ingredient].b_nom == nom)
						{
							dataContent = dataContent + "<tr><td>"+ ingredientsListe[ingredient].i_nom + "</td><td>" + ingredientsListe[ingredient].r_qte + "</td><td>" + ingredientsListe[ingredient].i_prix + "€</td></tr>"
							console.log(dataContent)
						}
					}
					content = "<table><thead><tr><th>Ingrédient</th><th>Quantité</th><th>Prix unitaire</th></tr></thead><tbody>" + dataContent +"</tbody></table>"

					var ligne = "<tr id=\"" + nom + "\">"+
		              "<td class=\"boissonsTD\"><a href=\"#\" title=\"Recette : " + capitalizeFirstLetter(nom) +"\" data-toggle=\"popover\" data-html=\"true\" data-trigger=\"focus\" data-content=\"" + content+ "\" class=\"recette\">"+nom+"</a></td>"+
		              "<td><input type=\"number\" min=\"0\" name=\""+ nom +"\" class=\"form-control prod\"></td>" +
		              "<td><input type=\"text\" name=\""+ nom +"\" class=\"form-control prixvente\"></td>" +
		              "<td>"+ price +"€</td>" +
		              "<td>"+ hasAlcohol +"</td>" + "<td>"+ isCold +"</td>" +
		            "</tr>";

		            $('#boissons > tbody:last-child').append(ligne);

		    	}
				$('[data-toggle="popover"]').popover();   

        	});
	});
}

function getActions() {
	$.ajax('https://kabosu.herokuapp.com/map/'+ playerName)
       .done(function(data){
       	var tableActions = []
			for(drinks in data.playerInfo.drinksOffered)
			{
				nom = data.playerInfo.drinksOffered[drinks].name;
				var prod = $('#'+nom+' > td > .prod').val()
				var vente = $('#'+nom+' > td > .prixvente').val()
				if(prod == "")
					prod = 0
				if(vente == "")
					vente = 0

				var prepare = {};
				prepare[nom] = prod;        
				var pricePrep = {};
				pricePrep[nom] = price;        
				var ajout = {"kind": "drinks", "prepare": prepare, "price": price}
				console.log(tableActions)
				tableActions.push(ajout)
			}
			localStorage.setItem("tableActions", tableActions)
			console.log(tableActions)
       	});
}

function sendActions() {
	var message = localStorage.getItem("tableActions")
	data = {"actions" : message, "simulated": false}
	$.ajax('https://kabosu.herokuapp.com/actions/' + playerName, {
              type: 'POST',
              contentType: 'application/json',
              data: JSON.stringify(data)
            });
}

window.setInterval(metrology, 3000);