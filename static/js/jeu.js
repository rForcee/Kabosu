var playerName;
var tableWeather = ['rainy', 'cloudy', 'sunny', 'heatwave', 'thunderstorm']


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
    ingredientsPlayer();
});

var message = "<table><thead><tr><th>Ingrédient</th><th>Quantité</th><th>Prix unitaire</th></tr></thead><tbody><tr><td>Citron</td><td>2</td><td>0.2€</td></tr><tr><td>Eau gazeuse</td><td>1</td><td>0.4</td></tr></tbody></table>"
$(document).ready(function(){
  $('[data-toggle="popover"]').popover({
    content: message
  });   
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
			day = Math.floor(hour / 24);
			$('#weather').text(tableWeather[previsionWeather]);
			$('#day').text("Jour " + day);
			$('#hour').text(hour % 24 + ":00");

	});
}

function mapPlayer() {
	$.ajax('https://kabosu.herokuapp.com/map/'+ playerName)
       .done(function(data){
       		$('#money').text(data.playerInfo.cash + "€");
			console.log(data.playerInfo.cash);
			for(drinks in data.playerInfo.drinksOffered)
			{
				console.log("drinks:" + drinks)
			}
			var ligne = "<tr id=\"" + nom + "\">"+
              "<td>"+"nom"+"</td>"+
              "<td><input type=\"number\" min=\"0\" name=\""+ nom +"\" class=\"form-control\"></td>" +
              "<td><input type=\"text\" name=\""+ nom +"\" class=\"form-control\"></td>" +
              "<td></td>" +
              "<td></td>" +
            "</tr>";
	});
}

function ingredientsPlayer() {
	$.ajax('https://kabosu.herokuapp.com/ingredients/'+ playerName)
       .done(function(data){
			var tbl_body = document.createElement("tbody");
		    var odd_even = false;
		    $.each(data, function() {
	        var tbl_row = tbl_body.insertRow();
	        tbl_row.className = odd_even ? "odd" : "even";
	        $.each(this, function(k , v) {
            var cell = tbl_row.insertCell();
            cell.appendChild(document.createTextNode(v.toString()));
        	})        
        odd_even = !odd_even;               
    	})
    $("#target_table_id").appendChild(tbl_body);
	});
}

window.setInterval(metrology, 3000);