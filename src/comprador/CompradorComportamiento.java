package comprador;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.AceptarPuja;
import ontologia.Informar;
import ontologia.IniciarPuja;
import ontologia.Libro;
import ontologia.Pujar;
import ontologia.RechazarPuja;
import ontologia.SubastaOntology;

public class CompradorComportamiento extends CyclicBehaviour {

    private Ontology onto;
    private Codec codec;

    public CompradorComportamiento(Agent agent) {
        super(agent);
        onto = SubastaOntology.getInstance();
        codec = new SLCodec();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg == null) {
            block();
        } else {
            switch (msg.getPerformative()) {
                case ACLMessage.CFP: {
                    try {
                        Action a = (Action) myAgent.getContentManager().extractContent(msg);
                        IniciarPuja o = (IniciarPuja) a.getAction();

                        String libro = o.getLibro().getTitulo();
                        Libro l = new Libro();
                        l.setTitulo(libro);
                        double precioActual = o.getPrecioActual();

                        ACLMessage reply = msg.createReply();
                        reply.setOntology(onto.getName());
                        reply.setLanguage(codec.getName());
                        if (((AgenteComprador) myAgent).getGui().getLibros().containsKey(libro)) {
                            double precioMaximo = ((AgenteComprador) myAgent).getGui().getLibros().get(libro);
                            if (precioMaximo >= precioActual) {
                                Pujar p = new Pujar();
                                p.setLibro(l);
                                p.setPujar(true);

                                try {
                                    Action action = new Action(myAgent.getAID(), p);
                                    myAgent.getContentManager().fillContent(reply, action);
                                } catch (Codec.CodecException | OntologyException ex) {
                                    ex.printStackTrace();
                                }

                                reply.setPerformative(ACLMessage.PROPOSE);
                            } else {

                                Pujar p = new Pujar();
                                p.setLibro(l);
                                p.setPujar(false);

                                try {
                                    Action action = new Action(myAgent.getAID(), p);
                                    myAgent.getContentManager().fillContent(reply, action);
                                } catch (Codec.CodecException | OntologyException ex) {
                                    ex.printStackTrace();
                                }

                                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                java.awt.EventQueue.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        javax.swing.JOptionPane.showMessageDialog(null, "El precio actual de la subasta de " + libro + " supera tu precio máximo\nHas salido de la subasta", myAgent.getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                    }
                                });
                                //Quitar de la tabla de subastas activas
                                ((AgenteComprador) myAgent).getGui().rmFilaSubastasActivas(libro);
                            }
                        } else {

                            Pujar p = new Pujar();
                            p.setLibro(l);
                            p.setPujar(true);

                            try {
                                Action action = new Action(myAgent.getAID(), p);
                                myAgent.getContentManager().fillContent(reply, action);
                            } catch (Codec.CodecException | OntologyException ex) {
                                ex.printStackTrace();
                            }

                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                            reply.setContent("not-interested");
                        }
                        myAgent.send(reply);

                    } catch (CodecException | OntologyException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case ACLMessage.ACCEPT_PROPOSAL: {
                    try {
                        Action a = (Action) myAgent.getContentManager().extractContent(msg);
                        AceptarPuja o = (AceptarPuja) a.getAction();

                        String libro = o.getLibro().getTitulo();
                        String precio = String.valueOf(o.getPrecioActual());
                        ((AgenteComprador) myAgent).getGui().addFilaSubastasActivas(libro, precio, Boolean.TRUE);
                        break;
                    } catch (CodecException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OntologyException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case ACLMessage.INFORM: {
                    try {
                        Action a = (Action) myAgent.getContentManager().extractContent(msg);
                        Informar o = (Informar) a.getAction();

                        String libro = o.getLibro().getTitulo();
                        
                        //Avisar de que se ha terminado la puja
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                javax.swing.JOptionPane.showMessageDialog(null, "No has ganado la subasta de " + libro, myAgent.getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            }
                        });

                        
                        ((AgenteComprador) myAgent).getGui().rmFilaSubastasActivas(libro);
                        break;
                    } catch (CodecException | OntologyException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case ACLMessage.REQUEST: {
                    try {
                        //Avisar de que eres el ganador
                        Action a = (Action) myAgent.getContentManager().extractContent(msg);
                        Informar o = (Informar) a.getAction();

                        String libro = o.getLibro().getTitulo();
                        String precio = String.valueOf(o.getPrecioActual());
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                javax.swing.JOptionPane.showMessageDialog(null, "Has ganado la subasta de " + libro, myAgent.getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                        ((AgenteComprador) myAgent).getGui().rmFilaSubastasActivas(libro);
                        ((AgenteComprador) myAgent).getGui().rmLibro(libro);
                        ((AgenteComprador) myAgent).getGui().addFilaSubastasGanadas(libro, precio);
                        break;
                    } catch (CodecException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OntologyException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                case ACLMessage.REJECT_PROPOSAL: {
                    try {
                        Action a = (Action) myAgent.getContentManager().extractContent(msg);
                        RechazarPuja o = (RechazarPuja) a.getAction();

                        String libro = o.getLibro().getTitulo();
                        String precio = String.valueOf(o.getPrecioActual());
                        ((AgenteComprador) myAgent).getGui().addFilaSubastasActivas(libro, precio, Boolean.FALSE);
                        break;
                    } catch (CodecException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OntologyException ex) {
                        Logger.getLogger(CompradorComportamiento.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                default:
                    break;
            }
        }

    }

}
