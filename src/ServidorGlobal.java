//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.net.*;
import java.io.*;
import java.util.*;


public class ServidorGlobal {

    static Vector<ClientHandler> ar = new Vector<>();
    static int i = 0;

    public static int index = 0;

    public static void main(String[] args) {
// TODO Auto-generated method stub
        try {
            System.out.println("Servidor aceita conexões.");
            ServerSocket ss = new ServerSocket(1111);

            Socket s;
            while (true) {
                s = ss.accept();
                System.out.println("Novo client recebido : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                ClientHandler mtch = new ClientHandler(s, "client " + i, dis, dos);
                Thread t = new Thread(mtch);

                System.out.println("Adiciona cliente " + i + " à lista ativa.");
                ar.add(mtch);
                t.start();

                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static class ClientHandler extends ServidorGlobal implements Runnable {
        private String name;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;
        boolean isloggedin;

        boolean pingpong= false;

        private ClientHandler(Socket s, String string,
                              DataInputStream dis, DataOutputStream dos) {
            this.s = s;
            this.dis = dis;
            this.dos = dos;
            this.name = string;
            this.isloggedin = true;
        }

        @Override
        public void run() {
            String recebido;

            while (isloggedin) {
                try {
                    recebido = dis.readUTF();
                    System.out.println(recebido);

                    if (recebido.equals("logout")) {
                        this.isloggedin = false;
                        this.s.close();
                        break; // while
                    }

                    if (recebido.startsWith("nome:")) {
                        String[] partes = recebido.split(":");
                        String nomeJogador = partes[1];// Remove o prefixo "nome:"
                        this.name=nomeJogador;


                        //dos.writeUTF(this.name + ": " + recebido);




                    }

                    if(recebido.startsWith("qs")){

                        System.out.println("Antes de enviar este foi o " + index + "do cliente " + this.name);
                        dos.writeUTF("qs:" + index);
                        dos.flush();

                        index++;
                        System.out.println("Depois de enviar este é o index " + index + "do cliente " + this.name);
                    }

                    if(recebido.startsWith("P1")){

                        System.out.println(this.name + ": Jogou esta jogada = " + recebido);

                        //  do{

                        for(ClientHandler mc : ServidorGlobal.ar){

                            if(this.name.equals(mc.name) ){}else{

                                mc.dos.writeUTF((recebido));
                                mc.dos.flush();
                                System.out.println("enviei a dizer que recebeu jogada" + mc.name);


                            }
                        }

                        //  }while(pingpong == false);
                    }

                    if(recebido.startsWith("P2")){

                        System.out.println(this.name + ": Jogou esta jogada = " + recebido);

                        for(ClientHandler mc : ServidorGlobal.ar){

                            if(this.name.equals(mc.name) ){}else{

                                mc.dos.writeUTF((recebido));
                                mc.dos.flush();
                                System.out.println("enviei a dizer que recebeu jogada" + mc.name);


                            }
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
