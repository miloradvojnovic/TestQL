def convert_tag_values(model, metamodel):
    for case in model.cases:
        for example in case.examples.examples:
            attributes = []
            should_index = -1
            for idx, attr in enumerate(case.examples.attributes.attributes):
                attributes.append(attr.name)
                if attr.name == 'should':
                    should_index = idx

            response = case.response
            request = model.request
            attr_len = len(attributes)
            vals_len = len(example.values)
            for i in range(vals_len):
                value = example.values[i]
                if i is should_index:
                    example.should = value
                value = str(value)
                request = request.replace("<"+attributes[i%attr_len]+">", value, 1)
                response = response.replace("<"+attributes[i%attr_len]+">", value, 1)
            example.request_generated = request.replace('\r', '').replace('\n', '\n' +20*' ')
            example.response_generated = response.replace('\r', '').replace('\n', '\n' +20*' ')
            