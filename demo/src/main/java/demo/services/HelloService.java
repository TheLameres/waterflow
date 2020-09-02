package demo.services;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloService {

    public String hello(){
        return "hello";
    }
}
