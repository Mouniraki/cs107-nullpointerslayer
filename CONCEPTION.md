# **VUE D'ENSEMBLE DES ELEMENTS CREES DANS LE CADRE DU PROJET**

### Package game.areagame.actor :
- Classe abstraite CollectableAreaEntity -> modélise un type d'objets
pouvant être repris dans d'autres jeux de type RPG (objets collectables)

### Package ARPG :
- Classe ARPGBehavior + Classe imbriquée ARPGCell -> spécifique au jeu ARPG.

### Package ARPG.Actor
- Classe ARPGInventory -> définition spécifique de l'inventaire pour le jeu ARPG.
- Enum ARPGItem -> définition spécifique des items de l'inventaire pour le jeu ARPG.
- Classe ARPGPlayer -> définition spécifique de Player pour le jeu ARPG.
- Classe ARPGStatusGUI -> définition spécifique d'une interface graphique liée à un Player de type ARPGPlayer.
- Classe Arrow -> définition d'acteur spécifique au jeu ARPG.
- Classe CastleKey -> définition d'acteur spécifique au jeu ARPG.
- Classe Coin -> définition d'acteur spécifique au jeu ARPG.
- Classe Heart -> définition d'acteur spécifique au jeu ARPG.
- Classe Bomb -> définition d'acteur spécifique au jeu ARPG.
- Classe CastleDoor -> définition d'acteur spécifique au jeu ARPG.
- Classe Grass -> définition d'acteur spécifique au jeu ARPG.
- Classe MagicWaterProjectile -> définition d'acteur spécifique au jeu ARPG.
- Classe abstraite Projectile -> définition d'un type d'acteurs spécifiques au jeu ARPG.
- Classe Orb -> définition d'un élément spécifique au jeu ARPG.
- Classe Bridge -> définition d'un élément spécifique au jeu ARPG.
- Classe Waterfall -> définition d'un élément spécifique au jeu ARPG.
- Classe abstraite Monster -> définition d'un type d'acteurs spécifiques au jeu ARPG
- Classe FlameSkull -> définition d'un monstre spécifique au jeu ARPG
- Classe LogMonster -> définition d'un monstre spécifique au jeu ARPG
- Classe DarkLord -> définition d'un monstre spécifique au jeu ARPG
- Interface FlyableEntity -> définition spécifique de Interactable pour le jeu ARPG

### Package ARPG.Area:
- Classe abstraite ARPGArea -> définition spécifique de Area pour le jeu ARPG.
- Classe Chateau -> Aire spécifique au jeu ARPG.
- Classe Ferme -> Aire spécifique au jeu ARPG.
- Classe Route -> Aire spécifique au jeu ARPG.
- Classe RouteChateau -> Aire spécifique au jeu ARPG.
- Classe Temple -> Aire spécifique au jeu ARPG.
- Classe RouteTemple -> Aire spécifique au jeu ARPG.
- Classe Village -> Aire spécifique au jeu ARPG.

### Package ARPG.Handler :
- Interface ARPGInteractionVisitor-> définition spécifique pour les interactions entre les
éléments utilisés pour le jeu ARPG.

### Package RPG :
- Classe Inventory + Holder -> modélise un concept d'inventaire utilisable dans n'importe quel jeu de type RPG.
- Interface InventoryItem -> modélise un concept d'élément d'inventaire utilisable dans n'importe quel jeu de type RPG.


## **MODIFICATIONS APPORTEES A L'ARCHITECTURE PROPOSEE**
Aucune modification n'a été apportée à la maquette.

## **DECISIONS DE NE PAS ADHERER AUX SUGGESTIONS DE L'ENONCE**
- Nous avons décidé de modifier l'interaction entre les flèches et les touffes d'herbes
pour rendre les flèches plus puissantes, car nous avons trouvé plus adéquat de permettre
aux flèches de pouvoir couper plusieurs touffes d'herbes en même temps sans être arrêtés dans leur course.
- Egalement, nous avons décidé de permettre au joueur d'utiliser le pouvoir magique de manière illimitée,
car nous estimons que les pouvoirs magiques ne se comportent pas comme les flèches et ne sont donc pas stockés en
quantité limitée.

## **EXTENSIONS**
- Mise en place d'un système de points d'immunité lorsque le Player ou les monstres sont affectés par une baisse de
points de vie (compteur décrémentant jusqu'à une certaine borne, avant d'être réinitialisé à 0).
- Mise en place d'acteurs de décors animés (chutes d'eau).
- Ajout des aires RouteTemple et Temple, ainsi qu'un bâton magique pouvant être récupéré dans l'aire Temple pour affronter le seigneur des ténèbres.
