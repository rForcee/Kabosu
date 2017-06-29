
function envoyer() {

      var message = {
        name: $('#pseudo').val()
      }
      
      $.ajax('https://kabosu.herokuapp.com/players', {
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(message)
      }).done(function(){
          window.open("jeu.html?player="+$('#pseudo').val(), "_self");
      });
      
}

$(function(){    
  $('#send').on('click', envoyer);
  localStorage.setItem("tableActions", "")
})
