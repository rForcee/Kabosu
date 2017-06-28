
function envoyer() {

      console.log("test")
      
      var message = {
        name: $('#pseudo').val()
      }
      
      $.ajax('https://kabosu.herokuapp.com/players', {
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(message)
      }).done(function(){
      console.log("testDe")

          window.open("jeu.html?player="+$('#pseudo').val(), "_self");
      });
      
}
