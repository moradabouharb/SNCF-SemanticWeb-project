package SemanticWeb.SNCF.Controller;

import SemanticWeb.SNCF.Utility.TextfileParser;
import SemanticWeb.SNCF.service.RDFgenerator;
import SemanticWeb.SNCF.service.Service1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TurtleController {

    private List<String> formats = new ArrayList<String>(Arrays.asList("TURTLE", "TTL", "Turtle", "N-TRIPLES", "N-TRIPLE", "NT", "JSON-LD", "RDF/XML-ABBREV", "RDF/XML", "N3", "RDF/JSON"));

    @Autowired
    RDFgenerator RG;

    @Autowired
    TextfileParser TP;

    @GetMapping(value = "turtle",produces = {"text/plain"})
    public @ResponseBody
    String rdfturtle(){
        return RG.RDF(TP.FileRoutesParser(),TP.FileTripParser(),TP.FileStopsParser(),TP.FileStoptimesParser());
    }


    @GetMapping("/query")
    public String GetSPARSQLquery(Model model){
        model.addAttribute("formats", formats);
        return "query";
    }

    @PostMapping("/query")
    public String PostSPARSQLquery(@RequestParam("url") String url, @RequestParam("query") String query, @RequestParam("format") String format, Model model){
        System.out.println(String.format("url: %s \n query: %s", url, query));
        String result = Service1.SendQuery(url, query, format);
        model.addAttribute("result", result);
        model.addAttribute("query", query);
        model.addAttribute("url", url);
        model.addAttribute("formats", formats);
        model.addAttribute("format", format);
        return "query";
    }
}
