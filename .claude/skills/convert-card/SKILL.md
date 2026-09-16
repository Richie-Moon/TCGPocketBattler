---
name: convert-card
description: Fetch a card using TCGdexAPI.jar, then convert it into our object-oriented model
---

To fetch a card, run the jar file:

```powershell
java -jar src/main/resources/TCGdexAPI.jar <setID> <cardID>
```
Replace <setID> and <cardID> with the set and card ID you want to fetch. 

That API will return a JSON object in the format of:

```json
{
  "id": "A1-001",
  "name": "Bulbasaur",
  "description": "There is a plant seed on its back right from the day this Pokémon is born. The seed slowly grows larger.",
  "hp": 70,
  "type": [
    "GRASS"
  ],
  "stage": "BASIC",
  "evolvesFrom": null,
  "moves": [
    {
      "name": "Vine Whip",
      "description": null,
      "damage": "40",
      "energyCost": {
        "GRASS": 1,
        "COLORLESS": 1
      }
    }
  ],
  "rarity": "COMMON",
  "weakness": "FIRE",
  "retreatCost": 1,
  "ex": false,
  "megaEx": false
}
```
Then, take this JSON data and write Java code to convert it into the object-oriented model for this project.
Sometimes, the damage field will have extra characters other than numbers. Ignore the extra characters and use the number as the base damage and the description to figure out how to model.
Sometimes, move descriptions will include references to energy as `{G}` or `{W}`. Replace these with full type names, e.g. `Grass` or `Water`. 
For the example above:

```java
public static final PokemonCard BULBASAUR = PokemonCard.basic(
                "A1-001", "Bulbasaur", "There is a plant seed on its back right from the day this Pokémon is born. The seed slowly grows larger.",
                70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1),
                List.of(new Action(
                        "Vine Whip",
                        "",
                        EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1),
                        new Attempt(List.of(
                                new DealDamage(new Literal(40), new OpponentActive()))))), CardRarity.COMMON)
        .withWeakness(Type.FIRE);
```

In the event of an API failure, report the error to the user and do not write any Java code. 
