package domus;

import domus.domain.DomiUM;

public class Main {

    public static void main(String[] args) {
        String ficheiro = "estado-teste.dat";

        DomiUM domium = new DomiUM();

        domium.criarUtilizador("u1", "Simão");
        domium.criarCasa("u1", "c1", "Casa Principal");
        domium.adicionarDivisao("u1", "c1", "Sala");

        // Este tipo pode variar conforme os nomes registados no teu DispositivoRegistry.
        // Mesmo que não adicione o dispositivo, a persistência já testa a serialização do estado.
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l1", "Philips", "Hue", 10.0);

        domium.gravarEstado(ficheiro);

        DomiUM carregado = DomiUM.carregarEstado(ficheiro);

        System.out.println("Utilizador carregado:");
        System.out.println(carregado.getUtilizador("u1"));

        System.out.println();
        System.out.println("Casa carregada:");
        System.out.println(carregado.getCasa("c1"));

        if (carregado.getCasa("c1") != null) {
            System.out.println();
            System.out.println("Persistência OK");
        } else {
            System.out.println();
            System.out.println("Persistência FALHOU");
        }
    }
}