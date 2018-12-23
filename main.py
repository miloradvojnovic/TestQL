from textx import metamodel_from_file
import os
from os import listdir

from generation.generators.main_generator import MainGenerator
from generation.generators.test_spec_generator import TestSpecGenerator
from generation.generators.graphql_spec_generator import GraphQLSpecGenerator
from preprocessor.model_preprocessor import convert_tag_values

metamodel = metamodel_from_file('metamodel/graphQL.tx', use_regexp_group = True)
metamodel.register_model_processor(convert_tag_values)
models = {}

for file in listdir('metamodel'):
    package = None
    if file.endswith('.test'):
        model = metamodel.model_from_file('metamodel/' + file)
        if model.package is not None:
            package = model.package.name
        if package is None:
            full_name = model.class_name.name
        else:
            full_name = package + '.' + model.class_name.name
        if full_name not in models:
            models[full_name] = [model]
        else:
            models[full_name].append(model)

def delete_files(path):
    try:
        for (dirpath, dirnames, filenames) in os.walk(path):
            for directory in dirnames:
                path = os.path.join(dirpath, directory)
                delete_files(path)
            for filename in filenames:
                if 'manual' not in dirpath:
                    os.remove(os.path.join(dirpath, filename))
    except Exception:
        pass


if __name__ == '__main__':
    path = './output'
    delete_files(path)
    main_generator = MainGenerator()
    graphql_spec_generator = GraphQLSpecGenerator(main_generator)
    test_spec_generator = TestSpecGenerator(main_generator)
    main_generator.add_generator(test_spec_generator)
    main_generator.add_generator(graphql_spec_generator)
    main_generator.generate_all(models)
