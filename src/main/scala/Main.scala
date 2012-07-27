import scalaz._
import Scalaz._

// type class
trait Show[M]{
  def show:String
}

object Show{
  // instance for Show for Int
  implicit def showInt(i:Int) = new Show[Int]{
    def show = String.valueOf(i)
  }

  // isntance for Show for List, now List is Show
  implicit def showList[T](i:List[T]) = new Show[List[T]]{
    def show = "{" + (if (i.isEmpty) "}" else (i.head + i.tail.foldLeft(""){(a,b)=>a+","+b} + "}"))
  }
}

object OurSemigroups{
  
  class MultMonoid extends Monoid[Int]{
    val zero = 1

    def append(s1: Int, s2: => Int) = if (s1==0) 0 else s1 * s2

  }
  
  implicit object multMonoid extends MultMonoid
  
  implicit object ptSemigroup extends Semigroup[Point]{
    def append(s1: Point, s2: => Point) = Point(s1.x+s2.x,s1.y+s2.y)
  }
}

case class Point(x:Int, y:Int)

object Main{
  import Show._
  import OurSemigroups._

  def combine[A](a:A, b: =>A)(implicit s: Semigroup[A])={
    s.append(a,b)
  }
  
	def main2(argv:Array[String]){
		println(10.show)
    println(List[Int]().show)
    println(List("test","hello"))
    println(List("dsas","sad").asMA.sum)
    
    val k:Validation[Point, Int] = Point(1,2).fail
    val b:Validation[Point, String] = Point(10,20).fail
    
    val m = k <|*|> b
    
    println(m)
    
    def calc:Int = {
      Thread.sleep(10000)
      10
    }

    def st1 = state[List[Int], Int]{
      i=>(i.sum::i, i.sum)
    }
    
    def inState = for{
      v<-state[List[Int],Int]{
        i => (i.sum::i, i.sum)
      }
      k<-state[List[Int],Int]{
        i => (i.sum::i, i.sum)
      }
    } yield k

    println(inState ! List(10))
    
    println(0 ⊹ calc)
	}
}
