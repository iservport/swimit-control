package com.iservport.swimit.akka

import java.util.concurrent.TimeUnit

import com.datastax.driver.core.{BoundStatement, ResultSet, ResultSetFuture}

import scala.concurrent.duration.Duration
import scala.concurrent.{CanAwait, ExecutionContext, Future}
import scala.util.{Success, Try}

/**
 * Trait para melhorar operações com resultados com Cassandra.
 *
 * Originalmente publicado em https://github.com/eigengo/activator-akka-cassandra
 */
private[akka] trait CassandraResultSetOperations {

  private case class ExecutionContextExecutor(executionContext: ExecutionContext) extends java.util.concurrent.Executor {
    def execute(command: Runnable): Unit = { executionContext.execute(command) }
  }

  /**
   * Conversão implícita.
   *
   * @param resultSetFuture
   */
  protected class RichResultSetFuture(resultSetFuture: ResultSetFuture) extends Future[ResultSet] {
    @throws(classOf[InterruptedException])
    @throws(classOf[scala.concurrent.TimeoutException])
    def ready(atMost: Duration)(implicit permit: CanAwait): this.type = {
      resultSetFuture.get(atMost.toMillis, TimeUnit.MILLISECONDS)
      this
    }

    @throws(classOf[Exception])
    def result(atMost: Duration)(implicit permit: CanAwait): ResultSet = {
      resultSetFuture.get(atMost.toMillis, TimeUnit.MILLISECONDS)
    }

    def onComplete[U](func: (Try[ResultSet]) => U)(implicit executionContext: ExecutionContext): Unit = {
      if (resultSetFuture.isDone) {
        func(Success(resultSetFuture.getUninterruptibly))
      } else {
        resultSetFuture.addListener(new Runnable {
          def run() {
            func(Try(resultSetFuture.get()))
          }
        }, ExecutionContextExecutor(executionContext))
      }
    }

    def isCompleted: Boolean = resultSetFuture.isDone

    def value: Option[Try[ResultSet]] = if (resultSetFuture.isDone) Some(Try(resultSetFuture.get())) else None
  }

  implicit def toFuture(resultSetFuture: ResultSetFuture): Future[ResultSet] = new RichResultSetFuture(resultSetFuture)

}

trait Binder[-A] {

  def bind(value: A, boundStatement: BoundStatement): Unit

}

/**
 * Trait para melhorar ligações com Cassandra.
 *
 * Originalmente publicado em https://github.com/eigengo/activator-akka-cassandra
 */
trait BoundStatementOperations {

  implicit class RichBoundStatement[A : Binder](boundStatement: BoundStatement) {
    val binder = implicitly[Binder[A]]

    def bindFrom(value: A): BoundStatement = {
      binder.bind(value, boundStatement)
      boundStatement
    }
  }

}

/**
 * Expõe as melhorias.
 */
object cassandra {

  object resultset extends CassandraResultSetOperations

  object boundstatement extends BoundStatementOperations

}