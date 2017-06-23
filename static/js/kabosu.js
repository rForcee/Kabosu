function envoyer(e) {
  		e.preventDefault()

  	      var message = {
  		      pseudo: $('#pseudo').val()
  	      }

          console.log(message);
  	      
  	      $.ajax('https://kabosu.herokuapp.com/players', {
        		type: 'POST',
        		contentType: 'application/json',
        		data: JSON.stringify(message)
  	      })
		      
	  }