package io.nwdaf.eventsubscription.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;

@Controller
public class ClientHomeController {
	@Autowired
	private Environment env;
	@Autowired
    @Qualifier("subjects")
    private List<String> subjects;

    @Autowired
    @Qualifier("days")
    private List<String> days;

    @Autowired
    @Qualifier("months")
    private List<String> months;

    @Autowired
    @Qualifier("years")
    private List<String> years;
    
    @GetMapping(value="/client/form")
    public String get(ModelMap model){
        NnwdafEventsSubscription object = new NnwdafEventsSubscription();
        object.notificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        model.addAttribute("nnwdafEventsSubscription", object);
        return "form";

    }
    @PostMapping(value="/client/form")
    public String post(NnwdafEventsSubscription object, ModelMap map){
        map.put("result", object);
        return "form";
    }

    @ModelAttribute("subjects")
    public List<String> subjects(){
        return subjects;
    }
    @ModelAttribute("days")
    public List<String> days(){
        return days;
    }
    @ModelAttribute("months")
    public List<String> months(){
        return months;
    }
    @ModelAttribute("years")
    public List<String> years(){
        return years;
    }
    @ModelAttribute("grades")
    public Grade [] grades(){
        return Grade.values();
    }
}
