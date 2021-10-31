# SravniByWebServer
Web server for sravniBY

# API DOCS

Server address: https://our-book-store.herokuapp.com/api

1. Getting lowest prices for book - use endpoint "/book". Type: GET.  
   Necessary query parameters: bookName. Request example: https://our-book-store.herokuapp.com/api/book?bookName=Как+завоевывать+друзей+и+оказывать+влияние+на+людей  
   Description: used to getting array of lowest prices for this book in different shops.  
   Successful response code: 200 OK; json body response example: [{"shop":"Oz","price":10.87,"link":"https://oz.by/books/more10826370.html?sbtoken\u003de3295852b298bfc63672e3faf6806b95"},{"shop":"Labirint","price":9.4037,"link":"https://www.labirint.ru/books/692923/"}]