package vendedor;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import ontologia.SubastaOntology;

public class AgenteVendedor extends Agent {

    //Interfaz gráfica del agente vendedor
    private PrincipalVendedor gui;
    private Ontology ontology;
    private Codec codec;

    @Override
    protected void setup() {
        //Se informa de que el agente vendedor esta listo
        System.out.println("El vendedor " + getAID().getLocalName() + " está listo.");
        
        ontology = SubastaOntology.getInstance();
        codec = new SLCodec();
        getContentManager().registerOntology(ontology);
        getContentManager().registerLanguage(codec);
        
        //Se inicia la gui
        gui = PrincipalVendedor.getInstance(this);
        gui.setVisible(true);
    }

    @Override
    protected void takeDown() {
        //Se cierra la gui
        gui.dispose();
        
        //Se informa de que esta cerrando el agente
        System.out.println("El agente vendedor " + getAID().getLocalName() + " está terminando");
    }

    /* Función para iniciar el comportamiento que realiza la subasta 
     * libro El libro que se va a subastar
     * precio El precio por el que se inicia la subasta
     * incremento Lo que se incrementa el precio en cada puja
    */
    public void iniciarSubasta(String libro, double precio, double incremento) {
        addBehaviour(new VendedorComportamiento(this, libro, precio, incremento));
    }

    public PrincipalVendedor getGui() {
        return gui;
    }
}
