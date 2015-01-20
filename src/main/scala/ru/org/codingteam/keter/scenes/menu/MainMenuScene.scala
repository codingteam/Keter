package ru.org.codingteam.keter.scenes.menu

import ru.org.codingteam.keter.ui.ViewScene
import ru.org.codingteam.rotjs.interface.Display

class MainMenuScene(display: Display) extends ViewScene(display) {

  override val components = Vector(menu(new MainMenuViewModel(this)))

}
