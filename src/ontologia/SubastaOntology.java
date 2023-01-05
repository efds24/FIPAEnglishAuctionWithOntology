// file: SubastaOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package ontologia;

import jade.content.onto.*;
import jade.content.schema.*;

/** file: SubastaOntology.java
 * @author OntologyBeanGenerator v4.1
 * @version 2023/01/5, 14:46:24
 */
public class SubastaOntology extends jade.content.onto.Ontology  {

  private static final long serialVersionUID = 6002065570892668532L;

  //NAME
  public static final String ONTOLOGY_NAME = "subasta";
  // The singleton instance of this ontology
  private static Ontology theInstance = new SubastaOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String INFORMAR_LIBRO="libro";
    public static final String INFORMAR_PRECIOACTUAL="precioActual";
    public static final String INFORMAR="Informar";
    public static final String ACEPTARPUJA_LIBRO="libro";
    public static final String ACEPTARPUJA_PRECIOACTUAL="precioActual";
    public static final String ACEPTARPUJA="AceptarPuja";
    public static final String INICIARPUJA_LIBRO="libro";
    public static final String INICIARPUJA_PRECIOACTUAL="precioActual";
    public static final String INICIARPUJA="IniciarPuja";
    public static final String RECHAZARPUJA_LIBRO="libro";
    public static final String RECHAZARPUJA_PRECIOACTUAL="precioActual";
    public static final String RECHAZARPUJA="RechazarPuja";
    public static final String LIBRO_TITULO="titulo";
    public static final String LIBRO="Libro";

  /**
   * Constructor
  */
  private SubastaOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema libroSchema = new ConceptSchema(LIBRO);
    add(libroSchema, ontologia.Libro.class);

    // adding AgentAction(s)
    AgentActionSchema rechazarPujaSchema = new AgentActionSchema(RECHAZARPUJA);
    add(rechazarPujaSchema, ontologia.RechazarPuja.class);
    AgentActionSchema iniciarPujaSchema = new AgentActionSchema(INICIARPUJA);
    add(iniciarPujaSchema, ontologia.IniciarPuja.class);
    AgentActionSchema aceptarPujaSchema = new AgentActionSchema(ACEPTARPUJA);
    add(aceptarPujaSchema, ontologia.AceptarPuja.class);
    AgentActionSchema informarSchema = new AgentActionSchema(INFORMAR);
    add(informarSchema, ontologia.Informar.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    libroSchema.add(LIBRO_TITULO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    rechazarPujaSchema.add(RECHAZARPUJA_PRECIOACTUAL, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    rechazarPujaSchema.add(RECHAZARPUJA_LIBRO, libroSchema, ObjectSchema.MANDATORY);
    iniciarPujaSchema.add(INICIARPUJA_PRECIOACTUAL, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    iniciarPujaSchema.add(INICIARPUJA_LIBRO, libroSchema, ObjectSchema.MANDATORY);
    aceptarPujaSchema.add(ACEPTARPUJA_PRECIOACTUAL, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    aceptarPujaSchema.add(ACEPTARPUJA_LIBRO, libroSchema, ObjectSchema.MANDATORY);
    informarSchema.add(INFORMAR_PRECIOACTUAL, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    informarSchema.add(INFORMAR_LIBRO, libroSchema, ObjectSchema.MANDATORY);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
}