import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;
import java.util.ServiceConfigurationError;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Labirinto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Defina o tamanho da janela
        int largura = 800;
        int altura = 600;
        frame.setSize(largura, altura);

        // Crie uma instância do labirinto
        int tamanhoLabirinto = 20; // Tamanho do labirinto (exemplo: 10x10)
        int numRatos = 10; // Número de ratos
        Labirinto labirinto = new Labirinto(frame.getContentPane(), "rato.png", tamanhoLabirinto, numRatos);
        frame.add(labirinto);



        // Exiba o JFrame
        frame.setVisible(true);



    }
}