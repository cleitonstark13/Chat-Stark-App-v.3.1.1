
/*
 * UNIVERSIDADE ESTADUAL DE GOIÁS
 * PROF: MÁRCIO
 * ALUNO: CLEITON ALVES E SILVA JÚNIOR
 * CURSO: SISTEMAS DE INFORMAÇÃO
 * DISCIPLINA: PROGRAMAÇÃO ORIENTADA A OBJETO II
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ServidorStark extends Thread {
    private static Map<String, PrintStream> mapClientes;
    private Socket conexao;
    private String nomeCliente;
    private static List<String> listaNomes = new ArrayList<String>();

    public ServidorStark(Socket socket) {
        this.conexao = socket;
    }

    /*
     * ARMAZENA O NOME DO
     * USUÁRIO EM UMA LISTA
     */

    public boolean armazena(String newName) {
        for (int i = 0; i < listaNomes.size(); i++) {
            if (listaNomes.get(i).equals(newName))
                return true;
        }
        listaNomes.add(newName);
        return false;
    }

    /*
     * REMOVE O NOME DO
     * USUÁRIO EM UMA LISTA
     */

    public void remove(String oldName) {
        for (int i = 0; i < listaNomes.size(); i++) {
            if (listaNomes.get(i).equals(oldName))
                listaNomes.remove(oldName);
        }
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            this.nomeCliente = entrada.readLine();
            if (armazena(this.nomeCliente)) {
                JOptionPane.showMessageDialog(null, "Usuário já cadastrado", "ERRO", JOptionPane.INFORMATION_MESSAGE);
                this.conexao.close();
                return;
            } else {
                // mostra o nome do cliente conectado ao ServidorStark
                System.out.println(this.nomeCliente + " : Conectado ao ServidorStark!");
                saida.print("Conectados: " + this.nomeCliente + " - ");
                String s = "";
                for (String aux : listaNomes) {
                    if (!aux.equalsIgnoreCase(this.nomeCliente)) {
                        s = s + aux + " - ";
                    }
                }
                /*
                 * Quando o cliente se conectar recebe todos que estao conectados
                 */
                saida.println(s);
                saida.println("--------------------------------------------------------------------");

                /*
                 * Envia lista para todos assim que qualquer cliente se conecta
                 */
                sendListToAll(this.nomeCliente);
            }

            if (this.nomeCliente == null) {
                this.conexao.close();
                return;
            }
            /*
             * Adiciona os dados de saida do cliente no objeto mapClientes;
             * A chave sera o nome e valor o printstream;
             */
            mapClientes.put(this.nomeCliente, saida);

            String[] msg = entrada.readLine().split("@");
            while (msg != null && !(msg[0].trim().equals(","))) {
                send(saida, " \n ", msg);
                msg = entrada.readLine().split("@");
            }
            System.out.println(this.nomeCliente + " saiu do bate-papo!");
            String[] out = { " do bate-papo!" };
            send(saida, " saiu", out);
            remove(this.nomeCliente);

            mapClientes.remove(this.nomeCliente);

            this.conexao.close();

        } catch (IOException e) {
            System.out.println("Chat do/a usuário/a " + this.nomeCliente + " fechou!!!");
            String[] out = { " saiu do bate-papo!" };
            send(null, "", out);
            remove(this.nomeCliente);
            mapClientes.remove(this.nomeCliente);
            System.out.println(this.nomeCliente + " removido do chat!!!");
        }
    }

    /*
     * Se o array da msg tiver tamanho igual a 1, entao envia para todos
     * Se o tamanho for 2, envia apenas para o cliente escolhido
     */
    public void send(PrintStream saida, String acao, String[] msg) {
        out: for (Map.Entry<String, PrintStream> cliente : mapClientes.entrySet()) {
            PrintStream chat = cliente.getValue();
            if (chat != saida) {
                if (msg.length == 1) {
                    chat.println(this.nomeCliente + acao + msg[0]);
                } else {
                    if (msg[1].equalsIgnoreCase(cliente.getKey())) {
                        chat.println(this.nomeCliente + acao + msg[0]);
                        break out;
                    }
                }
            }
        }
    }

    /*
     * Lista de usuários que
     * entraram no Chat Stark
     */

    public void sendListToAll(String nome) {
        for (Map.Entry<String, PrintStream> cliente : mapClientes.entrySet()) {
            if (!cliente.getKey().equalsIgnoreCase(nome)) {
                String aux = "";
                for (String s : listaNomes) {
                    if (!s.equalsIgnoreCase(cliente.getKey())) {
                        aux = aux + s + " - ";
                    }
                }
                PrintStream chat = cliente.getValue();
                chat.println("Conectados: " + aux);
                chat.println("--------------------------------------------------------------------");
                chat.flush();
            }
        }
    }

    public static void main(String args[]) {
        mapClientes = new HashMap<String, PrintStream>();
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("Bem vindo ao Chat Stark");
            System.out.println("ServidorStark rodando na porta 8080");
            JLabel servidorLigado = new JLabel("SERVIDOR RODANDO NA PORTA 8080");  
            Object[] telaServidor = {servidorLigado};
            JOptionPane.showMessageDialog(null, telaServidor, "Servidor Stark", JOptionPane.INFORMATION_MESSAGE);
            
                while (true) {
                Socket conexao = server.accept();
                Thread t = new ServidorStark(conexao);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
