package io.gatling.dubbo.action

import java.util.concurrent.ExecutorService

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.commons.stats.Status
import io.gatling.commons.validation.Failure
import io.gatling.core.CoreComponents
import io.gatling.core.action._
import io.gatling.core.check.Check
import io.gatling.core.session.{ Expression, Session }
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen
import io.gatling.dubbo.DubboCheck

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Success, Failure => UFailure }

class DubboAction[A](
                      requestName:      Expression[String],
                      f:                (Session) => A,
                      val executor:     ExecutorService,
                      val objectMapper: ObjectMapper,
                      checks:           List[DubboCheck],
                      coreComponents:   CoreComponents,
                      throttled:        Boolean,
                      val next:         Action
                    ) extends ExitableAction with NameGen {

  implicit val ec = ExecutionContext.fromExecutor(executor)

  override def statsEngine: StatsEngine = coreComponents.statsEngine

  override def name: String = genName("dubboRequest")

  override def execute(session: Session): Unit = recover(session) {
    requestName(session) map { reqName =>
      val startTime = System.currentTimeMillis()
      val fu = Future {
        try {
          f(session)
        } finally {
        }
      }

      fu.onComplete {
        case Success(result) =>
          val endTime = System.currentTimeMillis()
          val resultJson = objectMapper.writeValueAsString(result)
          val (newSession, error) = Check.check(resultJson, session, checks)
          error match {
            case None =>
              statsEngine.logResponse(session, reqName, ResponseTimings(startTime, endTime), Status("OK"), None, None)
              throttle(newSession(session))
            case Some(Failure(errorMessage)) =>
              statsEngine.logResponse(session, reqName, ResponseTimings(startTime, endTime), Status("KO"), None, Some(errorMessage))
              throttle(newSession(session).markAsFailed)
          }

        case UFailure(e) =>
          val endTime = System.currentTimeMillis()
          statsEngine.logResponse(session, reqName, ResponseTimings(startTime, endTime), Status("KO"), None, Some(e.getMessage))
          throttle(session.markAsFailed)
      }
    }
  }

  private def throttle(s: Session): Unit = {
    if (throttled) {
      coreComponents.throttler.throttle(s.scenario, () => next ! s)
    } else {
      next ! s
    }
  }
}
