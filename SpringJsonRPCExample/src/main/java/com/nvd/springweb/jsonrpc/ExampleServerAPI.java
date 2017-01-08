package com.nvd.springweb.jsonrpc;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import java.util.List;

@JsonRpcService("/calculator")
public interface ExampleServerAPI {

    long multiplier(@JsonRpcParam(value = "a") long a, @JsonRpcParam(value = "b") long b);

    String getStringValue(@JsonRpcParam(value = "person") Person person);
    
    String printPersons(@JsonRpcParam(value = "persons") List<Person> persons);
}
