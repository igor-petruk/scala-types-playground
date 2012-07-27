
object Utils{
  class λAB[A,B](f:A=>B){
    def ∘[C](other: B=>C): A=>C = {ξ=>other(f(ξ))}
  }
  
  implicit def toλAB[A,B](f:A=>B) = new λAB(f)
}

trait Monad[M[_]]{
  import Utils._

  def unit[Q](v: Q):M[Q]
  def map[A,B](f:A=>B):M[B]=flatMap(f ∘ unit[B])
  def flatMap[A,B](f:A=>M[B]):M[B]
}

class CustomMonad[T](value: LoggingConfig[T]) extends Monad[LoggingConfig]{
  type Me[_] = CustomMonad[T]
  type MonadConst = LoggingConfig[_]

 // def apply():T = value.data

  def unit[Q<:LoggingConfig[Q]](v: Q):CustomMonad[Q] = new CustomMonad(v)

  def flatMap[Q >: T, B](f: (Q) => LoggingConfig[B]) = f(value.data)

  override def toString = "Custom %s monad".format(value)
}

abstract class LoggingConfig[T](v:T){
  val data = v
}
case class Loggable[T](value:T, format:String) extends LoggingConfig[T](value)
case class NotLoggable[T](value:T) extends LoggingConfig[T](value)

object MonadMain {
  class LoggableW[T](v:T){
    def ~(format:String):CustomMonad[T]  = new CustomMonad(Loggable(v,format))
  }
  implicit def toLgg[T](value: T):LoggableW[T] = new LoggableW[T](value)
  implicit def toNonLgg[T](value: T):CustomMonad[T] = new CustomMonad(new NotLoggable[T](value))

  def main(argv: Array[String]) {
    val c = for{
      _ <- "Welcome" ~ "%s"
      j <- 2 ~ "Here %s ololo"
    } yield j

    println(c)
  }
}
