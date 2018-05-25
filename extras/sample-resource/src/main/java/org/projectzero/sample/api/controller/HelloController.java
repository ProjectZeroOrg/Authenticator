package org.projectzero.sample.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("hasRole('USER')")
public class HelloController {
    @GetMapping("/hello")
    public String greeting(Principal principal) {
        return "\"Hello, " + principal.getName() +"\"";
    }
}
