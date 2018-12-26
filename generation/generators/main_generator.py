from jinja2.environment import Environment
from jinja2.loaders import PackageLoader

from root import root
import os


class MainGenerator(object):
    def __init__(self, path, generators=[]):
        self.path = path
        self.generators = generators

    def generate(self, template_name, directory, output_name, render_vars):
        env = Environment(trim_blocks=True, lstrip_blocks=True, loader=PackageLoader("generation", "templates"))
        template = env.get_template(template_name)
        rendered = template.render(render_vars)
        path = os.path.join(self.path, directory)
        if not os.path.exists(path):
            os.makedirs(path)
        file_name = os.path.join(path, output_name)
        with open(file_name, "w+") as f:
            f.write(rendered)

    def generate_all(self, model):
        for generator in self.generators:
            generator.generate(model)

    def add_generator(self, generator):
        self.generators.append(generator)
