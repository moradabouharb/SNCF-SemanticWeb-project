package SemanticWeb.SNCF.service;
import com.github.jsonldjava.core.RDFDataset;
import org.apache.jena.graph.Node_Blank;
import org.apache.jena.vocabulary.XSD;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;
import org.apache.jena.rdf.model.ModelFactory;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@Service
public class RDFgenerator {

    public String RDF(List<List<String>> list1, List<List<String>> list2, List<List<String>> list3, List<List<String>> list4){
        Writer out = new StringWriter();
        Model model = ModelFactory.createDefaultModel();
        final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
        final String owl = "http://dbpedia.org/ontology/";
        final String db = "dbpedia.org/resource/";
        model.setNsPrefix("rdfs",rdfs);
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("owl", owl);
        model.setNsPrefix("db", db);

        Property routelebel = model.createProperty(rdfs, "label");
        Property routeOfTransportation = model.createProperty(owl, "RouteOfTransportation");
        Property rdftype = model.createProperty(RDF.getURI(), "type");
        Property propertytrip = model.createProperty(db, "Trip");
        Property propertyroute = model.createProperty(owl, "route");
        Property timetravel = model.createProperty(owl, "Time_travel");
        Property endpoint = model.createProperty(owl, "endPoint");

        for(int i=1; i<list1.get(0).size(); i++) {
            Resource route = model.createResource(list1.get(0).get(i));
            Literal routename = model.createTypedLiteral(list1.get(1).get(i),XSD.xstring.getURI());
            model.add(route,routelebel,routename);
            model.add(route,rdftype,routeOfTransportation);
        }

        for(int i=1; i<list2.get(0).size(); i++) {
            Resource trip = model.createResource(list2.get(1).get(i));
            model.add(trip,rdftype,propertytrip);
            model.add(trip,propertyroute,list2.get(0).get(i));
       	}
/*
        for(int i=1; i<list3.get(0).size(); i++) {
       		System.out.println(list3.get(0).get(i));
       		System.out.println(list3.get(1).get(i));
       		System.out.println(list3.get(2).get(i));
       		System.out.println(list3.get(3).get(i));
       	}
*/
        for(int i=1; i<list4.get(0).size(); i++) {
            Resource trip = model.createResource(list4.get(0).get(i));
            Literal departuretime = model.createTypedLiteral(list4.get(2).get(i),XSD.time.getURI());
            model.add(trip,rdftype,propertytrip);
            model.add(trip,timetravel,departuretime);
            model.add(trip, endpoint, list4.get(3).get(i),XSD.xstring.getURI());
            /*
       		System.out.println(list4.get(0).get(i));
       		System.out.println(list4.get(1).get(i));
       		System.out.println(list4.get(2).get(i));
       		System.out.println(list4.get(3).get(i));
       		*/
       	}

        model.write(out,"turtle");
        return out.toString();
    }
}
