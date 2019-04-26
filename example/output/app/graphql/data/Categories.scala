package graphql.data

import graphql.Ctx
import play.api.libs.json._
import sangria.macros.derive.{GraphQLDescription, deriveInputObjectType}
import sangria.marshalling.playJson._
import sangria.schema._
import graphql.ResponseTransformer._

import scala.concurrent.ExecutionContext

object Categories {

  @GraphQLDescription("Category of the product.")
  final case class Category(@GraphQLDescription("Unique identifier of the category.")
                        id: Long,
                        @GraphQLDescription("Name of the category.")
                        name: String)

  @GraphQLDescription(
    "Input object for creating category.")
  final case class CreateCategoryInput(@GraphQLDescription("Name of the category.")
                                       name: String,
                                   @GraphQLDescription("Supercategory identifier.")
                                   superCategoryId: Option[Long])

  implicit val createCategoryInputType: InputObjectType[CreateCategoryInput] =
    deriveInputObjectType[CreateCategoryInput]()

  implicit val CreateCategoryInputFormat: OFormat[CreateCategoryInput] =
    Json.format[CreateCategoryInput]

  val CreateCategoryInputArgument: Argument[CreateCategoryInput] =
    Argument("input", createCategoryInputType)

  val name: Argument[String]=
    Argument("name", StringType)

  val id: Argument[Long] =
    Argument("id", LongType)

  val categories: Field[Ctx, Unit] = Field(
    "categories",
    ListType(CategoryType),
    Some("Get all categories"),
    resolve = ctx => ctx.ctx.findAllCategories()
  )

  val category: Field[Ctx, Unit] = Field(
    "category",
    OptionType(CategoryType),
    Some("Get one category"),
    arguments = id :: Nil,
    resolve = ctx => ctx.ctx.findOneCategory(ctx.arg(id))
  )

  def createCategory(implicit ec: ExecutionContext): Field[Ctx, Unit] = Field(
    "createCategory",
    CategoryType,
    Some("Operation for creating new categories."),
    arguments = CreateCategoryInputArgument :: Nil,
    resolve = ctx => ctx.ctx.saveCategory(ctx.arg(CreateCategoryInputArgument)).withConversion
  )

  def searchCategories(implicit ec: ExecutionContext): Field[Ctx, Unit] = Field(
    "searchCategories",
    ListType(CategoryType),
    Some("Get all categories by search parameter."),
    arguments = name :: Nil,
    resolve = ctx => ctx.ctx.searchCategories(ctx.arg(name)).withConversion
  )
}

