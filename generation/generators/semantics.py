from textx import TextXSemanticError

def request_obj_processor(request, return_value=False):
    tag_values = set()
    for att in request.attributes:
        for arg in att.arguments:
            check_request_tags(tag_values, arg)
    if return_value:
        return tag_values

def check_request_tags(tag_values, arg):
    if 'TagValue' in arg.value.__class__.__name__:
        value = arg.value.name
        if value in tag_values:
            raise TextXSemanticError('Same Tag name (%s) for GraphQL arguments is not allowed.' % value)
        else:
            tag_values.add(value)
    elif 'CompoundValue' in arg.value.__class__.__name__:
        for argument in arg.value.arguments:
            check_request_tags(tag_values, argument)

def response_obj_processor(response, return_value=False):
    tag_values = set()
    for member in response.json.members:
        check_response_tags(tag_values, member.value)
    if return_value:
        return tag_values

def check_response_tags(tag_values, memb_value):
    if 'TagValue' in memb_value.__class__.__name__:
        value = memb_value.name
        if value in tag_values:
            raise TextXSemanticError('Same Tag name (%s) for response is not allowed.' % value)
        else:
            tag_values.add(value)
    elif 'JsonObject' in memb_value.__class__.__name__:
        for member in memb_value.members:
            check_response_tags(tag_values, member.value)
    elif 'JsonArray' in memb_value.__class__.__name__:
        mult = memb_value.multiplier
        if mult is not None:
            if 'TagValue' in mult.value.__class__.__name__:
                value = mult.value.name
                if value in tag_values:
                    raise TextXSemanticError('Same Tag name (%s) for response is not allowed.' % value)
                else:
                    tag_values.add(value)
        for value in memb_value.values:
            check_response_tags(tag_values, value)    

def examples_obj_processor(examples, return_value=False): 
    values = set()
    if examples.attributes.attributes[0].name != 'should':
        raise TextXSemanticError('First attribute must be should.')
    for att in examples.attributes.attributes:
        name = att.name
        if name in values:
            raise TextXSemanticError('Same Attribute name (%s) for examples is not allowed.' % name)
        else:
            values.add(name)
    if return_value:
        return values

def check_some_semantics(model, metamodel):
    tag_values = request_obj_processor(model.request, True)
    example_values = set()
    for case in model.cases:
        tag_values = tag_values.union(response_obj_processor(case.response, True))
        example_values = example_values.union(examples_obj_processor(case.examples, True))
        example_values.remove('should')

    for tag_val in tag_values:
        if tag_val not in example_values:
            raise TextXSemanticError('Tag value (%s) is not present in examples.' % tag_val)
    for ex_val in example_values:
        if ex_val not in tag_values:
            raise TextXSemanticError('Tag value (%s) is not present in request nor responses.' % ex_val)
