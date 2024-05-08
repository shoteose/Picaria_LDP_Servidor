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

                        System.out.println(recebido + " lolol");

                                String[] arrOfStr = recebido.trim().split(":");

                                System.out.println("Prefix: " + arrOfStr[0]);
                                System.out.println("Name: " + arrOfStr[1]);

                            Jogador b = new Jogador(arrOfStr[1]);
                                mandarParaFeedBack(recebido);
                                mandarParaFeedBack( recebido + " -- " + arrOfStr[1]);
                               Jogadores.add(b);
                                mc.dos.writeUTF("toma de volta");

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


