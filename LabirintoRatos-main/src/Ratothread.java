import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Ratothread extends Thread {
    private int x, y;
    public int tes,test;
    private int tamanho;
    private JPanel panel;
    private Set<Posicao> posicoesVisitadas = new HashSet<>(); // Manter o controle de posições visitadas

    private Labirinto labirintoRef;

    public Ratothread(int x, int y, int tamanho, Labirinto labirinto, JPanel jPanel) {
        this.x = x;
        this.y = y;
        this.tamanho = tamanho;
        this.labirintoRef = labirinto;
        this.panel = jPanel;
    }

    @Override
    public synchronized void run() {
 boolean move = true;
        while (move) {
 // Gere uma lista de movimentos possíveis
            int[] movimentos = {0, 1, 2, 3}; // 0 = cima, 1 = baixo, 2 = esquerda, 3 = direita
            shuffleArray(movimentos);

            boolean moveu = false;
            for (int movimento : movimentos) {

                int novoX = x;
                int novoY = y;
                if (labirintoRef.getPosicao(novoX,novoY) == 4){
                    labirintoRef.setPosicao(x, y, 1); // Marque a posição anterior como visitada
                }// n funciona , marcar posicao que passou como 1

                if (x == labirintoRef.getQueijoX() && y == labirintoRef.getQueijoY() || labirintoRef.getPosicao(x, y) == 2) {
                    System.out.println("Rato encontrou o queijo!");
                    x = labirintoRef.getQueijoX();
                    y = labirintoRef.getQueijoY();
                    labirintoRef.setPosicao(x, y, 2); // Atualize a posição do rato para a posição do queijo

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            panel.repaint();  // Atualize o JPanel para refletir as mudanças
                        }
                    });
                    moveu = true;
                    move = false;
                    Ratothread.interrupted();

                    break;
                }
// Se movimento for igual a 0, mova o rato para a esquerda (decrementa novoX em 1)
                if (movimento == 0) novoX--;

// Se movimento for igual a 1, mova o rato para a direita (incrementa novoX em 1)
                if (movimento == 1) novoX++;

// Se movimento for igual a 2, mova o rato para cima (decrementa novoY em 1)
                if (movimento == 2) novoY--;

// Se movimento for igual a 3, mova o rato para baixo (incrementa novoY em 1)
                if (movimento == 3) novoY++;

                // Verifique se a nova posição é válida
                if (ePosicaoValida(novoX, novoY)) {
                    if (labirintoRef.getPosicao(novoX,novoY) == 4){
                        labirintoRef.setPosicao(x, y, 1); // Marque a posição anterior como visitada
                    }// n funciona
                    if (labirintoRef.getPosicao(x,y) == 4){
                        labirintoRef.setPosicao(x, y, 1); // Marque a posição anterior como visitada
                    }// n funciona
                    // Bloqueie a posição atual e mova-se para a nova posição
                    labirintoRef.setPosicao(x, y, 4); // Marque a posição anterior como visitada
                    labirintoRef.setPosicao(novoX, novoY, 3); // Marque a nova posição do rato
                    labirintoRef.repaint();
                    x = novoX;
                    y = novoY;
                    moveu = true;
                    break; // Sai do loop após encontrar uma direção válida
                }
                else {
                    System.out.println("Rato sem saida!");

                }



            }

            if (!moveu) {
                // Se não foi possível se mover, o rato retrocede
                labirintoRef.setPosicao(x, y, 1);
                if (posicoesVisitadas.isEmpty()) {
                    move = false;
                    System.out.println("Rato sem saída!");
                    if (labirintoRef.getPosicao(x,y) == 4){
                        labirintoRef.setPosicao(x, y, 1); // Marque a posição anterior como visitada
                    }
                } else {
                    if (labirintoRef.getPosicao(x,y) == 4){
                        labirintoRef.setPosicao(x, y, 1); // Marque a posição anterior como visitada
                    }
                    // Retroceda para a última posição visitada
                    Posicao ultimaPosicao = posicoesVisitadas.iterator().next();
                    posicoesVisitadas.remove(ultimaPosicao);
                    labirintoRef.setPosicao(x, y, 1);
                    x = ultimaPosicao.getX();
                    y = ultimaPosicao.getY();
                    labirintoRef.setPosicao(x, y, 3);
                }
            }


            try {
                Thread.sleep(1000); // Aguarde um curto período entre os movimentos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    panel.repaint();  // Atualize o JPanel para refletir as mudanças
                }
            });

            labirintoRef.imprimirLabirinto();

        }

    }






    private boolean ePosicaoValida(int x, int y) {
        if (x >= 0 && x < tamanho && y >= 0 && y < tamanho) {

            if (labirintoRef.getPosicao(x, y) == 4) {
                labirintoRef.setPosicao(x, y, 1); // Marque a posição anterior como visitada
                return true;
            }
            return labirintoRef.getPosicao(x, y) != 1 && labirintoRef.getPosicao(x, y) != 3;
        }
        return false; // Fora dos limites da matriz
    }

    private void shuffleArray(int[] arr) {
        Random rand = new Random();

        // Loop de trás para frente para embaralhar o array
        for (int i = arr.length - 1; i > 0; i--) {
            // Gere um índice aleatório entre 0 e i (inclusive)
            int index = rand.nextInt(i + 1);

            // Troque o valor no índice 'index' com o valor no índice 'i'
            int temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }
    }



        }


