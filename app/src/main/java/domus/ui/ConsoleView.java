package domus.ui;

import java.util.Scanner;

/**
 * View textual responsável pela interação com o utilizador através da consola.
 */
public class ConsoleView {

    /**
     * Scanner usado para ler dados da consola.
     */
    private final Scanner scanner;

    /**
     * Cria uma nova view de consola.
     */
    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Mostra o menu principal da aplicação.
     */
    public void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("=== DomusControl ===");
        System.out.println("1. Criar utilizador");
        System.out.println("2. Criar casa");
        System.out.println("3. Adicionar divisão");
        System.out.println("4. Adicionar dispositivo");
        System.out.println("5. Ligar dispositivo");
        System.out.println("6. Desligar dispositivo");
        System.out.println("7. Listar casas");
        System.out.println("8. Listar utilizadores");
        System.out.println("9. Guardar estado");
        System.out.println("10. Carregar estado");
        System.out.println("0. Sair");
    }

    /**
     * Lê a opção escolhida no menu.
     *
     * @return opção escolhida
     */
    public int lerOpcao() {
        return lerInteiro("Opção: ");
    }

    /**
     * Lê uma linha de texto da consola.
     *
     * @param mensagem mensagem apresentada antes da leitura
     * @return texto lido
     */
    public String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return this.scanner.nextLine();
    }

    /**
     * Lê um valor decimal da consola.
     *
     * @param mensagem mensagem apresentada antes da leitura
     * @return valor decimal lido
     */
    public double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = this.scanner.nextLine();
            try {
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido. Introduza um número decimal.");
            }
        }
    }

    /**
     * Lê um valor inteiro da consola.
     *
     * @param mensagem mensagem apresentada antes da leitura
     * @return valor inteiro lido
     */
    public int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = this.scanner.nextLine();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido. Introduza um número inteiro.");
            }
        }
    }

    /**
     * Mostra uma mensagem informativa.
     *
     * @param mensagem mensagem a apresentar
     */
    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    /**
     * Mostra uma mensagem de erro.
     *
     * @param mensagem mensagem a apresentar
     */
    public void mostrarErro(String mensagem) {
        System.out.println("Erro: " + mensagem);
    }

    /**
     * Fecha os recursos associados à view.
     */
    public void fechar() {
        this.scanner.close();
    }
}
