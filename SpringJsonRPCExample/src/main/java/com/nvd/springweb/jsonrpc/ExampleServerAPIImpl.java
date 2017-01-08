package com.nvd.springweb.jsonrpc;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@AutoJsonRpcServiceImpl
public class ExampleServerAPIImpl implements ExampleServerAPI {

    @Override
    public long multiplier(long a, long b) {
        return a * b;
    }

    @Override
    public String getStringValue(Person person) {
        return person.toString();
    }

   @Override 
    public String printPersons(List<Person> persons) {
        return persons.toString();
    }
    
}

