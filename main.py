from textx import metamodel_from_file
import os
from os import listdir
import click

from generation.generators.main_generator import MainGenerator
from generation.generators.test_spec_generator import TestSpecGenerator
from generation.generators.semantics import *
from test_case_matcher import TestCaseMatcher



def get_model(path):
    matcher = TestCaseMatcher()
    metamodel = metamodel_from_file('metamodel/graphql.tx')
    obj_processors = {
        'Request': request_obj_processor, 
        'Response': response_obj_processor, 
        'Examples': examples_obj_processor
        }
    metamodel.register_obj_processors(obj_processors)
    metamodel.register_model_processor(check_some_semantics)
    return matcher.match(path, metamodel)

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

@click.command()
@click.option('--model', default='./example/tests', help='Path to the model directory.')
@click.option('--output', default='./example/output', help='Path to the output directory.')
def run(model, output):
    delete_files(output + '/test')
    main_generator = MainGenerator(output)
    test_spec_generator = TestSpecGenerator(main_generator)
    main_generator.add_generator(test_spec_generator)
    main_generator.generate_all(get_model(model))



if __name__ == '__main__':
    run()
