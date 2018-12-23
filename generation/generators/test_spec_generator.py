import os

from generation.generators.generator import Generator


class TestSpecGenerator(Generator):

    def __init__(self, main_generator):
        self.main_generator = main_generator

    def generate(self, model):
        self.generate_model(model)

    def generate_model(self, models):
        for k, v in models.items():
            package = ""
            class_name = v[0].class_name.name
            if v[0].package is not None:
                package = v[0].package.name
            self.main_generator.generate("TestSpec.scala",
                                         os.path.join("test", package.replace(".", os.sep)),
                                         class_name + ".scala",
                                     {"package": package, "class_name": class_name, "models": v})
