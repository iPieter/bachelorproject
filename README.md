# Bachelorproject: Televic
Deze repository bevat de code voor het bachelorproject.

## 1 Toevoegen van een nieuwe feature in Java EE
Stel dat we een Task klasse willen maken:
### 1 Task klasse (Database layer)
Deze klasse is de verbinding tussen Java en de database, hier moeten dus alle attributen beschreven worden.
### 2 TaskEJB klasse (Business logic layer)
Deze klasse maakt nieuwe Taken aan en valideert deze. Er worden hier ook methoden voorzien die meer doen dan alleen
taken ophalen of verwijderen.

Vaak wordt er een interface gebruikt waarvan de TaskEJB dan een specifieke implementatie is.
### 3 TaskController (Presentation layer)
Deze klasse vormt de verbinding tussen de xhtml pagina's en de tasks. De klasse doet dit door de methoden van de TaskEJB klasse verder uit te breiden en te koppelen aan de xhtml pagina.

### 4 Opmerkingen tov de koppeling van de layers
Ondanks dat we alles van elkaar hebben gescheiden in het boek, is er toch veel overlap tussen de verschillende layers. Het is daarom misschien beter dan niet iedereen met een bepaalde layer bezig is, maar dat iedereen per feature werkt.
