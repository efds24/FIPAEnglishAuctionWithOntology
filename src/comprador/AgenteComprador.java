package comprador;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import ontologia.SubastaOntology;

public class AgenteComprador extends Agent {

    //Interfaz gráfica del agente vendedor
    private PrincipalComprador gui;
    
    private Ontology ontology;
    private Codec codec;

    public PrincipalComprador getGui() {
        return gui;
    }

    @Override
    protected void setup() {
        //El agente se inicia
        System.out.println("El comprador " + getAID().getLocalName() + " está listo.");
        
        
        //Se abre su GUI
        gui = new PrincipalComprador(this);
        gui.setVisible(true);

        //El agente se registra en el servicio de paginas amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("compra-libros");
        sd.setName("JADE-subasta");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        ontology = SubastaOntology.getInstance();
        codec = new SLCodec();
        getContentManager().registerOntology(ontology);
        getContentManager().registerLanguage(codec);
        
        //Se inicia el comportamiento
        addBehaviour(new CompradorComportamiento(this));

    }

    @Override

    protected void takeDown() {
        //Se desregistra del servicio de paginas amarillas
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    javax.swing.JOptionPane.showMessageDialog(null, "Error al borrarse del servicio de páginas amarillas", getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        //Se cierra la gui
        gui.dispose();

        //Se informa de que se está cerrando
        System.out.println("El agente comprador " + getAID().getLocalName() + " está terminando");
    }
}
