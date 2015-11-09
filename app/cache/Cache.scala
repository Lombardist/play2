package cache

import java.util.concurrent.TimeUnit

import com.hazelcast.config.Config
import com.hazelcast.core.{Hazelcast, HazelcastInstance}
import models.entities.Company
import models.entities.Company.CompanyDAO
import models.user.User
import models.user.User.UserDAO
import play.api.{Application, GlobalSettings}

object CacheGlobal {

  private val cfg = new Config
  private val instance = Hazelcast.newHazelcastInstance(cfg)

  val companies = instance.getMap[Any, Any]("companies")

  val users = instance.getMap[Any, Any]("users")


}
