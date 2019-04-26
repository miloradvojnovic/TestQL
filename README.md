# TestQL

TestQL is a generator for GraphQL tests. It is inspired by [Karate](https://github.com/intuit/karate). The biggest difference between Karate is that TestQL generates tests for a specific platform. For test developers, Karate is a better choice because it's independent of a platform but for software developers that want to have unit or integration tests in some specific programming language TestQL is the right choice. Currently there's generator only for [Scala](https://www.scala-lang.org/) - [Play Framework](https://www.playframework.com/). [TextX](https://github.com/textX/textX) is used for building Domain-Specific Language (DSL) that is used for TestQL.

## DSL

For defining test cases for TestQL there's DSL that is created. There are some examples [here](https://github.com/miloradvojnovic/TestQL/tree/master/example/tests). As an example, we could use a test case where we want to test getting categories from API.

```
package:graphql
class:CategoriesSpec
scenario:retrieving information about all categories

request:
query findAllCategories {
    categories {
        id
        name
    }
}

response:
{
    "data": {
        "categories": [
            [2]{
                "id": <id>,
                "name": <name>
            }
        ]
    }
}

examples:
| should                    | id  | name         |
| ------------------------- | --- | ------------ |
| "retrieve all categories" | 1   | "RAM memory" |
| *                         | 2   | "SSD disk"   |
```

Every test should have:
- package name (optional)
- class name
- a scenario that is tested
- GraphQL request
- On or more test cases

Every test case should have:
- response
- examples for response

TestQL will generate a test for every example that is defined in a table. If the test contains two responses and for every response five examples than ten test cases will be generated. That is 
possible with tags. Tag is used for some variable that will be injected with every example. This is the most powerful feature of TestQL because one same request or response could be used in every test case. Since TextX has support for semantic error detection there's a couple that is defined here:
- The same tag shouldn't be used in a request more than once
- The same tag shouldn't be used in response more than once
- The first tag in every example should be `should`
- The same tag shouldn't be used in examples more than once
- Every tag that is used in a request and response should be used in examples 
- Every tag that is used in examples should be used in a request or response

## IDE

As an IDE for writing tests for TestQL currently the best option is a [Visual Studio Code](https://code.visualstudio.com/).VS Code has plugins that could help. Since all tests are in Markdown format there's a plenty of plugins that could help in code coloring, formatting, etc. Also, there's a [GraphQL plugin](https://marketplace.visualstudio.com/items?itemName=kumar-harsh.graphql-for-vscode) that could help in code completion when GraphQL request should be defined. [Example](https://github.com/miloradvojnovic/TestQL/tree/master/example) has a complete setup for that plugin. If that folder is opened with VS Code and if a plugin is installed, code completion should be possible. The last plugin that is used is [Text Tables](https://marketplace.visualstudio.com/items?itemName=RomanPeshkov.vscode-text-tables). Text Tables is used for writing tables in Markdown format very easy. With that plugin in the VS Code table that is used for defining examples is formatted very easy.

## Running

For running a generator on example that is provided:
```
python main.py
cd /example/output
sbt test
```

Generated Scala classes will be generated in `/example/output`.

For running specific tests and on specific output directory there are two arguments:
- `--model` for defining a directory where tests files are
- `--output` for defining a directory where Scala Play Framework project is

```
python main.py --model model/directory --output output/directory
```

For defining a test environment that will be used for testing there's `DataSpec` class. Class should have the method:
```scala
def executeQuery(query: Document): JsValue
```

In an example you can see how is implemented. For example mocking is used for testing but that's one of the options that can be used.
