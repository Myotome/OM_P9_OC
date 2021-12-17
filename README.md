# Real Estate Manager
---------------------
Projet 9 OpenClassrooms

> Développé par Olivier Marais

### Application de gestion de bien immobilier de Luxe
-----------------------------------------------------

## Technologie
---------------
* Kotlin 94 %
* Java 6 %
* XML

### Compilation et dépendances
* Gradle 7.0.4
* Kotlin 1.5.31
* Hilt 2.40
* Room 2.4.0-rc01
* Retrofit 2.9.0
* Coil 1.4.0
* Service Google
  - Location 
  - Maps
  - Firebase
  - Storage
 
 * Tests par JUnit 4 et Robolectric 4.7.3

# Adapté aux smartphones ou tablettes
-------------------------------------
## Liste des biens 
![2021-12-17_10h24_20](https://user-images.githubusercontent.com/78822313/146521276-dade182d-a622-46fc-9309-8ec1fe43d152.png)
![2021-12-17_10h24_28](https://user-images.githubusercontent.com/78822313/146521291-68762e51-e6c5-4c1f-adb0-e1c5fe4ddf7c.png) 

## Affichage du détail d'un bien
Retrouvez :
* Le type de bien
* Le prix du bien
* La surface du bien
* Le nombre de pièces
* La description du bien
* Les photos et leurs légendes
* L'adresse du bien
* Les points d'intérêts à proximité
* Le statut de vente du bien
* Les informations d'entrée et/ou de vente du bien
* L'agent immobilier en charge du bien  

### Vue Smartphone
* Page dynamique défilante
* Faîtes défiler les photos
* Choisissez d'afficher ou non les informations en cliquant sur les boutons  

![2021-12-17_10h25_12](https://user-images.githubusercontent.com/78822313/146521407-2838511d-1565-432d-ae22-bd8fbe688f46.png)
![2021-12-17_10h25_21](https://user-images.githubusercontent.com/78822313/146521439-a66e25b7-386a-46c8-a3bb-9a04dfadf329.png)
![2021-12-17_10h25_34](https://user-images.githubusercontent.com/78822313/146521473-1a691779-a7db-464a-844a-647140098ac7.png)

### Vue Tablette
* Page dynamique défilante
* Faîtes défiler les photos  

![2021-12-17_10h26_07](https://user-images.githubusercontent.com/78822313/146521552-b5db4182-b4c7-4f2c-ab22-f86e8553512f.png)
![2021-12-17_10h31_30](https://user-images.githubusercontent.com/78822313/146522374-1cc63756-5aad-476f-979c-253266f70b74.png)

## Retrouvez les biens sur la carte
* Autour de vous
* Cliquez sur la punaise pour voir le détail du bien

![2021-12-17_10h35_08](https://user-images.githubusercontent.com/78822313/146522941-d5f024bb-21a2-4ced-9366-1cebd85f434c.png)
![2021-12-17_10h41_05](https://user-images.githubusercontent.com/78822313/146523887-956e7cd8-b44a-44b4-a246-3a34caab8dcb.png)

## Créez de nouveaux biens
* Remplissez les champs 
* Choisissez des photos depuis la gallerie ou prenez en de nouvelles
* Votre bien est sauvegardé localement ET en ligne si vous êtes connecté à Internet  

![2021-12-17_10h56_32](https://user-images.githubusercontent.com/78822313/146526134-4bb9ffc3-b9cd-42bf-937b-6851a2f2dd3c.png)
![2021-12-17_10h57_22](https://user-images.githubusercontent.com/78822313/146526241-e71d33ec-c2dc-4f13-92c6-152cdc0e4461.png)

## Editez un bien
* Les champs sont pré remplis 
* Votre bien est mis à jour localement ET en ligne si vous êtes connecté à Internet

![2021-12-17_10h59_34](https://user-images.githubusercontent.com/78822313/146526606-3ef491e6-0b2d-4464-8b79-9b89b43c4560.png)
![2021-12-17_10h59_47](https://user-images.githubusercontent.com/78822313/146526629-47c18b28-26d6-4417-b8dd-e7e08695dc5d.png)

## Soyez notifié lorsque vous créez ou modifier un bien

![2021-12-17_11h04_37](https://user-images.githubusercontent.com/78822313/146527286-0e2dbddd-bda6-4202-bf33-e594d25725bc.png)

## Cherchez un ou plusieurs bien
* Un sytème de recherche complet pour tous vos désir de filtre
* Une interface adaptive, n'affichez que les champs qui vous intéresses
* Le tri se fait aussi bien sur la liste que sur la carte

![2021-12-17_11h01_52](https://user-images.githubusercontent.com/78822313/146526927-24083299-4654-4b3f-b4ca-456fef335a78.png)
![2021-12-17_11h02_22](https://user-images.githubusercontent.com/78822313/146526987-07ed820d-48a9-4f40-b5f7-31f857385d1b.png)
![2021-12-17_11h02_30](https://user-images.githubusercontent.com/78822313/146527002-567a65a3-a2b2-4348-95bd-7c2343f25270.png)

## Votre base de donnée est automatiquement synchronisée en ligne
* Aucune action n'est requise de votre part
* La synchronisation est en temps réelle lorsque vous êtes connecté à internet
* Vous êtes hors ligne ? 
  - Pas de soucis, continuer de travailler grace à votre base de donnée locale
  - La mise à jour sera automatique lorsque vous rejoindrez le réseau Internet

