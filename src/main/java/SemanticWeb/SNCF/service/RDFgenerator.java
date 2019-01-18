package SemanticWeb.SNCF.service;

import SemanticWeb.SNCF.Utility.TextfileParser;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@Service
public class RDFgenerator {

    @Autowired
    private TextfileParser TP;

    public String RDF(List<List<String>> list1, List<List<String>> list2, List<List<String>> list3, List<List<String>> list4){
        Writer out = new StringWriter();
        Model model = getModel(list1, list2, list3, list4);
        return model.write(out,"turtle").toString();
    }

    public Model RDF(){
        return getModel(TP.FileRoutesParser(),TP.FileTripParser(),TP.FileStopsParser(),TP.FileStoptimesParser());
    }

    private Model getModel(List<List<String>> list1, List<List<String>> list2, List<List<String>> list3, List<List<String>> list4) {
        Model model = ModelFactory.createDefaultModel();
        final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
        final String owl = "http://dbpedia.org/ontology/";
        final String db = "dbpedia.org/resource/";
        final String geo = "http://www.w3.org/2003/01/geo/wgs84_pos#";
        final String latitude = "http://www.w3.org/2003/01/geo/wgs84_pos#lat";
        final String longitude = "http://www.w3.org/2003/01/geo/wgs84_pos#long";
        final String Proute = "http://localhost/route#";
        final String Ptrip = "http://localhost/trip#";
        final String Pstop = "http://localhost/stop#";
        final String xsdString = XSD.xstring.getURI();
        final String xsdTime = XSD.time.getURI();
        model.setNsPrefix("rdfs",rdfs);
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("owl", owl);
        model.setNsPrefix("db", db);
        model.setNsPrefix("xsdString", xsdString);
        model.setNsPrefix("xsdTime", xsdTime);
        model.setNsPrefix("geo", geo);
        model.setNsPrefix("latitude", latitude);
        model.setNsPrefix("longitude", longitude);
        model.setNsPrefix("Proute", Proute);
        model.setNsPrefix("Ptrip", Ptrip);
        model.setNsPrefix("Pstop", Pstop);
        Property routelebel = model.createProperty(rdfs, "label");
        Property routeOfTransportation = model.createProperty(owl, "RouteOfTransportation");
        Property rdftype = model.createProperty(RDF.getURI(), "type");
        Property propertytrip = model.createProperty(db, "Trip");
        Property propertyroute = model.createProperty(owl, "route");
        Property timetravel = model.createProperty(owl, "Time_travel");
        Property endpoint = model.createProperty(owl, "endPoint");
        Property lat = model.createProperty(owl, "latitude");
        Property longi = model.createProperty(owl, "longitude");

        for(int i=1; i<list1.get(0).size(); i++) {
            Resource route = model.createProperty(Proute,list1.get(0).get(i));
            Literal routename = model.createTypedLiteral(list1.get(1).get(i),xsdString);
            model.add(route,routelebel,routename);
            model.add(route,rdftype,routeOfTransportation);
        }
        for(int i=1; i<list2.get(0).size(); i++) {
            Resource trip = model.createProperty(Ptrip,list2.get(1).get(i));
            model.add(trip,rdftype,propertytrip);
            model.add(trip,propertyroute,list2.get(0).get(i));
        }
        for(int i=1; i<list3.get(0).size(); i++) {
            Resource stop = model.createProperty(Pstop,list3.get(0).get(i));
            Literal latitudelet = model.createTypedLiteral(list3.get(2).get(i),latitude);
            Literal longitudelet = model.createTypedLiteral(list3.get(3).get(i),longitude);
            Literal endpointname = model.createTypedLiteral(list3.get(1).get(i),xsdString);
            model.add(stop, routelebel, endpointname);
            model.add(stop, lat, latitudelet);
            model.add(stop, longi, longitudelet);
            model.add(stop, rdftype, endpoint);
        }
        for(int i=1; i<list4.get(0).size(); i++) {
            Resource trip = model.createProperty(Ptrip,list4.get(0).get(i));
            Literal departuretime = model.createTypedLiteral(list4.get(2).get(i),xsdTime);
            Literal literalendpoint = model.createTypedLiteral(list4.get(3).get(i),xsdString);
            model.add(trip,rdftype,propertytrip);
            model.add(trip,timetravel,departuretime);
            model.add(trip, endpoint, literalendpoint);
        }
        return model;
    }
}
