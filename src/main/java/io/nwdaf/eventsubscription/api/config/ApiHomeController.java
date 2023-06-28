package io.nwdaf.eventsubscription.api.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ApiHomeController {
    @GetMapping(value = "/")
    public RedirectView redirectWithUsingRedirectView(
    	      RedirectAttributes attributes) {
    	        return new RedirectView("swagger-ui/index.html");
    	    }
}
