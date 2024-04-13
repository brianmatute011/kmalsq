<div align="center">
    <img src="./.resources/smalllogo.jpg" alt="KmalSQ" width="200" height="200">
</div>
<div align="center">

<div>

[//]: # (    <img src="./.resources/antlrv4.svg">)
<img alt="antlr badge" src="https://img.shields.io/badge/antlr-v4-red"> <img alt="version badge" src="https://img.shields.io/badge/version-1.0-blue"> <img alt="test badge" src="https://img.shields.io/badge/tests-passing-green"> <img alt="release badge" src="https://img.shields.io/badge/release-red">

[//]: # (    <img src="./.resources/version.svg">)


[//]: # (<img src="./.resources/testsb.svg">)


[//]: # (<img src="./.resources/release.svg">)


</div>

[//]: # (![antlr-version]&#40;/.resources/antlrv4.svg&#41; ![dsl-version]&#40;./.resources/version.svg&#41; ![info-tests]&#40;./.resources/testsb.svg&#41; ![release]&#40;.resources/release.svg&#41;)

</div>

# KmalSQ v1
### Welcome to KmalSQ v1
KmalSQ is a Generic DSL that allows the generation of queries to multiple search and database engines
[(See more about the KmalSQ kernel)](./kmalsq-dsl/README.md). KmalSQ is part of a research in the field of domain-specific language design (DSL) and its application in the field of information search in search engines and databases. We are open to collaborations and suggestions.



## Available Integrations
-[x] Elasticsearch


# Integration with ElascticSearch
### Using KmalSQ with ElasticSearch
KmalSQ allows the generation of queries for ElasticSearch, below are examples of how KmalSQ can be used to generate queries for ElasticSearch.
Currently support for an executor and a query builder is provided to each integrated engine.
#### From the query builder

```java
package mlix.global;
import mlix.global.build.ElasticSearchKmalBuildQuery;
import org.elasticsearch.index.query.QueryBuilder;

public class App {
    public static void main( String[] args ) {
        ElasticSearchKmalBuildQuery elasticSearchKmalBuildQuery = new ElasticSearchKmalBuildQuery();
        String dslPrompt = "NOT [asin.keyword] == '1234567890';";
        QueryBuilder queryBuilder = elasticSearchKmalBuildQuery.query(dslPrompt);
        System.out.println(queryBuilder.toString());
    }
}
```



### Prompt vs Translation 

```shell
KmalSQ prompt: [asin.keyword] == '0609804340';
->
{
  "bool" : {
    "must" : [
      {
        "match" : {
          "asin.keyword" : {
            "query" : "0609804340",
            "operator" : "OR",
            "prefix_length" : 0,
            "max_expansions" : 50,
            "fuzzy_transpositions" : true,
            "lenient" : false,
            "zero_terms_query" : "NONE",
            "auto_generate_synonyms_phrase_query" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```


```shell
KmalSQ prompt: ([reviews.entries.date] RANGOUT FROM 04/04/2004 TO 05/08/2005 
                    AND [title.keyword] ENDS_WITH 'say') OR  [title.keyword] CONTAINS 'something';
->
{
  "bool" : {
    "should" : [
      {
        "bool" : {
          "must" : [
            {
              "bool" : {
                "must_not" : [
                  {
                    "bool" : {
                      "must" : [
                        {
                          "range" : {
                            "reviews.entries.date" : {
                              "from" : 1081051200000,
                              "to" : 1123300799000,
                              "include_lower" : true,
                              "include_upper" : true,
                              "boost" : 1.0
                            }
                          }
                        }
                      ],
                      "adjust_pure_negative" : true,
                      "boost" : 1.0
                    }
                  }
                ],
                "adjust_pure_negative" : true,
                "boost" : 1.0
              }
            },
            {
              "bool" : {
                "must" : [
                  {
                    "wildcard" : {
                      "title.keyword" : {
                        "wildcard" : "*say",
                        "boost" : 1.0
                      }
                    }
                  }
                ],
                "adjust_pure_negative" : true,
                "boost" : 1.0
              }
            }
          ],
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      },
      {
        "bool" : {
          "must" : [
            {
              "wildcard" : {
                "title.keyword" : {
                  "wildcard" : "*something*",
                  "boost" : 1.0
                }
              }
            }
          ],
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}

```


```shell
KmalSQ prompt: NOT ([asin.keyword] == '0786926503' AND NOT ([title.keyword] STARTS_WITH 'The' OR [title.keyword] STARTS_WITH 'B'));
->
{
  "bool" : {
    "must_not" : [
      {
        "bool" : {
          "must" : [
            {
              "bool" : {
                "must" : [
                  {
                    "match" : {
                      "asin.keyword" : {
                        "query" : "0786926503",
                        "operator" : "OR",
                        "prefix_length" : 0,
                        "max_expansions" : 50,
                        "fuzzy_transpositions" : true,
                        "lenient" : false,
                        "zero_terms_query" : "NONE",
                        "auto_generate_synonyms_phrase_query" : true,
                        "boost" : 1.0
                      }
                    }
                  }
                ],
                "adjust_pure_negative" : true,
                "boost" : 1.0
              }
            },
            {
              "bool" : {
                "must_not" : [
                  {
                    "bool" : {
                      "should" : [
                        {
                          "bool" : {
                            "must" : [
                              {
                                "wildcard" : {
                                  "title.keyword" : {
                                    "wildcard" : "The*",
                                    "boost" : 1.0
                                  }
                                }
                              }
                            ],
                            "adjust_pure_negative" : true,
                            "boost" : 1.0
                          }
                        },
                        {
                          "bool" : {
                            "must" : [
                              {
                                "wildcard" : {
                                  "title.keyword" : {
                                    "wildcard" : "B*",
                                    "boost" : 1.0
                                  }
                                }
                              }
                            ],
                            "adjust_pure_negative" : true,
                            "boost" : 1.0
                          }
                        }
                      ],
                      "adjust_pure_negative" : true,
                      "boost" : 1.0
                    }
                  }
                ],
                "adjust_pure_negative" : true,
                "boost" : 1.0
              }
            }
          ],
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```