package SemanticWeb.SNCF.Controller;

import SemanticWeb.SNCF.Utility.TextfileParser;
import SemanticWeb.SNCF.service.RDFgenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TurtleController {

    @Autowired
    RDFgenerator RG;

    @Autowired
    TextfileParser TP;

    @GetMapping(value = "turtle",produces = {"text/plain"})
    public @ResponseBody
    String rdfturtle(){
        return RG.RDF(TP.FileRoutesParser(),TP.FileTripParser(),TP.FileStopsParser(),TP.FileStoptimesParser());
    }
}
