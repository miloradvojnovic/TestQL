package {{ package }}

import graphql.manual.DataSpec
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import play.api.libs.json._
import sangria.macros._


class {{ class_name }} extends WordSpec with DataSpec {
  "{{ class_name -}}" when {
      {% for model in models %}
      "{{ model.scenario.name.strip() }}" should {
          {% for case in model.cases %}
            {% for example in case.examples.examples %}
            {{ example.should }} in {
                  val query =
                  graphql"""
                        {{- example.request_generated -}}
                  """
                    executeQuery(query) should be(
                    Json.parse(
                    """
                        {{- example.response_generated -}}
                    """
                    ))
                    }
                  {% endfor %}
            {% endfor %}
       }
      {% endfor %}
    }
}