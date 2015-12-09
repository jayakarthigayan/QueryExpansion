# QueryExpansion
Music-Search-Query-Expansion


Sample Request:

http://localhost:8080/queryexpansion/qe/getexpquery

{
   "queryText":"elmore hooker",
   "matchingDocs":["em_65400;Elmore James;Various Artists;Jazz & Blues;*^*;*^*","em_65401;Tribute To Elmore;Various Artists;Jazz & Blues;*^*;*^**","em_65406;John Lee Hooker;Various Artists;Jazz & Blues;*^*;*^*","em_65407;Got My Mojo Working;Various Artists;Jazz & Blues;*^*;*^*"]
}

Sample Response:

{
"roccioQuery": "elmore hooker jame john lee tribute"
"associationQuery": "elmore hooker artist blues john lee"
"metricQuery": "elmore hooker jame lee john"
"scalarQuery": "elmore hooker jame tribute john lee"
}