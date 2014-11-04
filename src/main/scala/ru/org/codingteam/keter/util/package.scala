package ru.org.codingteam.keter

import scala.reflect.ClassTag

package object util {

  def castToOption[T: ClassTag](v: Any): Option[T] = v match {
    case r: T => Some(r)
    case _ => None
  }
}
