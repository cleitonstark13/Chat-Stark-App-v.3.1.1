/*
 * UNIVERSIDADE ESTADUAL DE GOIÁS
 * PROF: MÁRCIO
 * ALUNO: CLEITON ALVES E SILVA JÚNIOR
 * CURSO: SISTEMAS DE INFORMAÇÃO
 * DISCIPLINA: PROGRAMAÇÃO ORIENTADA A OBJETO II
*/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ChatStark extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JButton btnAjuda;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtNome;
    private JLabel NomeCaixa;
    private JLabel Informacoes;
    private JLabel NomeApresentacao;
    private JLabel Versao;
    private JLabel nomeERRO;

    /**
     * @throws IOException
     */

     /*
      * Tela Layout Chat Stark v3.1.1.1
      */
    public ChatStark() throws IOException {
        /*
         * ICON CHAT
         */

        ImageIcon imagemTituloJanela = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/icon.png");
        setIconImage(imagemTituloJanela.getImage());
        ImageIcon iconCadastro = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/iconCadastro.png");
        ImageIcon iconApresentacao = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/chatStark.png");

        /*
         * TELA
         * APRESENTAÇÃO
         */

        NomeApresentacao = new JLabel("Bem vindo ao Chat Stark");
        Versao = new JLabel("Versão v.3.1.1");
        Object[] telaApresentacao = { NomeApresentacao, Versao};
        JOptionPane.showMessageDialog(null, telaApresentacao, "Chat Stark Ltda. 2022 - v.3.1.1",
                JOptionPane.INFORMATION_MESSAGE, iconApresentacao);

        /*
         * CADASTRO
         * DO USUÁRIO
         * NO CHAT
         */

        NomeCaixa = new JLabel("Nome de usuário:");
        txtNome = new JTextField("");
        Object[] texts = {NomeCaixa, txtNome};
        UIManager.put("OptionPane.yesButtonText", "Entrar");
        UIManager.put("OptionPane.noButtonText", "Sair");
        int n = JOptionPane.showConfirmDialog(null, texts, "Cadastro de usuário", 
                JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE, iconCadastro);
        if(n == JOptionPane.NO_OPTION){
            System.exit(0);
        }
        /* PAINEL DO CHAT */
        JLabel imagemIcon = new JLabel(new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/chatLogo.png"));
        pnlContent = new JPanel();
        pnlContent.add(imagemIcon);
        texto = new JTextArea(20, 25);
        texto.setEditable(false);
        texto.setBackground(new Color(240, 240, 240));
        txtMsg = new JTextField(25);
        lblHistorico = new JLabel("CHAT");
        lblHistorico.setForeground(Color.WHITE);
        lblMsg = new JLabel("MENSAGEM");
        lblMsg.setForeground(Color.WHITE);
        ImageIcon enviarButton = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/iconEnviar.png");
        btnSend = new JButton("Enviar", enviarButton);
        btnSend.setToolTipText("Enviar Mensagem");
        ImageIcon sairButton = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/iconSair.png");
        btnSair = new JButton("Sair", sairButton);
        btnSair.setToolTipText("Sair do Chat");
        ImageIcon ajudaButton = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/iconAjuda.png");
        btnAjuda = new JButton("Ajuda", ajudaButton);
        btnAjuda.setToolTipText("Botão de ajuda (F1)");
        btnSend.addActionListener(this);
        btnSair.addActionListener(this);
        btnAjuda.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        Informacoes = new JLabel("© CHAT STARK v.3.1.1");
        Informacoes.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);	
        pnlContent.add(txtMsg);
        pnlContent.add(btnSend);
        pnlContent.add(btnSair);
        pnlContent.add(btnAjuda);
        pnlContent.add(Informacoes);
        Color MinhaCorAzulChatStark = new Color(0, 157, 223);
        pnlContent.setBackground(MinhaCorAzulChatStark);
        texto.setBorder(BorderFactory.createEtchedBorder(MinhaCorAzulChatStark, MinhaCorAzulChatStark));
        txtMsg.setBorder(BorderFactory.createEtchedBorder(MinhaCorAzulChatStark, MinhaCorAzulChatStark));
        setTitle(txtNome.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(310, 630);
        setVisible(true);
        setLocation(650, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /***
     * Método usado para conectar no server socket, retorna IO Exception caso dê
     * algum erro.
     * 
     * @throws IOException
     */
    public void conectar() throws IOException {
    	try {
        socket = new Socket("127.0.0.1", 8080);
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtNome.getText() + "\r\n");
        bfw.flush();
    	} catch (IOException e1) {
    		ImageIcon erroButton = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/iconErro.png");
    		nomeERRO = new JLabel("Chat não conectado, ServidorStark está desligado.");
    		UIManager.put("OptionPane.okButtonText", "Sair");
    	    Object[] telaErro = { nomeERRO};
            JOptionPane.showMessageDialog(null, telaErro, "ERRO",
                    JOptionPane.INFORMATION_MESSAGE, erroButton);
    		System.out.print("Servidor não conectado");
    		System.exit(0);
        }
    }
    
    /**
     * Método usado para receber mensagem do servidor
     * 
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void escutar() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Sair".equalsIgnoreCase(msg)) {

             if (bfr.ready()) { 
                msg = bfr.readLine(); 
                if (msg.equalsIgnoreCase("Sair")) {
                    System.out.println("---------------------------------------------");
                    texto.append("Servidor caiu! \r\n");
                    System.out.println("---------------------------------------------");
                } else {
                    texto.append(msg + "\r\n");
                }
            }
        }
    }

    /***
     * Método usado para enviar mensagem para o server socket
     * 
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void enviarMensagem(String msg) throws IOException {

        if (msg.equalsIgnoreCase("Sair")) {
            sair();
        } else {
            /*
             * CAIXA
             * DE TEXTO CHAT
             */
            bfw.write(msg + "\r\n");
            texto.append(txtNome.getText() + " \n " + txtMsg.getText() + "\r\n");

        }
        bfw.flush();
        txtMsg.setText("");
    }

   

    public void ajuda() throws IOException {

        ImageIcon imagemAjuda = new ImageIcon("C:/Users/cjunior/Downloads/Chat Stark App v.3.1.1/ChatStark/src/com/cleitonstark/app/iconAjudaTOP.png");
        JLabel JL01 = new JLabel("1) BOTÃO ENVIAR - ENVIA A MENSAGEM");
        JLabel JL02 = new JLabel("2) BOTÃO SAIR - FECHA O CHAT");
        JLabel JL03 = new JLabel("3) ENVIAR MENSAGEM NO PRIVADO - COLOQUE A MENSAGEM SEGUIDA DO @NOME DO DESTINO QUE DESEJA ENVIAR A MENSAGEM");
        UIManager.put("OptionPane.okButtonText", "Fechar");
        Object[] ajudaTela = {
            JL01, JL02, JL03
        };
        JOptionPane.showMessageDialog(null, ajudaTela,"Tela de ajuda! (F1)", JOptionPane.DEFAULT_OPTION, imagemAjuda);
    }

    /***
     * Método usado quando o usuário clica em sair
     * 
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void sair() throws IOException {
        System.exit(0);
 }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand().equalsIgnoreCase(btnSend.getActionCommand()))
                enviarMensagem(txtMsg.getText());
            else if (e.getActionCommand().equalsIgnoreCase(btnSair.getActionCommand()))
                sair();
                else if (e.getActionCommand().equalsIgnoreCase(btnAjuda.getActionCommand()))
                ajuda();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /*
     * EVENTOS
     * TECLA ENTER, ESC, F1
     */

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                enviarMensagem(txtMsg.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_F1){
            try {
                ajuda();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            try {
                bfw.flush();
                txtMsg.setText("");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    public static void main(String[] args) throws IOException {

        ChatStark app = new ChatStark();
        app.conectar();
        app.escutar();
    }
}
