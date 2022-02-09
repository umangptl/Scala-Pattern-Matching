import scala.io.StdIn.readLine

/*
parse grammar
  S  -: E$
  E  -: T E2
  E2 -: '|' E3
  E2 -: NIL
  E3 -: T E2
  T  -: F T2
  T2 -: F T2
  T2 -: NIL
  F  -: A F2
  F2 -: '?' F2
  F2 -: NIL
  A  -: Const
  A  -: '(' A2
  A2 -: E ')'
 */

abstract class S
case class E(left: T, right: Option[E2]) extends S    // '|' or
case class E2(left: E3) extends S
case class E3(left: T, right: Option[E2]) extends S
case class T(left: F, right: Option[T2]) extends S    //'&' and
case class T2(left: F, right: Option[T2]) extends S
case class F(left: A, right: Option[F2]) extends S    //'?' optional
case class F2(left: Option[F2])extends S
abstract class A extends S
case class A2(left: E)extends A
case class Const(left:Char)extends A                  // '.'dot for any char

class project_scala(input:String) {
  var patternString: String = input
  var index = 0

  def parseS(): S = parseE()

  def parseE(): E = E(parseT(), parseE2())

  def parseE2(): Option[E2] = {
     if(index < input.length() && input(index) == '|') {
      index += 1
      Some(E2(parseE3()))
    } else if(index < input.length() && input(index) == ')'){
      index+=1
      None
    }else None
  }

  def parseE3(): E3 = E3(parseT(), parseE2())

  def parseT(): T = T(parseF(), parseT2())

  def parseT2(): Option[T2] = {
    if (index < input.length() && input(index) != ')'
      && input(index) != '|' && input(index) != '?') {
      Some(T2(parseF(), parseT2()))
    } else None
  }

  def parseF(): F = F(parseA(), parseF2())

  def parseF2(): Option[F2] = {
    if (index < input.length() && input(index) == '?') {
      index += 1
      Some(F2(parseF2()))
    } else None
  }

  def parseA(): A = {
    if (input(index) == '(') {
      index += 1
      parseA2()
    } else {
      index += 1
      Const(patternString.charAt(index - 1))
    }
  }

  def parseA2(): A2 = A2(parseE())

}

object Main {
  def main(args: Array[String])= {
    val inputPattern: String = readLine ("pattern? ")     //  i (like|love|hate)( (cat|dog))? people
    val parsePattern = new project_scala( inputPattern)   // ((h|j)ell. worl?d)|42
    val pattern = parsePattern.parseS()
    println(pattern)

    var stringMatch = readLine ("String? ")
    while (stringMatch != "quit"){
      val isMatch: Boolean = Matches(pattern, stringMatch)
      if(isMatch){
        println("Match")
      }else println("No Match")
      stringMatch = readLine ("String? ")
    }
  }

  def Matches(s: S, str: String): Boolean ={
    var index = 0

    def recursiveMatch(s: Any): Boolean = s match{

      case s:E =>
        val flip = index
        val leftbool: Boolean = recursiveMatch(s.left)
        s.right match{
          case Some(next)=>
          if(!leftbool){
            index= flip
            recursiveMatch(next)
          }else true
          case None =>leftbool
        }

      case s: E2 => recursiveMatch(s.left)

      case s: E3 =>
        val flip = index
        val leftbool: Boolean = recursiveMatch(s.left)
        s.right match {
          case Some(next)=>
            if(!leftbool){
              index= flip
              recursiveMatch(next)
            } else true
          case None => leftbool
        }

      case s:T =>
        s.right match{
          case Some(next) =>
            val flip = index
            if(s.left.right.nonEmpty && recursiveMatch(next)) {
              true
            } else{
              index = flip
              recursiveMatch(s.left) && recursiveMatch(next)
            }
          case None => recursiveMatch(s.left)
        }

      case s:T2 =>
        s.right match {
          case Some(next)=>
            val flip = index
            if(s.left.right.nonEmpty && recursiveMatch(next)) {
              true
            } else{
              index = flip
              recursiveMatch(s.left) && recursiveMatch(next)
            }
          case None => recursiveMatch(s.left)
        }

      case s: F =>
        s.right match {
          case Some(next) =>
            val flip = index
            if(!recursiveMatch(s.left))
              index = flip
              true
          case None => recursiveMatch(s.left)
        }

      case s:F2 => true

      case s:A2 => recursiveMatch(s.left)

      case s:Const =>
      if(index >= str.length){
        //print("too small string")
        false
      } else
        if (s.left == '.') {
        index += 1
        true
      }else if (s.left == str.charAt(index)){
        index+=1
        true
      } else false
    }

    val pass = recursiveMatch(s)
    if(index < str.length) {
      false
    } else {
      pass
    }

  }
}