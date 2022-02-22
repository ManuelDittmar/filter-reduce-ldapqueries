# filter-reduce-ldapqueries
 Cache groups to reduce amount of ldap queries. Inspired by https://github.com/camunda-consulting/code/tree/master/snippets/authentication-filter-with-bypass#readme

Disclaimer: The filter is not tested and not intended to be used in production. it is only used to demonstrate how a custom filter can look like.

## Tomcat Setup

1. Build the jar file with `mvn clean install`.
2. Copy the resulting file jar into the WEB-INF/lib-folder of the extracted engine-rest war under `camunda-bpm-tomcat-7.10.0\server\apache-tomcat-9.0.52\webapps\engine-rest\WEB-INF\lib\`
3. Wire the classes from this project in the `web.xml` of engine rest (`camunda-bpm-tomcat-7.16.0\server\apache-tomcat-9.0.12\webapps\engine-rest\WEB-INF\web.xml`):

```
 <filter>
    <filter-name>camunda-auth</filter-name>
    <filter-class>
      org.example.CustomFilter
    </filter-class>
	<async-supported>true</async-supported>
    <init-param>
      <param-name>authentication-provider</param-name>
      <param-value>org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider</param-value>
    </init-param>
    <init-param>
	    <param-name>rest-url-pattern-prefix</param-name>
	    <param-value></param-value>
	  </init-param> 
  </filter>
```

## Guava Cache

```
 Cache<String, List<String>> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
```

Find details here: https://github.com/google/guava/wiki/CachesExplained
