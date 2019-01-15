package SemanticWeb.SNCF.service;


import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class Service1 {

    public static String SendQuery(String url, String query, String format){
        QueryExecution qexec = QueryExecutionFactory.sparqlService(url, query);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String result = "";
        Model model = qexec.execConstruct();
        model.write(os, format);
        result += os.toString();
        return result;
    }
}
