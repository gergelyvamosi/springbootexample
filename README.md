NoSQL // Cassandra example enterprise Java app, CRUD (REST api), Sring Boot 3.0.4



Prerequisities:

    Debian 10.13
    Cassandra 4.1.0
    cqlsh 6.1.0
    Java OpenJDK 17 64bit

    development:
    VS Code 1.76.0
    Extension Pack for Java (e.g. maven)
    Spring Boot Extension Pack



Cassandra Table:

create keyspace gergelyvamosi with replication={'class':'SimpleStrategy', 'replication_factor':1};

use gergelyvamosi;

CREATE TABLE demo (
   id timeuuid PRIMARY KEY,
   title text,
   description text,
   published boolean
);

CREATE CUSTOM INDEX idx_title ON gergelyvamosi.demo (title) 
USING 'org.apache.cassandra.index.sasi.SASIIndex' 
WITH OPTIONS = {
'mode': 'CONTAINS', 
'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer', 
'case_sensitive': 'false'};



To test I used Thunder Client plugin within VS Code // simple send via HTTP e.g. POST method JSON messages to http://localhost:8080/api/demos to add new demo items.

Cheers!