package com.example.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends ServidorController implements Runnable{

    private String name;
    private static DataInputStream dis;
    private static DataOutputStream dos;
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
                    this.name=nomeJogador;
                    Jogador novoJogador = new Jogador(nomeJogador);
                    ServidorController.Jogadores.add(novoJogador);

                    enviarListaJogadores();
                }

                if(recebido.startsWith("qs")){

                    System.out.println(recebido + " : -- servidor recebeu");

                    // Respondendo ao cliente
                    dos.writeUTF(recebido + "enviado");
                    dos.flush();


                    dos.writeUTF("...----..." + this.name + ": " + recebido);
                    dos.flush();

                    for(ClientHandler mc: ServidorController.ar){


                            mc.dos.writeUTF(this.name + ": " + recebido);

                    }

                    index++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


