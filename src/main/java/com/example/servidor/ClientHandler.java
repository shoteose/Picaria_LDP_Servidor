package com.example.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends ServidorController implements Runnable{

    private String name;
    private DataInputStream dis;
    private DataOutputStream dos;
    Socket s;

    private Jogador jogador;
    boolean isloggedin;


    String recebeu;

    public ClientHandler(Socket s,DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.isloggedin = true;
    }

    @Override
    public void run() {

        while (isloggedin){

            try {
                dis = new DataInputStream(s.getInputStream());

                String recebido = dis.readUTF();

                if (recebido.startsWith("nome:")) {
                    String[] partes = recebido.split(":");
                    System.out.println(partes[1]);
                    String nomeJogador = partes[1];// Remove o prefixo "nome:"
                    Jogador novoJogador = new Jogador(nomeJogador);
                    ServidorController.Jogadores.add(novoJogador);

                    enviarListaJogadores();
                }

                if(recebido.startsWith("qs")){

                    System.out.println(recebido + "recebeu");



                        dos.writeUTF(recebido + "enviado");
                        dos.flush();




                    index++;

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }




       /* String recebido;

        while(true){
            try {
                recebido = dis.readUTF();
                System.out.println("Received: " + recebido);

                System.out.println(recebido);

                if(recebido.endsWith("logout")){
                    this.isloggedin = false;
                    this.s.close();
                    break; // while
                }




                    if(recebido.startsWith("n")) {

                        System.out.println(recebido + " lolol");

                        String[] parts = recebido.split(":");
                        if (parts.length >= 2) {
                            System.out.println("Prefix: " + parts[0]);
                            System.out.println("Name: " + parts[1]);
                        } else {
                            System.out.println("Invalid format received.");
                        }

                            Jogador b = new Jogador(parts[1]);
                                mandarParaFeedBack(recebido);
                               Jogadores.add(b);


                    }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
*/
    }

    private void enviarListaJogadores() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        synchronized(ServidorController.Jogadores) {
            oos.writeObject(ServidorController.Jogadores); // Enviar a lista atualizada de jogadores
        }
        oos.flush();
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}


