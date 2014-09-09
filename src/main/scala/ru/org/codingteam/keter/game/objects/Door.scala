package ru.org.codingteam.keter.game.objects

case class Door(id: ActorId,
                name: String,
                tile: String,
                passable: Boolean,
                open: Boolean,
                openTile: String,
                closedTile :String) extends GameObject
