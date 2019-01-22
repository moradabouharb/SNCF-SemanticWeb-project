package SemanticWeb.SNCF.Controller;

import SemanticWeb.SNCF.Utility.TextfileParser;
import SemanticWeb.SNCF.service.RDFgenerator;
import SemanticWeb.SNCF.service.Service1;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.core.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TurtleController {

    private List<String> formats = new ArrayList<String>(Arrays.asList("TURTLE", "TTL", "Turtle", "N-TRIPLES", "N-TRIPLE", "NT", "JSON-LD", "RDF/XML-ABBREV", "RDF/XML", "N3", "RDF/JSON"));
    final String owl = "http://dbpedia.org/ontology/";
    String prefixs = "prefix geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#> \n" +
            "prefix xsdTime: <http://www.w3.org/2001/XMLSchema#time> \n" +
            "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "prefix dbo:   <http://dbpedia.org/ontology/> \n" +
            "prefix Proute: <http://localhost/route#> \n" +
            "prefix latitude: <http://www.w3.org/2003/01/geo/wgs84_pos#lat> \n" +
            "prefix Ptrip: <http://localhost/trip#> \n" +
            "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n" +
            "prefix xsdString: <http://www.w3.org/2001/XMLSchema#string> \n" +
            "prefix db:    <http://dbpedia.org/resource/> \n" +
            "prefix xsd: <http://www.w3.org/2001/XMLSchema> \n" +
            "prefix longitude: <http://www.w3.org/2003/01/geo/wgs84_pos#long> \n" +
            "prefix Pstop: <http://localhost/stop#> \n\n\n"
            ;

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
    public String GetLocalQuery(@RequestParam(value = "button", required = false) String btn, Model model){
        if (!StringUtils.isEmpty(btn)){
            int i = Integer.parseInt(btn);

            if (i == 4){
                model.addAttribute("query", prefixs +
                        "SELECT  ?NameofRoute ?NameofStopStation ?latitude ?longitude ?TimeTravel\n" +
                        "WHERE {\n" +
                        "?trip dbo:endPoint ?StopStation;\n" +
                        "dbo:Time_travel ?TimeTravel;\n" +
                        "dbo:route ?Route.\n" +
                        "?Route rdfs:label ?NameofRoute.\n" +
                        "?StopStation rdfs:label ?NameofStopStation;\n" +
                        "db:latitude ?latitude;\n" +
                        "db:longitude ?longitude.\n" +
                        "}\n" +
                        "Limit 20");
            }else if (i == 5){
                model.addAttribute("query", prefixs +
                        "SELECT ?Travel_time ?Stop_Station\n" +
                        "WHERE {\n" +
                        "?trip dbo:Time_travel ?Travel_time ;\n" +
                        "dbo:endPoint Pstop:StopPointOCETrainTER-87775288.\n" +
                        "Pstop:StopPointOCETrainTER-87775288 rdfs:label ?Stop_Station;\n" +
                        "\n" +
                        "}\n" +
                        "limit 20");
            }else if (i == 6){
                model.addAttribute("query", prefixs +
                        "SELECT ?Travel_time ?Stop_Station\n" +
                        "WHERE {\n" +
                        "?trip dbo:Time_travel ?Travel_time ;\n" +
                        "dbo:endPoint Pstop:StopPointOCETrainTER-87775288.\n" +
                        "Pstop:StopPointOCETrainTER-87775288 rdfs:label ?Stop_Station;\n" +
                        "filter(?Travel_time = \"10:01:00\"^^xsd:time)\n" +
                        "}\n");
            }else if (i == 7){
                model.addAttribute("query", prefixs +
                        "SELECT ?RouteName ?TravelTime\n" +
                        "WHERE {\n" +
                        "?trip dbo:route ?route;\n" +
                        "dbo:Time_travel ?TravelTime.\n" +
                        "?route rdfs:label ?RouteName.\n" +
                        "}\n" +
                        "limit 20");
            }else if (i == 8){
                model.addAttribute("query", prefixs +
                        "SELECT ?Travel_time ?Stop_Station\n" +
                        "WHERE {\n" +
                        "?trip dbo:Time_travel ?Travel_time ;\n" +
                        "dbo:endPoint ?stop_point.\n" +
                        "?stop_point rdfs:label ?Stop_Station.\n" +
                        "\n" +
                        "}\n" +
                        "limit 20");
            }
        }
        return "query1";
    }

    @PostMapping("/query1")
    public String PostLocalQuery(@RequestParam("query") String query, Model model){
        model.addAttribute("formats", formats);

        org.apache.jena.rdf.model.Model model1 = RG.RDF();

        Query queryJena = QueryFactory.create(query);
        QueryExecution queryExecution = QueryExecutionFactory.create(queryJena, model1);
        List<Var> vars = queryJena.getProjectVars();
        List<List<String>> data = new ArrayList<List<String>>();

        try{
            ResultSet resultSet = queryExecution.execSelect();

                while (resultSet.hasNext()){
                    List<String> strings = new ArrayList<String>();
                    QuerySolution querySolution = resultSet.nextSolution();

                    for (Var var : vars) {
                        if (querySolution.contains(var.getVarName()))
                            if (querySolution.get(var.getName()).isLiteral())
                                strings.add(querySolution.getLiteral(var.getName()).toString());
                            else if (querySolution.get(var.getName()).isResource())
                                strings.add(querySolution.getResource(var.getName()).toString());
                    }
                    data.add(strings);
                }

        } finally {
            queryExecution.close();
        }

        model.addAttribute("vars", vars);
        model.addAttribute("data", data);
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
