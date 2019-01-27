package {{ package }}

import graphql.manual.DataSpec
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import play.api.libs.json._
import sangria.macros._


class {{ class_name }} extends WordSpec with DataSpec {
    "{{ class_name }}" when {
      {% for model in models %}
        "{{ model.scenario.name.strip() }}" should {
          {% for case in model.cases %}
            {% for example in case.examples.examples %}
              {% set ex_loop = loop %}
              {% for att in case.examples.attributes.attributes -%}
                {% if att.name == 'should' %}
                  {% set should_index = loop.index %}
                {% endif %}
                {% if should_index is defined %}
                  {% for value in example.values %}
                    {% if loop.index == should_index %}
            "{{ value }}"
                    {%- endif -%}
                  {%- endfor -%}
                {%- endif -%}
              {%- endfor %} in {
              {% set ns = namespace(compound=False, indentation=28, multipliers = [], begin_brace = '{', end_brace = '}', arr_begin_brace = '[', arr_end_brace = ']', quotation_mark = '"') %}
                val query =
                    graphql"""
                       {{model.request.type }} {{ model.request.name }} {
                          {% for attribute in model.request.attributes recursive %}
                            {%- if attribute.arguments|length > 0 %}
{{ attribute.name|indent(ns.indentation, True) }}(
                            {%- elif attribute.attributes|length > 0 %}
{{ attribute.name|indent(ns.indentation, True) -}}
                            {%- else %}
{{ attribute.name|indent(ns.indentation, True) }}
                            {% endif %}
                            {% for argument in attribute.arguments recursive -%}
                              {% set arg_loop = loop %}
                              {% if 'SimpleValue' in argument.value.__class__.__name__ -%}
{{- argument.name }}: {{ argument.value -}}{% if ns.compound == True and not arg_loop.last %}, {% endif %} 
                              {%- elif 'TagValue' in argument.value.__class__.__name__ -%}
                                {% for ex_attribute in case.examples.attributes.attributes %}
                                  {% set att_loop = loop %}
                                  {% for value in example.values %}
                                    {% if loop.index ==  att_loop.index %}
                                      {% if ex_attribute.name == argument.value.name %}
                                        {% if 'str' in value.__class__.__name__ -%}
{{- argument.name }}: "{{ value -}}"{% if ns.compound == True and not arg_loop.last %}, {% endif %} 
                                        {%- else -%}
{{- argument.name }}: {{ value -}}{% if ns.compound == True and not arg_loop.last %}, {% endif %} 
                                        {%- endif %}
                                      {% endif %}
                                    {% endif %}
                                  {% endfor %}
                                {% endfor %}
                              {%- else-%}
{{- argument.name }}: { {% set ns.compound = true %} {{- loop(argument.value.arguments) -}} {% set ns.compound = false %} }
                              {%- endif -%}
                            {%- endfor %}
                            {% if attribute.arguments|length > 0 %}
)
                            {%- endif -%}
                            {%- if attribute.attributes is defined and attribute.attributes|length > 0 -%} 
{
{% set ns.indentation = ns.indentation + 4 %}
{{ loop(attribute.attributes) }}
{%- set ns.indentation = ns.indentation - 4 -%}
{{ns.end_brace|indent(ns.indentation, True)}}
                            {% endif %}
                          {% endfor %}
                        }
                  """
                  {% set ns.indentation=26 %}
                    executeQuery(query) should be(
                    Json.parse(
                    """
                      {
                            {% for member in case.response.json.members recursive -%}
                              {% set member_loop = loop %}
                              {%- if 'JsonObject' in member.value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": {
{% set ns.indentation = ns.indentation + 4 %}
{{ loop(member.value.members) -}}
{% set ns.indentation = ns.indentation - 4 %}
{{ns.end_brace|indent(ns.indentation, True)}}{% if not member_loop.last %}, {% endif %}
                              {%- elif 'JsonArray' in member.value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": [
                                  {% if member.value.multiplier is not none %}
                                    {% if 'TagValue' in member.value.multiplier.value.__class__.__name__ %}
                                      {% for ex_attribute in case.examples.attributes.attributes %}
                                        {% set att_loop = loop %}
                                        {% for value in example.values %}
                                          {% if loop.index == att_loop.index %}
                                            {% if ex_attribute.name == member.value.multiplier.value.name %}
                                              {% set ns.temp = value %}
                                            {% endif %}
                                          {% endif %}
                                        {% endfor %}
                                      {% endfor %}
                                      {% set multiplier_counter = ns.temp %}
                                    {%- else -%} 
                                      {% set multiplier_counter = member.value.multiplier.value %}
                                    {% endif %}
                                  {% else %}
                                    {% set multiplier_counter = 1 %}
                                  {% endif %}
                                  {% if ns.multipliers.append(multiplier_counter) %}{% endif %}
                                  {% for mult in range(1, multiplier_counter + 1) %}
                                    {% set mult_loop = loop %}
                                    {% if ns.multipliers.pop() and ns.multipliers.append(mult) %}{% endif %}
                                    {% for value in member.value.values %}
                                      {%- if 'JsonObject' in value.__class__.__name__ -%}
{% set ns.indentation = ns.indentation + 4 %}
{{ns.begin_brace|indent(ns.indentation, True)}}
{% set ns.indentation = ns.indentation + 4 %}
{{ member_loop(value.members) -}}
{% set ns.indentation = ns.indentation - 4 %}
{{ns.end_brace|indent(ns.indentation, True)}}{% if not mult_loop.last %}, {% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                      {%- elif 'JsonArray' in value.__class__.__name__ %}
{% set ns.indentation = ns.indentation + 4 %}
{{ns.arr_begin_brace|indent(ns.indentation, True)}}
{% set ns.indentation = ns.indentation + 4 %}
loop(value.values)
{% set ns.indentation = ns.indentation - 4 %}
{{ns.arr_end_brace|indent(ns.indentation, True)}}{% if not loop.last %}, {% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                      {%- elif 'JsonNull' in value.__class__.__name__ %}
{% set ns.indentation = ns.indentation + 4 %}
{% filter indent(ns.indentation, True) %}null{% endfilter %}{% if not loop.last %},{% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                      {%- elif 'str' in member.value.__class__.__name__ %}
{% set ns.indentation = ns.indentation + 4 %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ value -}}"{% if not loop.last %}, {% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                      {%- else %}
{% set ns.indentation = ns.indentation + 4 %}
{{ value|indent(ns.indentation, True) -}}{% if not loop.last %},{% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                      {%- endif %} 
                                    {% endfor %}
                                  {% endfor %}
{{ns.arr_end_brace|indent(ns.indentation, True)}}{% if ns.multipliers.pop() and not member_loop.last %},{% endif %}
                              {%- elif 'JsonNull' in member.value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": null{% if not member_loop.last %}, {% endif %}
                              {%- elif 'str' in member.value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": "{{ member.value -}}"{% if not member_loop.last %}, {% endif %}
                              {%- elif 'TagValue' in member.value.__class__.__name__ %}
                                {% for ex_attribute in case.examples.attributes.attributes %}
                                  {% set att_loop = loop %}
                                  {% for value in example.values %}
                                    {% if (ns.multipliers|length>0 and loop.index == (case.examples.attributes.attributes|length * (ns.multipliers|last - 1) + att_loop.index)) or (ns.multipliers|length == 0 and (loop.index == (ex_loop.index * att_loop.index))) %}
                                      {% if ex_attribute.name == member.value.name %}
                                        {%- if 'JsonObject' in value.__class__.__name__ -%}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": {
{% set ns.indentation = ns.indentation + 4 %}
{{ loop(value.members) -}}
{% set ns.indentation = ns.indentation - 4 %}
{{ns.end_brace|indent(ns.indentation, True)}}{% if not member_loop.last %}, {% endif %}
                                        {%- elif 'JsonArray' in value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": [
                                          {% for value in value.values %}
                                            {%- if 'JsonObject' in value.__class__.__name__ -%}
{% set ns.indentation = ns.indentation + 4 %}
{{ns.begin_brace|indent(ns.indentation, True)}}
{% set ns.indentation = ns.indentation + 4 %}
{{ member_loop(value.members) -}}
{% set ns.indentation = ns.indentation - 4 %}
{{ns.end_brace|indent(ns.indentation, True)}}{% if not loop.last %}, {% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                            {%- elif 'JsonArray' in value.__class__.__name__ %}
{% set ns.indentation = ns.indentation + 4 %}
{{ns.arr_begin_brace|indent(ns.indentation, True)}}
{% set ns.indentation = ns.indentation + 4 %}
loop(value.values)
{% set ns.indentation = ns.indentation - 4 %}
{{ns.arr_begin_brace|indent(ns.indentation, True)}}{% if not loop.last %}, {% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                            {%- elif 'JsonNull' in value.__class__.__name__ %}
{% filter indent(ns.indentation, True) %}null{% endfilter %}{% if not loop.last %},{% endif %}
                                            {%- elif 'str' in value.__class__.__name__ %}
{% set ns.indentation = ns.indentation + 4 %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ value -}}"{% if not loop.last %},{% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                            {%- else %}
{% set ns.indentation = ns.indentation + 4 %}
{{ value|indent(ns.indentation, True) -}}{% if not loop.last %},{% endif %}
{% set ns.indentation = ns.indentation - 4 %}
                                            {% endif %} 
                                          {% endfor %}
{{ns.arr_end_brace|indent(ns.indentation, True)}}{% if not member_loop.last %},{% endif %}
                                        {%- elif 'JsonNull' in value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key -}}": null{% if not member_loop.last %}, {% endif %}
                                        {%- elif 'str' in value.__class__.__name__ %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": "{{ value -}}"{% if not member_loop.last %}, {% endif %}
                                        {%- else %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": {{ value -}}{% if not member_loop.last %}, {% endif %}
                                        {%- endif -%} 
                                      {%- endif -%}
                                    {%- endif -%}
                                  {% endfor %}
                                {% endfor %}
                              {%- else %}
{{ns.quotation_mark|indent(ns.indentation, True)}}{{ member.key }}": {{ member.value -}}{% if not member_loop.last %}, {% endif %}
                              {% endif %} 
                            {% endfor %}
                      }
                    """
                    ))
                    }
                  {% endfor %}
            {% endfor %}
       }
      {% endfor %}
    }
}