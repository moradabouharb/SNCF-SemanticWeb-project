package SemanticWeb.SNCF.Controller;
import SemanticWeb.SNCF.Utility.TextfileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class info {

    @Autowired
    TextfileParser T;

    @GetMapping("/routes")
    public ModelAndView route(){
        List<String> routeslist = T.FileRoutesParser().get(0); // I need the first list (there are 2 lists of list routes..
        ModelAndView model = new ModelAndView("route.html");
        model.addObject("list", routeslist);
        return model;
    }

    @GetMapping("/stops")
    public ModelAndView stops(){
        List<String> routeslist = T.FileStopsParser().get(0); // I need the first list (there are 2 lists of list routes..
        ModelAndView model = new ModelAndView("stops.html");
        model.addObject("list", routeslist);
        return model;
    }

    @GetMapping("/trips")
    public ModelAndView trips(){
        List<String> routeslist = T.FileTripParser().get(1); // I need the first list (there are 2 lists of list routes..
        ModelAndView model = new ModelAndView("trips.html");
        model.addObject("list", routeslist);
        return model;
    }
}