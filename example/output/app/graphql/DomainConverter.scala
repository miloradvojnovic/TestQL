package graphql

import graphql.data.Users._
import graphql.data.Categories._
import model.{User => DomainUser, Category => DomainCategory}

object DomainConverter {

  def convertUserFromDomain(user: DomainUser): User =
    User(
      user.id.get,
      user.username,
      user.firstName,
      user.lastName,
      user.address,
      user.phone,
      user.email,
      user.money
    )

  def convertCategoryFromDomain(category: DomainCategory): Category =
    Category(
      category.id.get,
      category.name
    )

}

