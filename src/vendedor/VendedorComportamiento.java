package vendedor;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.AceptarPuja;
import ontologia.Informar;
import ontologia.IniciarPuja;
import ontologia.Libro;
import ontologia.RechazarPuja;
import ontologia.SubastaOntology;




public class VendedorComportamiento extends TickerBehaviour {

    private String libro;
    private double precioActual;
    private double incremento;
    private ArrayList<AID> agentesCompradores;
    private MessageTemplate mt;
    private int step = 0;
    private AID comprador;
    private ArrayList<AID> compradoresEnPuja;
    private ArrayList<AID> histCompradores;
    private Ontology onto;
    private Codec codec;
    private Libro l;

    public VendedorComportamiento(Agent agente, String libro, double precioActual, double incremento) {
        super(agente, 5000);
        this.libro = libro;
        this.precioActual = precioActual;
        this.incremento = incremento;
        comprador = null;
        this.compradoresEnPuja = new ArrayList<>();
        histCompradores = new ArrayList<>();
        onto = SubastaOntology.getInstance();
        codec = new SLCodec();
        l = new Libro();
        l.setTitulo(libro);

    }

    @Override
    public void onTick() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("compra-libros");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            agentesCompradores = new ArrayList<>();
            for (int i = 0; i < result.length; i++) {
                agentesCompradores.add(result[i].getName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        switch (step) {
            case 0:
                //Mandar un cfp a todos los compradores
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                cfp.setOntology(onto.getName());
                cfp.setLanguage(codec.getName());

                IniciarPuja ip = new IniciarPuja();
                ip.setLibro(l);
                ip.setPrecioActual((float) precioActual);

                try {
                    Action action = new Action(myAgent.getAID(), ip);
                    myAgent.getContentManager().fillContent(cfp, action);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(VendedorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (AID a : agentesCompradores) {
                    cfp.addReceiver(a);
                }

                cfp.setConversationId("subasta");
                cfp.setReplyWith("cfp" + System.currentTimeMillis());
                myAgent.send(cfp);
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("subasta"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));

                //Almacena los compradores que están en este momento en la puja
                compradoresEnPuja = new ArrayList<>();
                step = 1;
                break;


            case 1:

                //Ver quien ha respondido que quiere pujar
                for (int i = 0; i < agentesCompradores.size(); i++) {
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.PROPOSE) {
                            compradoresEnPuja.add(reply.getSender());
                        }
                    }
                }

                //Si hay interesados
                if (!compradoresEnPuja.isEmpty()) {
                    //Fijas comprador al primero de la lista que acabas de recibir
                    comprador = compradoresEnPuja.get(0);
                    //Actualizas el historico de compradores
                    histCompradores.clear();
                    histCompradores.addAll(compradoresEnPuja);

                    //Modificas la tabla de subastas activas
                    ((AgenteVendedor) myAgent).getGui().modificarTablaSubastasActivas(libro, comprador.getLocalName(), String.valueOf(precioActual));
                }

                //Para cada comprador interesado que no vaya ganando se le manda un REJECT_PROPOSAL
                for (AID a : compradoresEnPuja) {
                    if (!a.equals(comprador)) {
                        ACLMessage rejectProposal = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                        rejectProposal.setOntology(onto.getName());
                        rejectProposal.setLanguage(codec.getName());
                        RechazarPuja rp = new RechazarPuja();
                        rp.setLibro(l);
                        rp.setPrecioActual((float) precioActual);

                        try {
                            Action action = new Action(myAgent.getAID(), rp);

                            myAgent.getContentManager().fillContent(rejectProposal, action);
                        } catch (Codec.CodecException c) {
                            c.printStackTrace();
                        } catch (OntologyException o) {
                            o.printStackTrace();
                        }
                        rejectProposal.addReceiver(a);
                        rejectProposal.setConversationId("subasta");
                        rejectProposal.setReplyWith("rejectProposal" + System.currentTimeMillis());
                        myAgent.send(rejectProposal);
                        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("subasta"), MessageTemplate.MatchInReplyTo(rejectProposal.getReplyWith()));
                    }
                }

                //Al que va ganando se le manda un ACCEPT_PROPOSAL
                ACLMessage acceptProposal = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                acceptProposal.setOntology(onto.getName());
                acceptProposal.setLanguage(codec.getName());
                AceptarPuja ap = new AceptarPuja();
                ap.setLibro(l);
                ap.setPrecioActual((float) precioActual);

                try {
                    Action action = new Action(myAgent.getAID(), ap);

                    myAgent.getContentManager().fillContent(acceptProposal, action);
                } catch (Codec.CodecException c) {
                    c.printStackTrace();
                } catch (OntologyException o) {
                    o.printStackTrace();
                }
                acceptProposal.addReceiver(comprador);
                acceptProposal.setConversationId("subasta");
                acceptProposal.setReplyWith("acceptProposal" + System.currentTimeMillis());
                myAgent.send(acceptProposal);
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("subasta"), MessageTemplate.MatchInReplyTo(acceptProposal.getReplyWith()));

                if (compradoresEnPuja.size() > 1) {
                    precioActual += incremento;
                    step = 0;
                } else if (compradoresEnPuja.size() == 1) {
                    comprador = compradoresEnPuja.get(0);
                    step = 2;
                } else if (compradoresEnPuja.isEmpty()) {
                    if (comprador == null) {
                        step = 3;
                    } else {
                        precioActual -= incremento;
                        compradoresEnPuja = histCompradores;
                        step = 2;
                    }
                }
                break;
            case 2:
                while (!agenteEnPaginasAmarillas(comprador) && step != 3) {
                    compradoresEnPuja.remove(0);
                    if (!compradoresEnPuja.isEmpty()) {
                        comprador = compradoresEnPuja.get(0);
                    } else {
                        step = 3;
                    }
                }

                //Avisas a todos los que quedan en la puja de que perdieron
                for (AID a : compradoresEnPuja) {
                    if (!a.equals(comprador) && agenteEnPaginasAmarillas(a)) {
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.setOntology(onto.getName());
                        inform.setLanguage(codec.getName());
                        Informar inf = new Informar();
                        inf.setLibro(l);
                        inf.setPrecioActual((float) precioActual);

                        try {
                            Action action = new Action(myAgent.getAID(), inf);

                            myAgent.getContentManager().fillContent(inform, action);
                        } catch (Codec.CodecException | OntologyException c) {
                            c.printStackTrace();
                        }
                        inform.addReceiver(a);
                        inform.setConversationId("subasta");
                        inform.setReplyWith("inform" + System.currentTimeMillis());
                        myAgent.send(inform);
                    }
                }

                //Avisas al ganador
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.setOntology(onto.getName());
                request.setLanguage(codec.getName());
                Informar inf = new Informar();
                inf.setLibro(l);
                inf.setPrecioActual((float) precioActual);

                try {
                    Action action = new Action(myAgent.getAID(), inf);

                    myAgent.getContentManager().fillContent(request, action);
                } catch (Codec.CodecException | OntologyException c) {
                    c.printStackTrace();
                }
                request.addReceiver(comprador);
                request.setConversationId("subasta");
                request.setReplyWith("request" + System.currentTimeMillis());
                myAgent.send(request);
                //Avisar de que se ha terminado la subasta
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        javax.swing.JOptionPane.showMessageDialog(null, "Subasta de " + libro + " finalizada\nPrecio de venta: " + precioActual + "\nComprador: " + comprador.getLocalName(), "INFORMACIÓN", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                //borrarlo de la tabla de subastas activas
                ((AgenteVendedor) myAgent).getGui().rmFilaTablaSubastasActivas(libro);
                //meterlo en la tabla de subastas finalizadas
                ((AgenteVendedor) myAgent).getGui().addFilaTablaSubastasFinalizadas(libro, String.valueOf(precioActual), comprador.getLocalName());
                stop();
                break;

            case 3:
                //Avisar de que nadie a entrado en la subasta
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        javax.swing.JOptionPane.showMessageDialog(null, "Nadie está interesado en el libro", "INFORMACIÓN", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                //Eliminarlo de la tabla de subastas activas
                ((AgenteVendedor) myAgent).getGui().rmFilaTablaSubastasActivas(libro);
                //Meterlo en la tabla de subastas finalizadas
                ((AgenteVendedor) myAgent).getGui().addFilaTablaSubastasFinalizadas(libro, " ", "Sin ganador");
                stop();
                break;

        }
    }

    public boolean agenteEnPaginasAmarillas(AID agente) {
        DFAgentDescription ag = new DFAgentDescription();
        boolean encontrado = false;
        ag.setName(agente);

        try {
            DFAgentDescription[] resultados = DFService.search(myAgent, ag);
            if (resultados.length > 0) {
                if (agente.equals(resultados[0].getName())) {
                    encontrado = true;
                }
            }
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

        return encontrado;

    }

}
