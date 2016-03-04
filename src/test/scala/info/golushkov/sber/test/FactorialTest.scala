package info.golushkov.sber.test

import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

/**
 *
 * @author Arseniy Golushkov <arsen.gl@yandex.ru> 
 *         27.02.16.
 */
class FactorialTest extends FlatSpec with Matchers {
  import Factorial._

  "Factorial 3" should "равен 6" in {
    (3!) shouldBe 6
  }

}
