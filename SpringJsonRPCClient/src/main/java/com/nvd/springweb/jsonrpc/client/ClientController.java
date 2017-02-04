package com.nvd.springweb.jsonrpc.client;

import com.googlecode.jsonrpc4j.spring.rest.JsonRpcRestClient;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    private static final Logger logger = Logger.getLogger(ClientController.class.getName());

    @RequestMapping(value = "multiplier")
    @ResponseBody
    public Object getMultiplierResult(@RequestParam("a") long a, @RequestParam("b") long b) {
        try {
            URL url = new URL(getAPIUrl());
            JsonRpcRestClient client = new JsonRpcRestClient(url);
            Map<String, Object> params = new HashMap<>();
            params.put("a", a);
            params.put("b", b);
            Long result = client.invoke("multiplier", params, Long.class);
            return String.valueOf(result);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error while invoking multiplier API", t);
        }
        return null;
    }

    @RequestMapping(value = "printperson")
    @ResponseBody
    public Object printPerson() {
        try {
            URL url = new URL(getAPIUrl());
            JsonRpcRestClient client = new JsonRpcRestClient(url);
            Map<String, Object> params = new HashMap<>();
            params.put("person", new Person("Alex", 1982));
            String result = client.invoke("printPerson", params, String.class);
            return result;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error while invoking printPerson API", t);
        }
        return null;
    }

    @RequestMapping(value = "printpersons")
    @ResponseBody
    public Object printPersons() {
        try {
            URL url = new URL(getAPIUrl());
            JsonRpcRestClient client = new JsonRpcRestClient(url);
            Map<String, Object> params = new HashMap<>();
            params.put("persons", Arrays.asList(new Person("Dynamo City", 1982), new Person("Solar City", 2016)));
            String result = client.invoke("printPersons", params, String.class);
            return result;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error while invoking printPersons API", t);
        }
        return null;
    }

    private static String getAPIUrl() {
        return "http://localhost:8080/jsonrpc4j/api";
    }
}
