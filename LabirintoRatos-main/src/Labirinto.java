import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Labirinto extends JPanel {
    private int[][] labirinto;
    private int queijoX;
    private int queijoY;
    private final Object lock = new Object(); // Objeto de bloqueio compartilhado


    @Override
    protected void paintComponent(Graphics g) {

        ImageIcon imagemCinza = new ImageIcon("C:\\Users\\arthu\\Downloads\\rato.png");
        super.paintComponent(g);

        int larguraCelula = getWidth() / labirinto[0].length;
        int alturaCelula = getHeight() / labirinto.length;

        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[0].length; j++) {
                int valor = labirinto[i][j];

                // Desenhe as células com base nos valores da matriz
                if (valor == 0) {
                    g.setColor(Color.WHITE);
                } else if (valor == 1) {
                    g.setColor(Color.BLACK);
                } else if (valor == 2) {
                    g.setColor(Color.YELLOW);
                } else if (valor == 3) {
                   // Image img = imagemCinza.getImage();
                   g.setColor(Color.gray);
                   /* int x =  g.getClipBounds().x * larguraCelula;
                    int y =  g.getClipBounds().y * alturaCelula;
                    g.drawImage(img, x, y, 20,20,this);
                    g.getClipBounds();*/
                }
             else if (valor == 4) {
                g.setColor(Color.BLUE);

            }
                g.fillRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
            }
        }
    }



    public Labirinto(Container mundo, String rato, int tamanho, int numRatos) {
        labirinto = new int[tamanho][tamanho];
        criarLabirintoPrim(tamanho);
        criarRatos(numRatos, tamanho);

    }

    private void criarLabirintoPrim(int tamanho) {
        Random random = new Random();

        // Inicialize o labirinto com todas as paredes (valor 1)
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                labirinto[i][j] = 1;
            }
        }

        // Escolha uma célula inicial aleatória
        int startX = random.nextInt(tamanho);
        int startY = random.nextInt(tamanho);
        labirinto[startX][startY] = 0; // Marque a célula inicial como parte do caminho

        List<int[]> fronteira = new ArrayList<>();
        fronteira.add(new int[]{startX, startY});

        while (!fronteira.isEmpty()) {
            int[] cell = fronteira.remove(random.nextInt(fronteira.size()));
            int x = cell[0];
            int y = cell[1];
            int[][] vizinhos = {{x - 2, y}, {x + 2, y}, {x, y - 2}, {x, y + 2}};

            for (int[] vizinho : vizinhos) {
                int nx = vizinho[0];
                int ny = vizinho[1];
                if (nx >= 0 && nx < tamanho && ny >= 0 && ny < tamanho) {
                    if (labirinto[nx][ny] == 1) {
                        labirinto[nx][ny] = 0; // Marque o vizinho como parte do caminho
                        labirinto[x + (nx - x) / 2][y + (ny - y) / 2] = 0; // Remova a parede
                        fronteira.add(new int[]{nx, ny});
                    }
                }
            }
        }

        // Escolha uma célula final aleatória e marque-a como a posição do queijo

        do {
            queijoX = random.nextInt(tamanho);
            queijoY = random.nextInt(tamanho);
        } while (labirinto[queijoX][queijoY] != 0 );
        labirinto[queijoX][queijoY] = 2; // Marque a posição do queijo
    }

        private void criarRatos ( int numRatos ,int tamanho){
            Random random = new Random();

            for (int i = 0; i < numRatos; i++) {
                int ratoX, ratoY;
                do {
                    ratoX = random.nextInt(labirinto.length);
                    ratoY = random.nextInt(labirinto[0].length);
                } while (labirinto[ratoX][ratoY] != 1 && (ratoX != queijoX && ratoY != queijoY) || labirinto[ratoX][ratoY] == 0);
                labirinto[ratoX][ratoY] = 3; // Marque a posição do rato
                synchronized (lock) {
                    new Ratothread(ratoX, ratoY, tamanho, this, this).start();
                }
            }
        }

        public void imprimirLabirinto () {
            for (int i = 0; i < labirinto.length; i++) {
                for (int j = 0; j < labirinto[i].length; j++) {
                    System.out.print(labirinto[i][j] + " ");
                }
                System.out.println();
            }
        }

        public int getTamanho () {
            return labirinto.length;
        }

        public int getQueijoX () {
            return queijoX;
        }

        public int getQueijoY () {
            return queijoY;
        }

        public int getPosicao ( int x, int y){
            return labirinto[x][y];
        }

        public void setPosicao ( int x, int y, int valor){
            labirinto[x][y] = valor;
        }
    }
