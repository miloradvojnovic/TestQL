import os

from generation.generators.generator import Generator


class GraphQLSpecGenerator(Generator):

    def __init__(self, main_generator):
        self.main_generator = main_generator

    def generate(self, model):
        self.generate_model(model)

    def generate_model(self, models):
        self.main_generator.generate("GraphQLSpec.scala",
                                         os.path.join("test.graphql".replace(".", os.sep)),
                                         "GraphQLSpec.scala", {"package": "graphql"})
