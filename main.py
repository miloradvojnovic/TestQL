from textx import metamodel_from_file

metamodel = metamodel_from_file('metamodel/graphQL.tx')

findOneUser = metamodel.model_from_file('metamodel/findOneUser.test')
createNewUser = metamodel.model_from_file('metamodel/createNewUser.test')
findAllUsers = metamodel.model_from_file('metamodel/findAllUsers.test')