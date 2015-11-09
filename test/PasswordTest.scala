import helpers.Password
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
  * Created by root on 11/8/15.
  */
@RunWith(classOf[JUnitRunner])
class PasswordTest extends Specification {

  "MD5" should {
    "Generate hash" in {
      val hash1 = Password.md5("text")
      val hash2 = Password.md5("text")
      println(hash1)
      hash1.length > 0
      hash2.length > 0
    }
  }

}
