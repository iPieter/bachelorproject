# Bachelorproject: Televic
Deze repository bevat de code voor het bachelorproject.

## Deel Anton
Ik heb de volgende onderdelen gemaakt:
  - De basis van traincoach.xhtml : maken van nieuwe Issues & tonen van alle data. Later hebben Matthias en Pieter dit dan uitgebreid
  - De geschiedenis bekijken van wagons
  - De problemen pagina waarbij Issue's gesloten worden, commentaar wordt gegeven & foto's getoond worden
  - De live tracking van treinen ( layout later aangepast door Pieter )
  - De MatLabProcessor die grote .mat bestanden omzet naar json bestanden die dan gebruikt kunnen worden in bvb de grafieken/kaart.
  - De Constraint Engine backend: een modulair systeem waarbij de gebruiker beperkingen kan opletten bij een rit. bvb:
    - Op deze locatie, voor dit model van trein mag de roll maximaal 3 zijn en de snelheid maximaal 6 m/s
  - REST interface voor alle objecten


### Extra commentaar
Ik ben zeer tevreden over de medewerking in groep. Iedereen communiceerde goed & gaf goede feedback qua code. We hebben regelmatig code herschreven toen we vonden dat het beter kon. Iedereen heeft er ongeveer even veel tijd in gestoken.
    
 ## Deel Matthias
  -Index Pagina:
    - Welcome tabs: dynamisch opvragen overzich van issues op status
    - Heatmap: (REST, Back-end, javascript) Geeft overzicht weer waar de activiteit van de issues zich bevindt
    - Highcharts donutgraph: (REST, Back-end, javascript) Geef een overzich van de gebruikersactiviteit de voorbije 30 dagen, ook wordt message weergegeven wanneer er nog geen activiteit van de gebruiker geregistreerd is in de voorbije 30 dagen.
  -Dynamische Sidebar: Op indexpagina opvragen van alle workplaces, op andere paginas opvragen van alle traincoaches in die workplace
  - Layout templating: Template gebruikt voor alle pagina's gerelateerd aan een workplace/traincoach
 -Workplace pagina: 
   -Map: Geeft overzicht van alle issues met bijhorende description
   -Overzich wagons
   -Overzicht technici
 -Traincoach pagina: Javascript gedeelte waarbij geselecteerde punt in de grafiek wordt doorgegeven aan de modal voor een nieuwe herstelling
 -Layout fixing, afgewerkt door Pieter
  
 ## Deel Pieter
Ik heb de volgende onderdelen gemaakt:
  - Initieel ontwerp van database
  - Authorisatie: Controleren of een gebruiker de juiste rol (```ADMIN```, ```MECHANIC```, ```OPERATOR```) heeft voor een pagina.
  - Layout constraint engine
  - Gebruikersmanagement: 
    - Aanmaken van gebruikers, toekennen van een rol. 
    - Mechanics toekennen aan stelplaatsen.
    - Wachtwoord en profielafbeelding veranderen.
  - Rest services voor profielafbeeldingen.
  - Layout afgewerkt
    - Live tracking
    - index
    - workplace
    - traincoach
    - Sidebar afgewerkt
  - Navigatiebalk op elke pagina toegevoegd.
  - Marker Traincoach voor kaart
  
  ### Extra commentaar
  Ik ben ook zeer tevreden over de samenwerking. Ik kon mijn taken afwerken en erop vertrouwen dat andere gedeeltes ook vorderden. Door goed te communiceren waren we ook op de hoogte van elkaars voortgang. 
  
  
