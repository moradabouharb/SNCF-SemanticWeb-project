package SemanticWeb.SNCF.Controller;

import SemanticWeb.SNCF.Utility.TextfileParser;
import SemanticWeb.SNCF.service.RDFgenerator;
import SemanticWeb.SNCF.service.Service1;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TurtleController {

    private List<String> formats = new ArrayList<String>(Arrays.asList("TURTLE", "TTL", "Turtle", "N-TRIPLES", "N-TRIPLE", "NT", "JSON-LD", "RDF/XML-ABBREV", "RDF/XML", "N3", "RDF/JSON"));
    final String owl = "http://dbpedia.org/ontology/";

    @Autowired
    private RDFgenerator RG;

    @Autowired
    private TextfileParser TP;

    @Autowired
    private Service1 service1;

    @GetMapping(value = "turtle", produces = {"text/turtle"})
    public @ResponseBody
    String rdfturtle(){
        return RG.RDF(TP.FileRoutesParser(),TP.FileTripParser(),TP.FileStopsParser(),TP.FileStoptimesParser());
    }

    @GetMapping(value = "turtle_plain", produces = {"text/plain"})
    public @ResponseBody
    String rdfturtletext(){
        return RG.Getmodeltext(RG.RDF());
    }


    @GetMapping("/query3")
    public String GetUserFileQuery(Model model){
        model.addAttribute("formats", formats);
        return "query3";
    }

    @PostMapping("/query3")
    public String PostUserFileQuery(@RequestParam("query") String query, @RequestParam("format") String format, @RequestParam("file") MultipartFile file, Model model) throws IOException {

        org.apache.jena.rdf.model.Model modelJena1 = ModelFactory.createDefaultModel();
        org.apache.jena.rdf.model.Model modelJena = ModelFactory.createDefaultModel();
        modelJena1.read(file.getInputStream(), "");
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        modelJena.write(byteArrayOutputStream);
        String result = "";
        List<String> strings = new ArrayList<String>();


        Query queryJena = QueryFactory.create(query);
        QueryExecution queryExecution = QueryExecutionFactory.create(queryJena, modelJena1);

        try{
            ResultSet resultSet = queryExecution.execSelect();
            String str = "";
            while (resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                str = querySolution.toString();
                strings.add(str);
            }

        } finally {
            queryExecution.close();
        }

        model.addAttribute("formats", formats);
        model.addAttribute("query", query);
        model.addAttribute("result", result);
        model.addAttribute("list", strings);
        return "query3";
    }

    @GetMapping("/query1")
    public String GetLocalQuery(Model model){
        model.addAttribute("formats", formats);
        return "query1";
    }

    @PostMapping("/query1")
    public String PostLocalQuery(@RequestParam("query") String query, @RequestParam("format") String format, Model model){
        model.addAttribute("formats", formats);

        List<String> strings = new ArrayList<String>();
        org.apache.jena.rdf.model.Model model1 = RG.RDF();

        Query queryJena = QueryFactory.create(query);
        QueryExecution queryExecution = QueryExecutionFactory.create(queryJena, model1);

        try{
            ResultSet resultSet = queryExecution.execSelect();
            String str = "";
            while (resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                str = querySolution.toString();
                strings.add(str);
            }

        } finally {
            queryExecution.close();
        }

        model.addAttribute("list", strings);
        model.addAttribute("query", query);

        return "query1";
    }

    @GetMapping("/query")
    public String GetSPARSQLquery(Model model){
        model.addAttribute("formats", formats);
        return "query";
    }

    @PostMapping("/query")
    public String PostSPARSQLquery(@RequestParam("url") String url, @RequestParam("query") String query, @RequestParam("format") String format, Model model){
        System.out.println(String.format("url: %s \n query: %s", url, query));
        List<Statement> stmlist = Service1.SendQuery(url, query).listStatements().toList();
        model.addAttribute("result", stmlist);
        model.addAttribute("query", query);
        model.addAttribute("url", url);
        model.addAttribute("formats", formats);
        model.addAttribute("format", format);
        return "query";
    }
}
