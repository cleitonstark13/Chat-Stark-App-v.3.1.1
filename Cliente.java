/*
 * UNIVERSIDADE ESTADUAL DE GOIÁS
 * PROF: MÁRCIO
 * ALUNO: CLEITON ALVES E SILVA JÚNIOR
 * CURSO: SISTEMAS DE INFORMAÇÃO
 * DISCIPLINA: PROGRAMAÇÃO ORIENTADA A OBJETO
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
import javax.swing.*;

public class Cliente extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    // private JTextArea listaPessoas;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    // private JLabel lbllistaPessoas;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    // private JTextField txtIP;
    // private JTextField txtPorta;
    private JTextField txtNome;
    // private JLabel IPCaixa;
    // private JLabel PortaCaixa;
    private JLabel NomeCaixa;
    private JLabel Informacoes;
    private JLabel NomeApresentacao;
    private JLabel Versao;

    /**
     * @throws IOException
     */
    public Cliente() throws IOException {
        /*
            * ICON 
            * CHAT
        */

        ImageIcon imagemTituloJanela = new ImageIcon("icon.png");
        setIconImage(imagemTituloJanela.getImage());
        ImageIcon iconCadastro = new ImageIcon("iconCadastro.png");
        ImageIcon iconApresentacao = new ImageIcon("chatStark.png");

        /* 
            * TELA
            * APRESENTAÇÃO
        */
        NomeApresentacao = new JLabel("Bem vindo ao Chat Stark");
        Versao = new JLabel("Versão v.10.7");
        Object[] telaApresentacao = { NomeApresentacao, Versao};
        JOptionPane.showMessageDialog(null, telaApresentacao, "Chat Stark Ltda. 2022 - v.1.10.7", JOptionPane.INFORMATION_MESSAGE, iconApresentacao);
       
          /* 
            * CADASTRO 
            * DO USUÁRIO 
            * DO CHAT 
        */

          /* 
            * IPCaixa = new JLabel("IP:");
            * txtIP = new JTextField("127.0.0.1");
            * PortaCaixa = new JLabel("Porta:");
            * txtPorta = new JTextField("8080");
        */
        NomeCaixa = new JLabel("Nome de usuário:");
        txtNome = new JTextField("");
        Object[] texts = { 
                            /* 
                             * IPCaixa, 
                             * txtIP, 
                             * PortaCaixa, 
                             * txtPorta, 
                             */
                            NomeCaixa, txtNome 
                         };
        JOptionPane.showMessageDialog(null, texts, "Cadastro de usuário", JOptionPane.INFORMATION_MESSAGE, iconCadastro);

        /* PAINEL DO CHAT */
        JLabel imagemIcon = new JLabel(new ImageIcon("chatLogo.png"));
        pnlContent = new JPanel();
        pnlContent.add(imagemIcon);
        /*
            * listaPessoas = new JTextArea(5, 20);
            * listaPessoas.setEditable(false);
            * listaPessoas.setBackground(new Color(240, 240, 240)); 
        */
        texto = new JTextArea(15, 25);
        texto.setEditable(false);
        texto.setBackground(new Color(240, 240, 240));
        txtMsg = new JTextField(25);
        /*
            * lbllistaPessoas = new JLabel("CONECTADOS");
            * lbllistaPessoas.setForeground(Color.WHITE);
        */
        lblHistorico = new JLabel("CHAT");
        lblHistorico.setForeground(Color.WHITE);
        lblMsg = new JLabel("MENSAGEM");
        lblMsg.setForeground(Color.WHITE);
        ImageIcon enviarButton = new ImageIcon("iconEnviar.png");
        btnSend = new JButton("Enviar", enviarButton);
        btnSend.setToolTipText("Enviar Mensagem");
        ImageIcon sairButton = new ImageIcon("iconSair.png");
        btnSair = new JButton("Sair", sairButton);
        btnSair.setToolTipText("Sair do Chat");
        btnSend.addActionListener(this);
        btnSair.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        Informacoes = new JLabel("© CHAT STARK v.10.7");
        Informacoes.setForeground(Color.WHITE);
        /*
            * JScrollPane scrollLista = new JScrollPane(listaPessoas);
        */
        JScrollPane scroll = new JScrollPane(texto);
        /* 
            * listaPessoas.setLineWrap(true);
        */
        texto.setLineWrap(true);
        /* 
            * pnlContent.add(lbllistaPessoas);
            * pnlContent.add(scrollLista);
        */
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnSend);
        pnlContent.add(btnSair);
        pnlContent.add(Informacoes);
        Color MinhaCor = new Color(0, 157, 223);
        pnlContent.setBackground(MinhaCor);
        texto.setBorder(BorderFactory.createEtchedBorder(MinhaCor, MinhaCor));
        /* 
            * listaPessoas.setBorder(BorderFactory.createEtchedBorder(MinhaCor, MinhaCor));
        */
        txtMsg.setBorder(BorderFactory.createEtchedBorder(MinhaCor, MinhaCor));
        setTitle(txtNome.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(310, 550);
        setVisible(true);
        setLocation(500, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    // PessoaChat pa;

    /***
     * Método usado para conectar no server socket, retorna IO Exception caso dê
     * algum erro.
     * 
     * @throws IOException
     */
    public void conectar() throws IOException {

        socket = new Socket("127.0.0.1", 8080);
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtNome.getText() + "\r\n");
        bfw.flush();
    }

    /***
     * Método usado para enviar mensagem para o server socket
     * 
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void enviarMensagem(String msg) throws IOException {

        if (msg.equalsIgnoreCase("Sair")) {
            bfw.write(txtNome.getText() + " saiu do bate-papo! \r\n");
            texto.append(txtNome.getText() + " saiu do bate-papo! \r\n");
            bfw.close();
            ouw.close();
            ou.close();
            socket.close();
        } else {
        /* --CAIXA DE TEXTO CHAT-- */
            bfw.write(msg + "\r\n");
            texto.append(txtNome.getText() + " \n " + txtMsg.getText() + "\r\n");
            
        }
        bfw.flush();
        txtMsg.setText("");
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

        while (!"Sair".equalsIgnoreCase(msg))

            if (bfr.ready()) {
                msg = bfr.readLine();
                if (msg.equalsIgnoreCase("Sair")){
                System.out.println("---------------------------------------------");
                texto.append("Servidor caiu! \r\n");
                System.out.println("---------------------------------------------");
                } else {
                texto.append(msg + "\r\n");
                }
            }
    }

    /***
     * Método usado quando o usuário clica em sair
     * 
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void sair() throws IOException {

        enviarMensagem("Sair");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand().equalsIgnoreCase(btnSend.getActionCommand()))
                enviarMensagem(txtMsg.getText());
            else if (e.getActionCommand().equalsIgnoreCase(btnSair.getActionCommand()))
                sair();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                enviarMensagem(txtMsg.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {

        if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE){
                try {
                    bfw.flush();
                    txtMsg.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    public static void main(String[] args) throws IOException {

        Cliente app = new Cliente();
        app.conectar();
        app.escutar();
    }
}