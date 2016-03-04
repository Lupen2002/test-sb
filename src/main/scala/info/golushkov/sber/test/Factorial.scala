package info.golushkov.sber.test

import scala.annotation.tailrec

/**
 *
 * @author Arseniy Golushkov <arsen.gl@yandex.ru> 
 *         27.02.16.
 */
object Factorial {
  private class FactorialException(msg: String) extends Exception(msg)

  implicit class BigIntFactorial(i: BigInt) {
    def ! = fac(i)
  }

  implicit class IntFactorial(i: Int) {
    def ! = fac(i)
  }

  @tailrec
  private def fac(n: BigInt, a: BigInt = 1): BigInt = {
    if (n > 10000)
      throw new FactorialException("Ошибка!!! число не должно превышать 10000")
    if (n < 0)
      throw new FactorialException("Ошибка!!! факториал может быть вычеслен только от числа больше 0")
    if (n > 0) fac(n-1, a*n) else a
  }

4}
