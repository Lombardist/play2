package security

import jp.t2v.lab.play2.auth.AuthConfig
import models.users.{Account, Role}
import play.api.mvc.{Result, RequestHeader}
import play.api.mvc.Results._

import scala.concurrent.{Future, ExecutionContext}
import scala.reflect.ClassTag

/**
 * Created by root on 11/1/15.
 */
trait AuthConfigImpl extends  AuthConfig {
  override type Authority = Role
  override type User = Account
  override type Id = String

  override def resolveUser(id: Id)(implicit context: ExecutionContext): Future[Option[User]] = Account.findById(id)

  override def sessionTimeoutInSeconds: Int = 300

  override def authorize(user: User, authority: Authority)(implicit context: ExecutionContext): Future[Boolean] = Account.authorize(user)

  override def logoutSucceeded(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = Future(Redirect("/"))

  override def authenticationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = ???

  override implicit def idTag: ClassTag[Id] = ???

  override def loginSucceeded(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = ???

  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit context: ExecutionContext): Future[Result] = ???

}
