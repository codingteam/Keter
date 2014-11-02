package ru.org.codingteam.keter

package object util {

  def castToOption[T: Manifest](v: Any): Option[T] = v match {
    case r: T => Some(r)
    case _ => None
  }
}
