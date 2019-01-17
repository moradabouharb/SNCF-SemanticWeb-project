package SemanticWeb.SNCF.service;


import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class Service1 {

    @Autowired
    private RDFgenerator RG;

    public static String SendQuery(String url, String query, String format){
        QueryExecution qexec = QueryExecutionFactory.sparqlService(url, query);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String result = "";
        Model model = qexec.execConstruct();
        model.write(os, format);
        result += os.toString();
        return result;
    }

    public static Model SendQuery(String url, String query){
        QueryExecution qexec = QueryExecutionFactory.sparqlService(url, query);
        return qexec.execConstruct();
    }

    public QueryExecution LocalSparQuery(String query){

        Model model = RG.RDF();
        if (model != null){
            Query query1 = QueryFactory.create(query);
            QueryExecution qexec = QueryExecutionFactory.create(query1, model);
            return qexec;
        }else
            return null;
    }

    public QueryExecution LocalSparQuery(String query, Model model){

        if (model != null){
            Query query1 = QueryFactory.create(query);
            QueryExecution qexec = QueryExecutionFactory.create(query1, model);
            return qexec;
        }else
            return null;
    }

}
