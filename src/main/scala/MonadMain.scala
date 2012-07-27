package monads

object Utils{
  implicit def toλAB[A,B](f:A=>B) = new {
    def ∘[C](other: B=>C): A=>C = {ξ=>other(f(ξ))}
  }
}

trait Monad[M[_]]{
  def unit[Q](v: Q):M[Q]
  def bind[A,B](m:M[A])(f:A=>M[B]):M[B]
}

object Monad{
  import Utils._

  implicit def monadExtra[M[_],A](m:M[A])(implicit monad: Monad[M])=new{
    def flatMap[B](f:A=>M[B]):M[B]=monad.bind(m)(f)
    def map[B](f:A=>B) = monad.bind(m)(f ∘ monad.unit)
  }

  implicit object loggingMonad extends Monad[LoggingConfig]{
    def unit[Q](v: Q):LoggingConfig[Q] = NotLoggable(v)

    def bind[A, B](m: LoggingConfig[A])(f: (A) => LoggingConfig[B]) = {
      m match {
        case Loggable(v,fmt) => println("LOG: " + v.formatted(fmt))
        case _=>
      }
      f(m.data)
    }
  }
}

class LoggingConfig[T](v:T){
  val data = v
}
case class Loggable[T](value:T, format:String) extends LoggingConfig[T](value)
case class NotLoggable[T](value:T) extends LoggingConfig[T](value)

object MonadMain {
  import Monad._

  class LoggableW[T](v:T){
    def ~(format:String)(implicit m:Monad[LoggingConfig]):LoggingConfig[T] = Loggable(v,format)
    def ~(implicit m:Monad[LoggingConfig]):LoggingConfig[T] = NotLoggable(v)
  }
  implicit def toLgg[T](value: T):LoggableW[T] = new LoggableW[T](value)

  def main(argv: Array[String]) {
    val c = for{
      _ <- 1 ~;
      j <- 2 ~ "Here %s ololo"
    } yield j

    println(c.data)
  }
}
