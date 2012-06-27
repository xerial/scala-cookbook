package utgenome.sample

import org.scalatest.{WordSpec, Tag}
import org.scalatest.matchers.ShouldMatchers

trait TestSpec extends WordSpec with ShouldMatchers 

class HelloTest extends TestSpec {
	"Hello should run main" taggedAs(Tag("main")) in {
	       Hello.main(Array.empty)
	}
}
