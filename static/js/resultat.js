var playerName;
var tableWeather = ['rainy', 'cloudy', 'sunny', 'heatwave', 'thunderstorm']
var tableActions;


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
    $('#name').text(playerName);
    mapPlayer();
    tableActions = localStorage.getItem("tableActions");
});

function mapPlayer() {
	$.ajax('https://kabosu.herokuapp.com/map/'+ playerName)
       .done(function(data){
       		$('#sales').text(data.playerInfo.sales);

       		if((data.playerInfo.profit) > 0)
       		{
       			$('#profit').text("+" + data.playerInfo.profit.toFixed(2) + "€");
       		}
       		else
       		{
       			$('#profit').text(data.playerInfo.profit.toFixed(2) + "€");
       		}
       		
       		var classement = data.map.ranking.indexOf(playerName) + 1;
       		var nbJoueurs = data.map.ranking.length;
       		$('#ranking').text(classement + "/" + nbJoueurs);

       		var cptPubs = 0;
       		for(zone in data.map.itemsByPlayer)
       		{
       			console.log(data.map.itemsByPlayer[zone].kind)
       			if(data.map.itemsByPlayer[zone].kind == 'ad')
       			{
       				cptPubs++;
       			}
       		}

       		$('#pub').text(cptPubs);
			console.log("Toto")

       		function ventes() {
				$.ajax('https://kabosu.herokuapp.com/sales/' + playerName)
			       .done(function(listVentes){

			       	for(drinks in data.playerInfo.drinksOffered)
					{

						nom = data.playerInfo.drinksOffered[drinks].name;
						console.log(nom)

						var weatherT = [0,0,0,0,0]						

						for(vente in listVentes)
						{
							if(listVentes[vente].boisson == nom)
							{
								weatherT[listVentes[vente].weather] = listVentes[vente].quantite
							}
						}
						var line = "<tr><td>"+ nom + "</td><td>" + weatherT[0] + "</td><td>" + weatherT[1] + "</td><td>" + weatherT[2] + "</td><td>" + weatherT[3] + "</td><td>" + weatherT[4] + "</td></tr>"
						console.log(line)
			            $('#ventes > tbody:last-child').append(line);

		    		}
				});
			}

	});
}



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
				sendActions();
				//alert("Changement de jour")
			}
			dayPrev = day;
			localStorage.setItem("dayPrev", dayPrev)
			$('#weather').text(tableWeather[previsionWeather]);
			$('#day').text("Jour " + day);
			$('#hour').text(hour % 24 + ":00");

	});
}

function sendActions() {
	if(budget > 0)
	{
		var messageJSON = localStorage.getItem("tableActions")
		message = JSON.parse(messageJSON)
		console.log(message)
		if(message == "")
			message = []
		data = {"actions" : message, "simulated": false}
		$.ajax('https://kabosu.herokuapp.com/actions/' + playerName, {
	              type: 'POST',
	              contentType: 'application/json',
	              data: JSON.stringify(data)
	            });
		localStorage.setItem("tableActions", "")
	}
}

window.setInterval(metrology, 3000);