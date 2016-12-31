Combat mechanics
================

## Terminology
* EStr -- character's effective strength after all modifiers are taken into account
* EDex -- character's effective dexterity after all modifiers are taken into account
* Acc -- weapon's accuracy rating
* AV -- attack value
* WD -- weapon damage
* DV -- damage value
* DR -- defence rating
* d(X) -- a random value between 1 and X, inclusive

## Attack
Attack action is resolved in two steps:
1. Determine if the attack connected:
* Attacker rolls d(EDex) as his attack score.
* Defender rolls d(EDex) as his evade score.
* Subtract defender's evade score from attacker's attack score to get attack value. AV of 0 or more is a hit.

2. Determine the damage:
* For melee attacks the DV is WD + d(EStr) + AV
* For ranged attacks the DV is simply WD + AV
This damage can be fully or partially negated by the defender's soak value that is determined as d(EStr) + DR

## Modifiers

### Range
When making a ranged attack, attacker's Dex value is modified by -1 for every full (Acc) tiles between attacker and defender. The tiles that attacker and defender stand on are not counted.

## Effects

### Bleeding (X seconds)
Living objects only. Prevents natural HP regeneration for X seconds.

### Bleedout (X seconds)
Living objects only. Object is bleeding heavily and will die after X seconds. If any healing is received before the bleedout ends, this effect is replaced with Bleeding for the remaining duration.

### Stunned (X seconds)
Any active object. Lasts for X seconds. A stunned object has all his attribute values halved (rounding up). Any queued action is cancelled when object becomes Stunned. Any actions queued by object while Stunned take twice as much time
