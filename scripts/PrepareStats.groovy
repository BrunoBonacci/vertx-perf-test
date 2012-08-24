/*
   Copyright 2012 - Bruno Bonacci

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

if( args.size() != 1 ){
    println "use: groovy scripts/PrepareStats.groovy ./your/test/results/vx-perf-data.csv"
    return
}

results = []
def lines = new File(args[0]).readLines().collect {
    line ->
    line.split(",").collect { it.trim() }
}

// we need to sum the output coming from same test (name + nProd + nCons)
def groups = lines.groupBy { it[0] + ":" + it[1] + ":" +it[2] }

groups.each{
    k,v->
    long total = v.sum{ it[3].toLong() }
    results << [shortName: v[0][0]-"com.brunobonacci.vertx.perf.", packg: v[0][0], nProd: v[0][1], nCons: v[0][2], throughput: total ];
}

println "test-case\tprod\tcons\tthroughput"
results.each{
    v ->
    println "$v.shortName\t$v.nProd\t$v.nCons\t$v.throughput"
}