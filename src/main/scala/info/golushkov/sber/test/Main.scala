package info.golushkov.sber.test

import java.net.InetSocketAddress
import java.util.concurrent.{Executors, ThreadFactory}

import com.sun.net.httpserver.HttpServer

import scala.concurrent.ExecutionContext

/**
 *
 * @author Arseniy Golushkov <arsen.gl@yandex.ru> 
 *         26.02.16.
 */
object Main extends App {

  val threadPool = Executors.newFixedThreadPool(8, new ThreadFactory {
    override def newThread(r: Runnable): Thread = {
      val t = new Thread(r, "http-handle-thread")
      t.setDaemon(true)
      t
    }
  })

  implicit val ec = new ExecutionContext {

    override def reportFailure(cause: Throwable): Unit = {}

    override def execute(task: Runnable): Unit = {
      threadPool.submit(task)
    }
  }

  val server = HttpServer.create(new InetSocketAddress(8089), 0)
  server.createContext("/calc", CalcHttpHandler())

  server.setExecutor(threadPool)
  server.start()
}