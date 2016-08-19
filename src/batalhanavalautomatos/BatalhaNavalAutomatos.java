/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalhanavalautomatos;

import static batalhanavalautomatos.BatalhaNavalAutomatos.configuration;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Serrano
 */
public class BatalhaNavalAutomatos {

    /**
     *
     * @author Serrano
     */
    /*
     * Tabuleiro
     * 0 - Vazio
     * 1 - Navio
     * 2 - Erro
     * 3 - Acertou
     */
    static final int POSICAOX = 0;
    static final int POSICAOY = 1;
    static final int VAZIO = 0;
    static final int NAVIO = 1;
    static final int ERROU_TIRO = 2;
    static final int ACERTOU_TIRO = 3;
    static int tamanhoX, tamanhoY, qtdNavios, limMaxNavio;
    static int tabuleiroJogador1[][], tabuleiroJogador2[][];
    static String nomeJogador1, nomeJogador2;
    static int naviosJogador1, naviosJogador2;
    static ArrayList<String> verificaMov = new ArrayList<>();
    static int contador = 0;
    static Configuration configuration = new Configuration();

    static Scanner entrada = new Scanner(System.in);

    /**
     * @param recognizer
     * @param args the command line arguments
     */
    public static void ObterTamanhoTabuleiro(LiveSpeechRecognizer recognizer) {

        for (;;) {
            boolean verifica = false;

            try {
                recognizer.startRecognition(true);
                System.out.println("Fale a altura do tabuleiro:");
                String utterance = recognizer.getResult().getHypothesis();
                System.out.println(utterance);
                recognizer.stopRecognition();
                System.out.println("Pronto");
                tamanhoX = Integer.parseInt(utterance);

                recognizer.startRecognition(true);
                System.out.println("Fale o comprimento do tabuleiro:");
                String utterance2 = recognizer.getResult().getHypothesis();
                System.out.println(utterance2);
                recognizer.stopRecognition();
                System.out.println("Pronto");
                tamanhoY = Integer.parseInt(utterance2);
                verifica = true;
            } catch (InputMismatchException er) {
                System.out.println("Digite um valor numérico válido");
            }

            if (verifica) {
                break;
            }
        }
    }

    public static void ObterNomeJogadores(LiveSpeechRecognizer recognizer) {
        recognizer.startRecognition(true);
        System.out.println("Fale o nome do jogador 1:");
        String utterance = recognizer.getResult().getHypothesis();
        System.out.println(utterance);
        recognizer.stopRecognition();
        System.out.println("Pronto");
        nomeJogador1 = utterance;

        recognizer.startRecognition(true);
        System.out.println("Fale o nome do jogador 2:");
        String utterance2 = recognizer.getResult().getHypothesis();
        System.out.println(utterance2);
        recognizer.stopRecognition();
        System.out.println("Pronto");
        nomeJogador2 = utterance2;
    }

    public static void CalculaQtdMaxNavios() {
        limMaxNavio = (tamanhoX * tamanhoY) / 3;
    }

    public static void ColocaTamanhoTabuleiro() {
        tabuleiroJogador1 = RetornaTabuleiroVazio();
        tabuleiroJogador2 = RetornaTabuleiroVazio();
    }

    public static int[][] RetornaTabuleiroVazio() {
        return new int[tamanhoX][tamanhoY];
    }

    //
    public static void ObterQtdNavios(LiveSpeechRecognizer recognizer) {

        
        recognizer.startRecognition(true);
        System.out.println("Fale a quantidade de navios:");
        System.out.println("Max: " + limMaxNavio + " navios");
        String utterance = recognizer.getResult().getHypothesis();
        System.out.println(utterance);
        recognizer.stopRecognition();
        System.out.println("Pronto");
        qtdNavios = Integer.parseInt(utterance);
        if (qtdNavios < 1 && qtdNavios > limMaxNavio) {
            qtdNavios = limMaxNavio;
        }

    }

    public static void InstanciarNavios() {
        naviosJogador1 = qtdNavios;
        naviosJogador2 = qtdNavios;
    }

    public static int[][] RetornarNaviosTabuleiro() {
        int novoTabuleiro[][] = RetornaTabuleiroVazio();
        int qtdRestoNavios = qtdNavios;
        int x = 0, y = 0;
        Random posicoes = new Random();
        do {
            x = 0;
            y = 0;
            for (int[] linha : novoTabuleiro) {
                for (int coluna : linha) {
                    if (posicoes.nextInt(100) <= 10) {
                        if (coluna == VAZIO) {
                            novoTabuleiro[x][y] = NAVIO;
                            qtdRestoNavios--;
                            break;
                        }
                        if (qtdRestoNavios < 0) {
                            break;
                        }
                    }
                    y++;
                }
                y = 0;
                x++;
                if (qtdRestoNavios <= 0) {
                    break;
                }
            }

        } while (qtdRestoNavios > 0);
        return novoTabuleiro;
    }

    public static void inserirNaviosTabuleiros() {
        tabuleiroJogador1 = RetornarNaviosTabuleiro();
        tabuleiroJogador2 = RetornarNaviosTabuleiro();
    }

    public static void ExibeNumeros() {
        int numeroColuna = 1;
        String numerosTab = "   ";

        for (int i = 0; i < tamanhoY; i++) {
            numerosTab += (numeroColuna++) + " ";
        }
        System.out.println(numerosTab);
    }

    public static void ExibirTabuleiro(String nomeJogador, int[][] tabuleiro, boolean sTabuleiro) {
        System.out.println("|----- " + nomeJogador + " -----|");
        ExibeNumeros();
        String linhaTabuleiro = "";

        char letraLinha = 65;

        for (int[] linha : tabuleiro) {

            linhaTabuleiro = (letraLinha++) + " |";

            for (int coluna : linha) {
                switch (coluna) {
                    case VAZIO:
                        linhaTabuleiro += " |";
                        break;
                    case NAVIO:
                        if (sTabuleiro) {
                            linhaTabuleiro += "N|";
                            break;
                        } else {
                            linhaTabuleiro += " |";
                            break;
                        }
                    case ERROU_TIRO:
                        linhaTabuleiro += "X|";
                        break;

                    case ACERTOU_TIRO:
                        linhaTabuleiro += "O|";
                        break;
                }
            }
            System.out.println(linhaTabuleiro);
        }

    }

    public static void ExibirTabuleiroJogadores() {
        ExibirTabuleiro(nomeJogador1, tabuleiroJogador1, true);
        ExibirTabuleiro(nomeJogador2, tabuleiroJogador2, false);

    }

    public static boolean VerificaPosicaoJogador(int[] posicoes) {
        boolean verif = true;
        if (posicoes[0] > tamanhoX - 1) {
            verif = false;
            System.out.println("A posição das letras não pode ser maior que " + (char) (tamanhoX + 64));
        }

        if (posicoes[1] > tamanhoY) {
            verif = false;
            System.out.println("A posição das letras não pode ser maior que " + tamanhoY);
        }

        return verif;
    }

    public static String CoordenadaJogador(LiveSpeechRecognizer recognizer) {

          recognizer.startRecognition(true);
                System.out.println("Fale a coordenada do seu tiro:");
                String utterance = recognizer.getResult().getHypothesis();
                System.out.println(utterance);
                recognizer.stopRecognition();
                System.out.println("Pronto");
                
        return utterance;
    }

    public static boolean ValidarTiroJogador(String tiroJogador) {
        int qtdNum = (tamanhoY > 10) ? 2 : 1;
        String exvef = "^[A-Za-z]{1}[0-9]{" + qtdNum + "}$";

        return tiroJogador.matches(exvef);
    }

    public static int[] RetornaPosicoesJogador(String tiroJogador) {
        String tiro = tiroJogador.toLowerCase();
        int[] retorna = new int[2];
        retorna[POSICAOX] = tiro.charAt(0) - 97;
        retorna[POSICAOY] = Integer.parseInt(tiro.substring(1)) - 1;
        return retorna;

    }

    public static void InserirValoresTabuleiro(int[] posicoes, int numJogador) {

        if (numJogador == 1) {

            if (tabuleiroJogador2[posicoes[POSICAOX]][posicoes[POSICAOY]] == NAVIO) {
                tabuleiroJogador2[posicoes[POSICAOX]][posicoes[POSICAOY]] = ACERTOU_TIRO;
                naviosJogador2--;
                System.out.println("Você acertou um navio!!!!! -> " + nomeJogador1);
            } else {
                tabuleiroJogador2[posicoes[POSICAOX]][posicoes[POSICAOY]] = ERROU_TIRO;
                System.out.println("Você errou o tiro!!!!! -> " + nomeJogador1);
            }
        } else {
            if (tabuleiroJogador1[posicoes[POSICAOX]][posicoes[POSICAOY]] == NAVIO) {
                tabuleiroJogador1[posicoes[POSICAOX]][posicoes[POSICAOY]] = ACERTOU_TIRO;
                naviosJogador1--;
                System.out.println("Você acertou um navio!!!!! ->" + nomeJogador2);
            } else {
                tabuleiroJogador1[posicoes[POSICAOX]][posicoes[POSICAOY]] = ERROU_TIRO;
                System.out.println("Você errou o tiro!!!!! -> " + nomeJogador2);
            }
        }

    }

    public static boolean AcaoJogador(LiveSpeechRecognizer recognizer) {

        boolean aValida = true;
        String tiroJogador = CoordenadaJogador(recognizer);

        if (ValidarTiroJogador(tiroJogador)) {

            int[] posicoes = RetornaPosicoesJogador(tiroJogador);

            if (VerificaPosicaoJogador(posicoes)) {

                InserirValoresTabuleiro(posicoes, 1);

            } else {

                aValida = false;
            }

        } else {
            System.out.println("Posição Inválida");
            aValida = false;
        }

        return aValida;
    }

    public static void acaoPC() {

        int[] posicoes = RetornaJogadaPC();
        InserirValoresTabuleiro(posicoes, 2);

    }

    public static int[] RetornaJogadaPC() {

        int x = RetornaJogadaAleatoriaPC(tamanhoX), y = RetornaJogadaAleatoriaPC(tamanhoY);
        int[] posicoes = new int[2];

        String envia = x + "" + y;

        if (contador == 0) {

            verificaMov.add(envia);
            contador = 1;
        } else {

            while (verificaMov.contains(envia)) {
                x = RetornaJogadaAleatoriaPC(tamanhoX);
                y = RetornaJogadaAleatoriaPC(tamanhoY);
                envia = x + "" + y;
            }

            posicoes[POSICAOX] = x;
            posicoes[POSICAOY] = y;
            verificaMov.add(envia);
        }

        return posicoes;

    }

    public static int RetornaJogadaAleatoriaPC(int limite) {
        Random jogadaPC = new Random();
        int numGerado = jogadaPC.nextInt(limite);

        return (numGerado == limite) ? --numGerado : numGerado;
    }

    public static void main(String[] args) throws IOException {

       
        // Modelo Acústico
        configuration.setAcousticModelPath("resource:/English/");
// Dicionario
        configuration.setDictionaryPath("resource:/Dicionario/cmudict-en-us.dict");
// Gramática
        configuration.setUseGrammar(true);
        configuration.setGrammarName("words");
        configuration.setGrammarPath("resource:/gramatica/");
        
        System.out.println(configuration.getGrammarName());
        System.out.println(configuration.getGrammarPath());

        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
        
        ObterNomeJogadores(recognizer);
        ObterTamanhoTabuleiro(recognizer);
        ColocaTamanhoTabuleiro();
        CalculaQtdMaxNavios();
        ObterQtdNavios(recognizer);
        InstanciarNavios();
        inserirNaviosTabuleiros();
        boolean jogoRodando = true;
        do {
            ExibirTabuleiroJogadores();
            if (AcaoJogador(recognizer)) {

                if (naviosJogador2 <= 0) {

                    System.out.println(nomeJogador1 + " Venceu o Jogo");
                    break;
                }
                acaoPC();

                if (naviosJogador1 <= 0) {

                    System.out.println("PC Venceu o Jogo");
                    break;
                }
            }
        } while (jogoRodando);
        ExibirTabuleiroJogadores();
        entrada.close();

    }

}
