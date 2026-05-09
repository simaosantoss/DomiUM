package domus.domain;

import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.devices.Dispositivo;
import domus.domain.exceptions.DomusException;
import domus.domain.statistics.ResumoCasaConsumo;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressão para tempo simulado, utilização e consumo acumulado.
 */
class TempoConsumoTest {

    @Test
    void dispositivoLigadoAcumulaTempoAoAvancarRelogio() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);

        domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"));
        domium.avancarTempo(30);

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivo);
        assertEquals(30L, dispositivo.getTempoTotalLigado());
        assertEquals(5.0, dispositivo.getConsumo(), 0.0001);
    }

    @Test
    void dispositivoDesligadoNaoAcumulaTempo() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);

        domium.avancarTempo(30);

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivo);
        assertEquals(0L, dispositivo.getTempoTotalLigado());
        assertEquals(0.0, dispositivo.getConsumo(), 0.0001);
    }

    @Test
    void ligarDuasVezesSemDesligarNaoDuplicaAtivacoes() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);

        domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"));
        domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"));

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivo);
        assertEquals(1, dispositivo.getNumeroAtivacoes());
    }

    @Test
    void escalonamentoAcumulaTempoQuandoExecutadoEmPassos() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);
        LocalTime horaAtual = domium.getDataHoraAtual().toLocalTime();
        LocalTime horaInicio = horaAtual.plusMinutes(1);
        LocalTime horaFim = horaAtual.plusMinutes(3);

        domium.criarEscalonamento("u1", "c1", "esc1", "Ligar temporariamente", horaInicio, horaFim);
        domium.adicionarAcaoInicioAEscalonamento(
                "u1", "c1", "esc1", new ComandoLigar("u1", "c1", "l1")
        );
        domium.adicionarAcaoFimAEscalonamento(
                "u1", "c1", "esc1", new ComandoDesligar("u1", "c1", "l1")
        );

        domium.avancarTempo(1);
        Dispositivo dispositivoLigado = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivoLigado);
        assertTrue(dispositivoLigado.isLigado());

        domium.avancarTempo(2);
        Dispositivo dispositivoDesligado = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivoDesligado);
        assertFalse(dispositivoDesligado.isLigado());
        assertEquals(2L, dispositivoDesligado.getTempoTotalLigado());
    }

    @Test
    void escalonamentoDentroDoIntervaloContaTempoParcial() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);
        LocalTime horaAtual = domium.getDataHoraAtual().toLocalTime();
        LocalTime horaInicio = horaAtual.plusMinutes(2);
        LocalTime horaFim = horaAtual.plusMinutes(5);

        domium.criarEscalonamento("u1", "c1", "esc1", "Ligar parcialmente", horaInicio, horaFim);
        domium.adicionarAcaoInicioAEscalonamento(
                "u1", "c1", "esc1", new ComandoLigar("u1", "c1", "l1")
        );
        domium.adicionarAcaoFimAEscalonamento(
                "u1", "c1", "esc1", new ComandoDesligar("u1", "c1", "l1")
        );

        domium.avancarTempo(7);

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivo);
        assertFalse(dispositivo.isLigado());
        assertEquals(3L, dispositivo.getTempoTotalLigado());
    }

    @Test
    void escalonamentoQueLigaDentroDoIntervaloFicaLigadoEContaAteAoFimDoAvanco() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);
        LocalTime horaAtual = domium.getDataHoraAtual().toLocalTime();
        LocalTime horaInicio = horaAtual.plusMinutes(2);
        LocalTime horaFim = horaAtual.plusMinutes(10);

        domium.criarEscalonamento("u1", "c1", "esc1", "Ligar durante avanço", horaInicio, horaFim);
        domium.adicionarAcaoInicioAEscalonamento(
                "u1", "c1", "esc1", new ComandoLigar("u1", "c1", "l1")
        );
        domium.adicionarAcaoFimAEscalonamento(
                "u1", "c1", "esc1", new ComandoDesligar("u1", "c1", "l1")
        );

        domium.avancarTempo(5);

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivo);
        assertTrue(dispositivo.isLigado());
        assertEquals(3L, dispositivo.getTempoTotalLigado());
    }

    @Test
    void casaMaiorConsumoUsaConsumoAcumulado() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Utilizador");
        domium.criarCasa("u1", "c1", "Casa Baixo Consumo");
        domium.criarCasa("u1", "c2", "Casa Alto Consumo Desligada");
        domium.adicionarDivisao("u1", "c1", "Sala");
        domium.adicionarDivisao("u1", "c2", "Sala");
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l1", "Philips", "Hue", 10.0);
        domium.adicionarDispositivo("u1", "c2", "Sala", "lampada", "l2", "Philips", "Hue", 100.0);

        domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"));
        domium.avancarTempo(120);

        ResumoCasaConsumo resumo = domium.getCasaMaiorConsumo();
        assertNotNull(resumo);
        assertEquals("c1", resumo.getCasaId());
        assertEquals(20.0, resumo.getConsumoTotal(), 0.0001);
    }

    /**
     * Cria um domínio mínimo com uma lâmpada registada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     * @param consumoPorHora consumo por hora da lâmpada
     * @return fachada preparada para os testes
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComLampada(String utilizadorId, String casaId,
                                          String divisaoNome, String dispositivoId,
                                          double consumoPorHora) throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador(utilizadorId, "Utilizador");
        domium.criarCasa(utilizadorId, casaId, "Casa");
        domium.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        domium.adicionarDispositivo(
                utilizadorId, casaId, divisaoNome, "lampada", dispositivoId,
                "Philips", "Hue", consumoPorHora
        );
        return domium;
    }
}
