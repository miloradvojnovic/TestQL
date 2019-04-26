package graphql

import graphql.data.Users._
import graphql.data.Categories._
import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType


package object data {
  implicit val UserType: ObjectType[Ctx, User] =
    deriveObjectType[Ctx, User]()
  implicit val CategoryType: ObjectType[Ctx, Category] =
    deriveObjectType[Ctx, Category]()
}
