package SemanticWeb.SNCF.Controller;

import SemanticWeb.SNCF.Utility.TextfileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class route {

    @Autowired
    TextfileParser T;

    @GetMapping("/route")
    public ModelAndView route(){
        List<String> routeslist = T.FileRoutesParser().get(0); // I need the first list (there are 2 lists of list routes..
        ModelAndView model = new ModelAndView("route.html");
        model.addObject("list", routeslist);
        return model;
    }
}
