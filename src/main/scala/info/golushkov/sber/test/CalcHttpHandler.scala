package info.golushkov.sber.test

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

/**
 *
 * @author Arseniy Golushkov <arsen.gl@yandex.ru> 
 *         26.02.16.
 */
class CalcHttpHandler(implicit val ec: ExecutionContext) extends HttpHandler {
  private val nPattern = """.*n=(-?\d+).*""".r
  private class CalcHttpHandlerException(msg: String) extends Exception(msg)

  @tailrec
  private def fac(n: BigInt, a: BigInt = 1): BigInt = {
    if (n > 10000)
      throw new CalcHttpHandlerException("Ошибка!!! число не должно превышать 10000")
    if (n < 0)
      throw new CalcHttpHandlerException("Ошибка!!! факториал может быть вычеслен только от числа больше 0")
    if (n > 0) fac(n-1, a*n) else a
  }


  private def getN(q: String) = {
    q match {
      case nPattern(n) => BigInt(n)
      case a: Any => throw new CalcHttpHandlerException("Ошибка!!! Не указан параметр N")
    }
  }

  private def sendMsgToClient(code: Int, msg: String, httpExchange: HttpExchange) = {
    val b = msg.getBytes
    httpExchange.sendResponseHeaders(code, b.length)
    val out = httpExchange.getResponseBody
    out.write(b)
    out.flush()
    out.close()
  }

  override def handle(httpExchange: HttpExchange): Unit = {
    val n = getN(httpExchange.getRequestURI.getQuery)
    Future {
      sendMsgToClient(200, fac(n).toString(), httpExchange)
    } onFailure {
      case e: CalcHttpHandlerException =>
        sendMsgToClient(400, e.getMessage, httpExchange)

      case NonFatal(e) =>
        sendMsgToClient(500, e.getMessage, httpExchange)
    }
  }
}
object CalcHttpHandler {
  def apply()(implicit ec: ExecutionContext) = new CalcHttpHandler
}