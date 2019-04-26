package graphql

import graphql.data.Users.CreateUserInput
import model._
import graphql.data.Categories.CreateCategoryInput

object InputConverter {

  def userInputToDomain(input: CreateUserInput): User =
    User(
      id = None,
      username = input.username,
      password = input.password,
      firstName = input.firstName,
      lastName = input.lastName,
      address = input.address,
      phone = input.phone,
      email = input.email,
      money = input.money
    )

  def categoryInputToDomain(input: CreateCategoryInput): Category =
    Category(
      id = None,
      name = input.name,
      superCategoryId = input.superCategoryId
    )
}
