Keter: General concepts
=======================

# Objects
Everything that is not part of the landscape -- meaning, everything that is not a floor, a wall or somesuch -- is an object. Any and all objects can carry and possess effects that describe how they can be interacted with and how they interact with the environment. The objects are broadly divided into two categories:

## Passive
Passive objects are anything that can be nontrivially interacted with mechanics-wise, like a door, a lever, a container or an item lying on the ground. Passive objects are not processed by event queue and can not perform actions but can be targeted by actions. A special case of passive objects is **items** that can be picked up, held in an inventory, equipped,  and dropped.

## Active
Active objects are driven by either a player or an AI. They can perform actions and can be a target of actions performed by other active objects. Active-ness can be imparted onto a passive object by some weird SCP effects. A special case of active objects are **sentient** objects. If an object is sentient it means that it possesses some semblance of thought and is able to *somehow* perceive, process and react, even if it's form permits no valid actions. Therefore, only **sentient** objects can be afflicted by memetic effects.

# Actions

All active objects place their actions onto the common queue. If all active objects have an action queued, then the world simulation runs till the next queued action and said action resolves. Otherwise, all objects with no action in queue are queried for their move.

Example actions:

* Move: move one square in any direction (or wait on the same place) -- 1 second

* Attack (with any readied weapon): intent to cause harm to the defending party -- duration is defined by the weapon of choice

* Use item (like a medkit): activate one (or all) of consciously activable effects of an item, target(s) suffer the consequences -- duration is defined by an item

* Use ability: activate one (or all) effects of a skill, intrinsic ability or supernatural phenomena that this active objects possess, with target(s) suffering the consequences -- defined, obviously, by the ability 

# Stats
All objects possess statistics that, in a nutshell, are some numbers that describe their quantitative properties. They have no real meaning by themselves, but will be used in the formulas describing how the certain actions resolve. 

## Common stats
* Health points -- or HP for short. When HP reaches zero, the object is destroyed/killed

## Living objects
* Strength (Str) -- brute force. brawn. muscle power. Obviously.
* Dexterity (Dex) -- agility, precision, speed, you know the drill.
* IQ -- smarts, intelligence and wit.

