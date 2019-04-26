package graphql.data

import graphql.Ctx
import play.api.libs.json.{Json, _}
import sangria.macros.derive.{GraphQLDescription, deriveInputObjectType}
import sangria.marshalling.playJson._
import sangria.schema._
import graphql.ResponseTransformer._

import scala.concurrent.ExecutionContext

object Users {

  @GraphQLDescription("User of the application.")
  final case class User(@GraphQLDescription("Unique identifier of the user.")
                        id: Long,
                        @GraphQLDescription("Username of the user.")
                        username: String,
                        @GraphQLDescription("First name of the user.")
                        firstName: String,
                        @GraphQLDescription("Last name of the user.")
                        lastName: String,
                        @GraphQLDescription("Address of the user.")
                        address: String,
                        @GraphQLDescription("Phone of the user.")
                        phone: String,
                        @GraphQLDescription("Email of the user.")
                        email: String,
                        @GraphQLDescription("Money of the user.")
                        money: Double)

  @GraphQLDescription(
    "Input object for specifying user properties on the creation.")
  final case class CreateUserInput(@GraphQLDescription("Username of the user.")
                                   username: String,
                                   @GraphQLDescription("Password of the user.")
                                   password: String,
                                   @GraphQLDescription(
                                     "First name of the user.")
                                   firstName: String,
                                   @GraphQLDescription("Last name of the user.")
                                   lastName: String,
                                   @GraphQLDescription("Address of the user.")
                                   address: String,
                                   @GraphQLDescription("Phone of the user.")
                                   phone: String,
                                   @GraphQLDescription("Email of the user.")
                                   email: String,
                                   @GraphQLDescription("Money of the user.")
                                   money: Double)

  implicit val createUserInputType: InputObjectType[CreateUserInput] =
    deriveInputObjectType[CreateUserInput]()

  implicit val CreateUserInputFormat: OFormat[CreateUserInput] =
    Json.format[CreateUserInput]

  val CreateUserInputArgument: Argument[CreateUserInput] =
    Argument("input", createUserInputType)

  val id: Argument[Long] =
    Argument("id", LongType)

  val users: Field[Ctx, Unit] = Field(
    "users",
    ListType(UserType),
    Some("Get all users"),
    resolve = ctx => ctx.ctx.findAllUsers()
  )

  val user: Field[Ctx, Unit] = Field(
    "user",
    OptionType(UserType),
    Some("Get one user"),
    arguments = id :: Nil,
    resolve = ctx => ctx.ctx.findOneUser(ctx.arg(id))
  )

  def createUser(implicit ec: ExecutionContext): Field[Ctx, Unit] = Field(
    "createUser",
    UserType,
    Some("Operation for creating new users."),
    arguments = CreateUserInputArgument :: Nil,
    resolve = ctx => ctx.ctx.saveUser(ctx.arg(CreateUserInputArgument)).withConversion
  )
}
