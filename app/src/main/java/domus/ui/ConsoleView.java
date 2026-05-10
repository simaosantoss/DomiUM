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
        System.out.println("11. Menu de cenários");
        System.out.println("12. Menu de escalonamentos");
        System.out.println("13. Menu de automações");
        System.out.println("14. Consultar data/hora atual");
        System.out.println("15. Avançar tempo");
        System.out.println("16. Atualizar ambiente de divisão");
        System.out.println("17. Menu de estatísticas");
        System.out.println("18. Menu de sugestões");
        System.out.println("19. Consultar casa");
        System.out.println("20. Consultar divisão");
        System.out.println("21. Consultar dispositivo");
        System.out.println("22. Listar todos os dispositivos de uma casa");
        System.out.println("23. Operações de dispositivos");
        System.out.println("24. Gerir permissões");
        System.out.println("0. Sair");
    }

    /**
     * Mostra o submenu de operações sobre dispositivos.
     */
    public void mostrarMenuDispositivos() {
        System.out.println();
        System.out.println("=== Operações de dispositivos ===");
        System.out.println("1. Ligar dispositivo");
        System.out.println("2. Desligar dispositivo");
        System.out.println("3. Definir intensidade da lâmpada");
        System.out.println("4. Definir cor da lâmpada");
        System.out.println("5. Definir volume da coluna");
        System.out.println("6. Definir playlist da coluna");
        System.out.println("7. Definir abertura da cortina");
        System.out.println("8. Definir temperatura do ar condicionado");
        System.out.println("9. Definir modo do ar condicionado");
        System.out.println("10. Definir humidade alvo do desumidificador");
        System.out.println("11. Abrir portão");
        System.out.println("12. Fechar portão");
        System.out.println("13. Trancar fechadura");
        System.out.println("14. Destrancar fechadura");
        System.out.println("0. Voltar");
    }

    /**
     * Mostra o submenu de cenários.
     */
    public void mostrarMenuCenarios() {
        System.out.println();
        System.out.println("=== Cenários ===");
        System.out.println("1. Criar cenário");
        System.out.println("2. Adicionar comando a cenário");
        System.out.println("3. Executar cenário");
        System.out.println("4. Listar cenários da casa");
        System.out.println("0. Voltar");
    }

    /**
     * Mostra o submenu de escalonamentos.
     */
    public void mostrarMenuEscalonamentos() {
        System.out.println();
        System.out.println("=== Escalonamentos ===");
        System.out.println("1. Criar escalonamento");
        System.out.println("2. Adicionar ação de início");
        System.out.println("3. Adicionar ação de fim");
        System.out.println("4. Listar escalonamentos da casa");
        System.out.println("0. Voltar");
    }

    /**
     * Mostra o submenu de automações.
     */
    public void mostrarMenuAutomacoes() {
        System.out.println();
        System.out.println("=== Automações ===");
        System.out.println("1. Criar automação por temperatura");
        System.out.println("2. Criar automação por humidade");
        System.out.println("3. Criar automação por luminosidade");
        System.out.println("4. Adicionar ação à automação");
        System.out.println("5. Listar automações da casa");
        System.out.println("0. Voltar");
    }

    /**
     * Mostra o submenu de estatísticas.
     */
    public void mostrarMenuEstatisticas() {
        System.out.println();
        System.out.println("=== Estatísticas ===");
        System.out.println("1. Casa com maior consumo");
        System.out.println("2. Top 3 dispositivos por tempo ligado");
        System.out.println("3. Top 3 dispositivos por número de ativações");
        System.out.println("4. Top 3 divisões com mais dispositivos");
        System.out.println("5. Resumo de consumo das casas");
        System.out.println("0. Voltar");
    }

    /**
     * Mostra o submenu de sugestões.
     */
    public void mostrarMenuSugestoes() {
        System.out.println();
        System.out.println("=== Sugestões ===");
        System.out.println("1. Listar sugestões de escalonamento");
        System.out.println("2. Aceitar sugestão de escalonamento");
        System.out.println("0. Voltar");
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
