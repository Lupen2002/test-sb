package info.golushkov.sber.test

import org.scalatest.{FlatSpec, Matchers}

/**
 *
 * @author Arseniy Golushkov <arsen.gl@yandex.ru> 
 *         27.02.16.
 */
class CalcHttpHandlerTest extends FlatSpec with Matchers {
  import CalcHttpHandler._
  "CalcHttpHandler" should "извлеч 4 из строки 'n=4'" in {
    "n=4".n shouldBe 4
  }

  it should "извлеч 1000 из строки 'qweewqrsafn=1000asdfsdvcasdv'" in {
    "qweewqrsafn=1000asdfsdvcasdv".n shouldBe 1000
  }
}
