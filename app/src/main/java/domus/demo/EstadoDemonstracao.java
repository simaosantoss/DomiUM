package domus.demo;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.conditions.CondicaoLuminosidade;
import java.time.LocalTime;

/**
 * Utilitário responsável por popular o domínio com dados de demonstração.
 */
public final class EstadoDemonstracao {

    /**
     * Construtor privado para impedir instanciação.
     */
    private EstadoDemonstracao() {
    }

    /**
     * Acrescenta dados simples de demonstração ao model indicado.
     *
     * Se o model for inválido, a operação é ignorada.
     *
     * @param model fachada do domínio a popular
     */
    public static void popular(DomiUM model) {
        if (model == null) {
            return;
        }

        String utilizadorId = "demo_u1";
        String casaId = "demo_c1";

        model.criarUtilizador(utilizadorId, "Utilizador Demonstração");
        model.criarCasa(utilizadorId, casaId, "Casa Demonstração");
        model.adicionarDivisao(utilizadorId, casaId, "Sala");
        model.adicionarDivisao(utilizadorId, casaId, "Quarto");
        model.adicionarDivisao(utilizadorId, casaId, "Garagem");

        model.adicionarDispositivo(
                utilizadorId, casaId, "Sala", "lampada", "demo_l1",
                "Philips", "Hue", 10.0
        );
        model.adicionarDispositivo(
                utilizadorId, casaId, "Quarto", "lampada", "demo_l2",
                "Philips", "Hue", 8.0
        );
        model.adicionarDispositivo(
                utilizadorId, casaId, "Garagem", "lampada", "demo_l3",
                "Philips", "Hue", 6.0
        );

        model.criarCenario(utilizadorId, casaId, "demo_cenario_noite", "Cenário Noite");
        model.adicionarComandoACenario(
                utilizadorId, casaId, "demo_cenario_noite",
                new ComandoLigar(utilizadorId, casaId, "demo_l1")
        );
        model.adicionarComandoACenario(
                utilizadorId, casaId, "demo_cenario_noite",
                new ComandoDesligar(utilizadorId, casaId, "demo_l1")
        );

        model.criarEscalonamento(
                utilizadorId, casaId, "demo_esc_manha", "Luz da manhã",
                LocalTime.of(8, 0), LocalTime.of(8, 1)
        );
        model.adicionarAcaoInicioAEscalonamento(
                utilizadorId, casaId, "demo_esc_manha",
                new ComandoLigar(utilizadorId, casaId, "demo_l2")
        );

        model.criarAutomacao(
                utilizadorId, casaId, "demo_auto_luz", "Ligar luz com pouca luminosidade",
                "Sala", new CondicaoLuminosidade(30.0, false)
        );
        model.adicionarAcaoAAutomacao(
                utilizadorId, casaId, "demo_auto_luz",
                new ComandoLigar(utilizadorId, casaId, "demo_l1")
        );

        for (int i = 0; i < 3; i++) {
            model.executarComando(new ComandoLigar(utilizadorId, casaId, "demo_l1"));
        }
    }
}
