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


public class ServidorController{
    @FXML
    private TextField inputPorta;

    public String feedBack;

    @FXML
    private Text taON;

    @FXML
    private TextArea feedback;

    @FXML
    private ListView listOnline;

    public static LinkedList<Jogador> Jogadores = new LinkedList<Jogador>();

    static Vector<ClientHandler> ar = new Vector<>();
    static int i = 0;
    private ServerSocket ss;
    private boolean servidorTaOn = true;

    @FXML
    private void iniciarServidor(ActionEvent event) {
        try {
            ss = new ServerSocket(Integer.parseInt(inputPorta.getText()));
            mandarParaFeedBack("Servidor iniciado na porta: " + Integer.parseInt(inputPorta.getText()));
            mudarCorOn();

            // Thread para aceitar Clientes
            Thread acceptClientsThread = new Thread(this::aceitarClientes);
            acceptClientsThread.start();
        } catch (IOException e) {
            mandarParaFeedBack("Erro ao iniciar o servidor: " + e.getMessage());
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

    private void mandarParaFeedBack(String mensagem) {
        Platform.runLater(() -> feedback.appendText(mensagem + "\n"));
    }

    private void mudarCorOn(){
        Platform.runLater(() -> mudaTexto());

    }

    private void mudaTexto(){
        this.taON.setText("ON");
        this.taON.setFill(Color.GREEN);
    }

}


