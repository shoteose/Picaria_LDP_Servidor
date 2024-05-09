package com.example.servidor;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.io.*;
import java.net.*;
import java.util.*;


public class ServidorController implements Initializable{
    @FXML
    private TextField inputPorta;

    public String feedBack;

    @FXML
    private Text taON;

    @FXML
    private TextArea feedback;

    @FXML
    private ListView listJogadores;

    public static LinkedList<Jogador> Jogadores = new LinkedList<Jogador>();

    static Vector<ClientHandler> ar = new Vector<>();
    static int i = 0;
    private ServerSocket ss;
    private boolean servidorTaOn = true;

    int cont = 0;

    public static int index = 0;



    @FXML
    private void iniciarServidor(ActionEvent event) {
        try {
            ss = new ServerSocket(Integer.parseInt(inputPorta.getText()));
            mandarParaFeedBack("Servidor iniciado na porta: " + Integer.parseInt(inputPorta.getText()));
            mudarCorOn(true);

            // Thread para aceitar Clientes
            Thread threadAceitarClientes = new Thread(this::aceitarClientes);
            threadAceitarClientes.start();
        } catch (IOException e) {
            mandarParaFeedBack("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    @FXML
    private void fecharServidor(ActionEvent event) {
        try {
            servidorTaOn = false;
            ss.close();
            mandarParaFeedBack("Servidor encerrado");
            mudarCorOn(false);
        } catch (IOException e) {
            mandarParaFeedBack("Erro ao encerrar o servidor: " + e.getMessage());
        }
    }

    @FXML
    private void pausarServidor(ActionEvent event) {

        if(cont == 0){
            servidorTaOn= false;
            cont = 1;
            mudarCorOn(false);
            mandarParaFeedBack("Servidor parado, nao recebe mais clientes");
        } else if (cont == 1){

            servidorTaOn= true;
            cont = 0;
            mudarCorOn(true);
            mandarParaFeedBack("Servidor voltou a aceitar clientes");
        }


    }

    private void aceitarClientes() {
        try {
            while (servidorTaOn) {

                Socket s = ss.accept();

                mandarParaFeedBack("Cliente conectado: " + s.getRemoteSocketAddress());

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                ClientHandler clientHandler = new ClientHandler(s, dis, dos);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            mandarParaFeedBack("Erro ao aceitar conexão de cliente: " + e.getMessage());
        }
    }

    public void mandarParaFeedBack(String mensagem) {
        Platform.runLater(() -> feedback.appendText(mensagem + "\n"));
    }

    private void mudarCorOn(boolean ligou) {

        if (ligou) {

            Platform.runLater(() -> mudaTexto(true));

        }else{
            Platform.runLater(() -> mudaTexto(false));

        }
    }

    private void mudaTexto(boolean ligou){

        if(ligou){

            this.taON.setText("ON");
            this.taON.setFill(Color.GREEN);

        }else{

            this.taON.setText("OFF");
            this.taON.setFill(Color.RED);

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(Jogador qwe: Jogadores){

            String listViewHighScore = qwe.getNome() + " : " + qwe.getCounterWins();
            this.listJogadores.getItems().add(listViewHighScore);

        }
    }

    @FXML
    public void atualizar() {



        for(Jogador lol: Jogadores){

            this.listJogadores.getItems().removeAll();

        }

        for(Jogador qwe: Jogadores){
            String listViewHighScore = qwe.getNome() + " : " + qwe.getCounterWins();
            this.listJogadores.getItems().add(listViewHighScore);

        }
    }
}


