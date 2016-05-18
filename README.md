elasticsearch-analysis-idcard
-----------------------------

ID card analyzer plugin for elasticsearch.

Analyzer: `idcard`, Tokenizer: `idcard`

Installation
------------

1. compile

`mvn clean package`

copy and unzip `target/release/elasticsearch-analysis-idcard-{version}.zip` to `your-es-root/plugins/idcard`

2. restart elasticsearch


Quick Start
-----------

Analyzer Test
```
    curl -XGET 'http://localhost:9200/_analyze?analyzer=idcard&pretty' -d '511234198005066789'
```

Result
```
    {
      "tokens" : [ {
        "token" : "511234198005066789",
        "start_offset" : 0,
        "end_offset" : 0,
        "type" : "word",
        "position" : 0
      }, {
        "token" : "511234",
        "start_offset" : 0,
        "end_offset" : 0,
        "type" : "word",
        "position" : 1
      }, {
        "token" : "5112341980",
        "start_offset" : 0,
        "end_offset" : 0,
        "type" : "word",
        "position" : 2
      }, {
        "token" : "51123419800506",
        "start_offset" : 0,
        "end_offset" : 0,
        "type" : "word",
        "position" : 3
      }, {
        "token" : "6789",
        "start_offset" : 0,
        "end_offset" : 0,
        "type" : "word",
        "position" : 4
      } ]
    }

```
