package info.golushkov.sber.test

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.control.NonFatal

/**
 *
 * @author Arseniy Golushkov <arsen.gl@yandex.ru> 
 *         26.02.16.
 */
class CalcHttpHandler(implicit val ec: ExecutionContext) extends HttpHandler {

  private def sendMsgToClient(code: Int, msg: String, httpExchange: HttpExchange) = {
    println(s"response\t[$code] $msg")
    val b = msg.getBytes
    httpExchange.sendResponseHeaders(code, b.length)
    val out = httpExchange.getResponseBody
    out.write(b)
    out.flush()
    out.close()
  }

  override def handle(httpExchange: HttpExchange): Unit = {
    import CalcHttpHandler._
    import info.golushkov.sber.test.Factorial._
    implicit def bigInt2String(i: BigInt): String = i.toString()

    println(s"request\t[${httpExchange.getRequestMethod}]" +
      s" ${httpExchange.getRequestURI.toString}")
    val n = httpExchange.getRequestURI.getQuery.n
    Future {
      sendMsgToClient(200, n!, httpExchange)
    } onFailure {
      case e: CalcHttpHandlerException =>
        sendMsgToClient(400, e.getMessage, httpExchange)

      case NonFatal(e) =>
        sendMsgToClient(500, e.getMessage, httpExchange)
    }
  }
}

object CalcHttpHandler {
  private val nPattern = """.*n=(-?\d+).*""".r

  def apply()(implicit ec: ExecutionContext) = new CalcHttpHandler

  def extractN(q: String) = {
    q match {
      case nPattern(n) => BigInt(n)
      case a: Any => throw new CalcHttpHandlerException("Ошибка!!! Не указан параметр N")
    }
  }
  
  implicit class ExtractionNFromString(s: String) {
    def n = extractN(s)
  }
}

class CalcHttpHandlerException(msg: String) extends Exception(msg)
