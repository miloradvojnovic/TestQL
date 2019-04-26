import net.codingwell.scalaguice.ScalaModule
import database._
import service._
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

class Module extends ScalaModule {


  private val dbConfig: DatabaseConfig[PostgresProfile] =
    DatabaseConfig.forConfig("slick.dbs.default")
  private val profile = dbConfig.profile


  override def configure(): Unit = {
    bind[DatabaseConfig[PostgresProfile]].toInstance(dbConfig)
    bind[PostgresProfile].toInstance(profile)

    bind[CategoryRepository].asEagerSingleton()
    bind[OrderItemRepository].asEagerSingleton()
    bind[OrderRepository].asEagerSingleton()
    bind[ProductRepository].asEagerSingleton()
    bind[ShoppingCartRepository].asEagerSingleton()
    bind[UserRepository].asEagerSingleton()

    bind[CategoryService].asEagerSingleton()
    bind[OrderItemService].asEagerSingleton()
    bind[OrderService].asEagerSingleton()
    bind[ProductService].asEagerSingleton()
    bind[ShoppingCartService].asEagerSingleton()
    bind[UserService].asEagerSingleton()
  }
}
