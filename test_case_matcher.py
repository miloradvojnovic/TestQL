import os

class TestCaseMatcher(object):
    def match(self, path, metamodel):
        models={}
        for r, d, f in os.walk(path):
            for file in f:
                package = None
                if file.endswith('.test'):
                    model = metamodel.model_from_file(os.path.join(r, file))
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
        return models
