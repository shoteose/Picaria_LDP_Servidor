package com.example.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends ServidorController implements Runnable{

    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
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
        String recebido;

        while(true){
            try {
                recebido = dis.readUTF();

                System.out.println(recebido);

                if(recebido.endsWith("logout")){
                    this.isloggedin = false;
                    this.s.close();
                    break; // while
                }

                for(ClientHandler mc: ServidorController.ar){

                    if(recebido.startsWith("n")) {

                        System.out.println(recebido);
                                String[] arrOfStr = recebido.split(":");
                                for (String a : arrOfStr) {
                                    System.out.println(a);
                                }
                                Jogador b = new Jogador(arrOfStr[1]);

                                mandarParaFeedBack( recebido + " -- " + arrOfStr[1]);
                               Jogadores.add(b);




                    }
                    else {
                        mc.dos.writeUTF(this.name + ": " + recebido);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}


