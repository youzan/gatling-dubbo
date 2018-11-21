package io.gatling

import io.gatling.commons.validation.Success
import io.gatling.core.check.{ Check, Extender, Preparer }

package object dubbo {

  /**
   * Type for Dubbo checks
   */
  type DubboCheck = Check[String]

  val DubboStringExtender: Extender[DubboCheck, String] =
    (check: DubboCheck) => check

  val DubboStringPreparer: Preparer[String, String] =
    (result: String) => Success(result)

}
