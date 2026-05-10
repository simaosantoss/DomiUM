package domus.domain;

import domus.domain.commands.ComandoDefinirIntensidadeLampada;
import domus.domain.commands.ComandoDefinirVolumeColuna;
import domus.domain.conditions.CondicaoLuminosidade;
import domus.domain.devices.ColunaInteligente;
import domus.domain.devices.LampadaInteligente;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.OperacaoInvalidaException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes de regressão para comandos específicos guardados em entidades
 * agregadoras como cenários, escalonamentos e automações.
 */
class ComandosAgregadosTest {

    @Test
    void cenarioComComandoEspecificoAlteraDispositivo() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarCenario("u1", "c1", "cinema", "Ver Cinema");

        domium.adicionarComandoACenario(
                "u1", "c1", "cinema",
                new ComandoDefinirIntensidadeLampada("u1", "c1", "l1", 20)
        );
        domium.executarCenario("u1", "c1", "cinema");

        LampadaInteligente lampada = (LampadaInteligente) domium.getDispositivo("c1", "l1");
        assertNotNull(lampada);
        assertEquals(20, lampada.getIntensidade());
    }

    @Test
    void escalonamentoComComandoEspecificoAlteraDispositivo() throws DomusException {
        DomiUM domium = criarDominioComColuna("u1", "c1", "Sala", "col1");
        LocalTime horaInicio = domium.getDataHoraAtual().toLocalTime().plusMinutes(1);
        LocalTime horaFim = horaInicio.plusMinutes(1);
        domium.criarEscalonamento("u1", "c1", "musica", "Música", horaInicio, horaFim);

        domium.adicionarAcaoInicioAEscalonamento(
                "u1", "c1", "musica",
                new ComandoDefinirVolumeColuna("u1", "c1", "col1", 42)
        );
        domium.avancarTempo(1);

        ColunaInteligente coluna = (ColunaInteligente) domium.getDispositivo("c1", "col1");
        assertNotNull(coluna);
        assertEquals(42, coluna.getVolume());
    }

    @Test
    void automacaoComComandoEspecificoAlteraDispositivo() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarAutomacao(
                "u1", "c1", "auto-luz", "Luz baixa",
                "Sala", new CondicaoLuminosidade(30.0, false)
        );

        domium.adicionarAcaoAAutomacao(
                "u1", "c1", "auto-luz",
                new ComandoDefinirIntensidadeLampada("u1", "c1", "l1", 15)
        );
        domium.atualizarAmbienteDivisao("u1", "c1", "Sala", 20.0, 50.0, 10.0);

        LampadaInteligente lampada = (LampadaInteligente) domium.getDispositivo("c1", "l1");
        assertNotNull(lampada);
        assertEquals(15, lampada.getIntensidade());
    }

    @Test
    void comandoIncompativelNaoPodeSerGuardado() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarCenario("u1", "c1", "cinema", "Ver Cinema");

        assertThrows(OperacaoInvalidaException.class, () ->
                domium.adicionarComandoACenario(
                        "u1", "c1", "cinema",
                        new ComandoDefinirVolumeColuna("u1", "c1", "l1", 30)
                )
        );
    }

    /**
     * Cria um domínio mínimo com uma lâmpada registada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     * @return fachada preparada para teste
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComLampada(String utilizadorId, String casaId,
                                          String divisaoNome, String dispositivoId)
            throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador(utilizadorId, "Utilizador");
        domium.criarCasa(utilizadorId, casaId, "Casa");
        domium.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        domium.adicionarDispositivo(
                utilizadorId, casaId, divisaoNome, "lampada", dispositivoId,
                "Philips", "Hue", 10.0
        );
        return domium;
    }

    /**
     * Cria um domínio mínimo com uma coluna registada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     * @return fachada preparada para teste
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComColuna(String utilizadorId, String casaId,
                                         String divisaoNome, String dispositivoId)
            throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador(utilizadorId, "Utilizador");
        domium.criarCasa(utilizadorId, casaId, "Casa");
        domium.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        domium.adicionarDispositivo(
                utilizadorId, casaId, divisaoNome, "coluna", dispositivoId,
                "JBL", "Charge", 20.0
        );
        return domium;
    }
}
