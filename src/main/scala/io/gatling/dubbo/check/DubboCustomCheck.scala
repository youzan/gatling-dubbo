package io.gatling.dubbo.check

import io.gatling.commons.validation._
import io.gatling.core.check.CheckResult
import io.gatling.core.session.Session
import io.gatling.dubbo.DubboCheck

import scala.collection.mutable

case class DubboCustomCheck(func: String => Boolean, failureMessage: String = "Dubbo check failed") extends DubboCheck {
  override def check(response: String, session: Session)(implicit cache: mutable.Map[Any, Any]): Validation[CheckResult] = {
    func(response) match {
      case true => CheckResult.NoopCheckResultSuccess
      case _    => Failure(failureMessage)
    }
  }
}
