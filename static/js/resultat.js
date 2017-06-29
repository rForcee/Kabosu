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
    alert(tableActions);
});

function mapPlayer() {
	$.ajax('https://kabosu.herokuapp.com/map/'+ playerName)
       .done(function(data){
       		$('#sales').text(data.playerInfo.sales);

       		if((data.playerInfo.profit) > 0)
       		{
       			$('#profit').text("+" + data.playerInfo.profit + "€");
       		}
       		else
       		{
       			$('#profit').text(data.playerInfo.profit + "€");
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
	});
}