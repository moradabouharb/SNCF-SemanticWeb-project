package SemanticWeb.SNCF.Controller;

import SemanticWeb.SNCF.Utility.TextfileParser;
import SemanticWeb.SNCF.service.RDFgenerator;
import SemanticWeb.SNCF.service.Service1;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
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

//        Query query1 = QueryFactory.create(query);
//        QueryExecution qexec = QueryExecutionFactory.create(query, modelJena1);
//        Iterator<Triple> triples = qexec.execConstructTriples();
//
//        try{
//            while (triples.hasNext()){
//                Triple triple = triples.next();
//                result += triple.toString() + " <br>";
//            }
//
//        }finally {
//            qexec.close();
//        }

        StmtIterator iter = modelJena1.listStatements();
        while (iter.hasNext()){
            Statement stm = iter.nextStatement();
            Resource subject = stm.getSubject();
            Property properties = stm.getPredicate();
            RDFNode object = stm.getObject();

            result += subject.toString() + " " + properties.toString() + " ";

            if (object instanceof Resource)
                result += object.toString();
            else
                result += "\"" + object + "\"";
            result += " . <br>";
        }

        model.addAttribute("formats", formats);
        model.addAttribute("query", query);
        model.addAttribute("result", result);
        return "query3";
    }






    @GetMapping("/query2")
    public String GetUserQuery(Model model){
        model.addAttribute("formats", formats);
        return "query2";
    }

    @PostMapping("/query2")
    public String PostUserQuery(@RequestParam("query") String query, @RequestParam("format") String format, @RequestParam("path") String path, Model model) throws IOException {


        model.addAttribute("formats", formats);
        FileManager.get().addLocatorClassLoader(this.getClass().getClassLoader());

        org.apache.jena.rdf.model.Model model1 = FileManager.get().loadModel("C:/Users/Good/Desktop/" + path);
        String res = "";

        QueryExecution queryExecution = service1.LocalSparQuery(query, model1);

//        try {
//            queryExecution.
//            ResultSet resultSet = queryExecution.execSelect();
//            while(resultSet.hasNext()){
//                QuerySolution solution = resultSet.nextSolution();
//                Literal name = solution.getLiteral("x");
//                res += name;
//            }
//        }
//        finally {
////            queryExecution.close();
//        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        org.apache.jena.rdf.model.Model model2 =  queryExecution.execConstruct();

        model1.write(outputStream, format);

        model.addAttribute("path", path);
        model.addAttribute("result", outputStream.toString());
        model.addAttribute("query", query);

        return "query2";
    }

    @GetMapping("/query1")
    public String GetLocalQuery(Model model){
        model.addAttribute("formats", formats);
        return "query1";
    }

    @PostMapping("/query1")
    public String PostLocalQuery(@RequestParam("query") String query, @RequestParam("format") String format, Model model){
        model.addAttribute("formats", formats);

        QueryExecution queryExecution = service1.LocalSparQuery(query);
        List<String> list = new ArrayList<String>();
        try {
            ResultSet resultSet = queryExecution.execSelect();
            list = resultSet.getResultVars();
        }
        finally {
            queryExecution.close();
        }
        model.addAttribute("list", list);
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
