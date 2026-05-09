package domus.domain;

import domus.domain.commands.ComandoLigar;
import domus.domain.commands.ComandoDefinirIntensidadeLampada;
import domus.domain.commands.ComandoDefinirVolumeColuna;
import domus.domain.conditions.CondicaoLuminosidade;
import domus.domain.core.Utilizador;
import domus.domain.devices.ColunaInteligente;
import domus.domain.devices.LampadaInteligente;
import domus.domain.exceptions.CasaJaExisteException;
import domus.domain.exceptions.CenarioJaExisteException;
import domus.domain.exceptions.CenarioNaoExisteException;
import domus.domain.exceptions.DispositivoNaoExisteException;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.DivisaoJaExisteException;
import domus.domain.exceptions.DivisaoNaoExisteException;
import domus.domain.exceptions.EscalonamentoJaExisteException;
import domus.domain.exceptions.OperacaoInvalidaException;
import domus.domain.exceptions.SemPermissaoException;
import domus.domain.exceptions.TipoDispositivoInvalidoException;
import domus.domain.exceptions.UtilizadorJaExisteException;
import domus.domain.exceptions.UtilizadorNaoExisteException;
import domus.domain.history.RegistoInteracao;
import java.time.LocalTime;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressão das exceções de domínio expostas pela fachada DomiUM.
 */
class DomiUMExceptionsTest {

    @Test
    void criarUtilizadorDuplicadoLancaExcecao() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Utilizador");

        assertThrows(UtilizadorJaExisteException.class, () ->
                domium.criarUtilizador("u1", "Outro Utilizador")
        );
    }

    @Test
    void criarCasaComUtilizadorInexistenteLancaExcecao() {
        DomiUM domium = new DomiUM();

        assertThrows(UtilizadorNaoExisteException.class, () ->
                domium.criarCasa("u1", "c1", "Casa")
        );
    }

    @Test
    void criarCasaDuplicadaLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("u1", "c1");

        assertThrows(CasaJaExisteException.class, () ->
                domium.criarCasa("u1", "c1", "Casa Repetida")
        );
    }

    @Test
    void adicionarDivisaoSemPermissaoLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("admin", "c1");
        domium.criarUtilizador("u2", "Utilizador Sem Permissao");

        assertThrows(SemPermissaoException.class, () ->
                domium.adicionarDivisao("u2", "c1", "Sala")
        );
    }

    @Test
    void adicionarDivisaoDuplicadaLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorCasaEDivisao("u1", "c1", "Sala");

        assertThrows(DivisaoJaExisteException.class, () ->
                domium.adicionarDivisao("u1", "c1", "Sala")
        );
    }

    @Test
    void adicionarDispositivoEmDivisaoInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("u1", "c1");

        assertThrows(DivisaoNaoExisteException.class, () ->
                domium.adicionarDispositivo(
                        "u1", "c1", "Sala", "lampada", "l1",
                        "Philips", "Hue", 10.0
                )
        );
    }

    @Test
    void adicionarDispositivoComTipoInvalidoLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorCasaEDivisao("u1", "c1", "Sala");

        assertThrows(TipoDispositivoInvalidoException.class, () ->
                domium.adicionarDispositivo(
                        "u1", "c1", "Sala", "tipo_inexistente", "d1",
                        "Marca", "Modelo", 10.0
                )
        );
    }

    @Test
    void executarCenarioInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("u1", "c1");

        assertThrows(CenarioNaoExisteException.class, () ->
                domium.executarCenario("u1", "c1", "noite")
        );
    }

    @Test
    void criarCenarioDuplicadoLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("u1", "c1");
        domium.criarCenario("u1", "c1", "noite", "Modo Noite");

        assertThrows(CenarioJaExisteException.class, () ->
                domium.criarCenario("u1", "c1", "noite", "Modo Noite Repetido")
        );
    }

    @Test
    void criarEscalonamentoDuplicadoLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("u1", "c1");
        domium.criarEscalonamento(
                "u1", "c1", "esc1", "Escalonamento",
                LocalTime.of(8, 0), LocalTime.of(8, 1)
        );

        assertThrows(EscalonamentoJaExisteException.class, () ->
                domium.criarEscalonamento(
                        "u1", "c1", "esc1", "Escalonamento Repetido",
                        LocalTime.of(9, 0), LocalTime.of(9, 1)
                )
        );
    }

    @Test
    void criarAutomacaoEmDivisaoInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa("u1", "c1");

        assertThrows(DivisaoNaoExisteException.class, () ->
                domium.criarAutomacao(
                        "u1", "c1", "auto1", "Automação",
                        "Sala", new CondicaoLuminosidade(30.0, false)
                )
        );
    }

    @Test
    void executarComandoValidadoComDispositivoInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Utilizador");
        domium.criarCasa("u1", "c1", "Casa");
        domium.adicionarDivisao("u1", "c1", "Sala");

        assertThrows(DispositivoNaoExisteException.class, () ->
                domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"))
        );
    }

    @Test
    void executarComandoValidadoSemPermissaoLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComLampada("admin", "c1", "Sala", "l1");
        domium.criarUtilizador("u2", "Utilizador Sem Permissao");

        assertThrows(SemPermissaoException.class, () ->
                domium.executarComandoValidado(new ComandoLigar("u2", "c1", "l1"))
        );
    }

    @Test
    void executarComandoValidadoComTipoIncompativelLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");

        assertThrows(OperacaoInvalidaException.class, () ->
                domium.executarComandoValidado(new ComandoDefinirVolumeColuna("u1", "c1", "l1", 30))
        );

        Utilizador utilizador = domium.getUtilizador("u1");
        assertNotNull(utilizador);
        assertFalse(historicoContemAcao(utilizador, "Definiu volume da coluna para 30"));
    }

    @Test
    void executarComandoValidadoValidoRegistaHistorico() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");

        domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"));

        Utilizador utilizador = domium.getUtilizador("u1");
        assertNotNull(utilizador);
        assertTrue(historicoContemAcao(utilizador, "Ligou o dispositivo"));
    }

    @Test
    void adicionarComandoEspecificoValidoACenarioEExecutarAlteraDispositivo() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarCenario("u1", "c1", "noite", "Modo Noite");

        domium.adicionarComandoACenario(
                "u1", "c1", "noite",
                new ComandoDefinirIntensidadeLampada("u1", "c1", "l1", 75)
        );
        domium.executarCenario("u1", "c1", "noite");

        LampadaInteligente lampada = (LampadaInteligente) domium.getDispositivo("c1", "l1");
        assertNotNull(lampada);
        assertEquals(75, lampada.getIntensidade());
    }

    @Test
    void adicionarComandoIncompativelACenarioLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarCenario("u1", "c1", "noite", "Modo Noite");

        assertThrows(OperacaoInvalidaException.class, () ->
                domium.adicionarComandoACenario(
                        "u1", "c1", "noite",
                        new ComandoDefinirVolumeColuna("u1", "c1", "l1", 30)
                )
        );
    }

    @Test
    void adicionarAcaoEspecificaValidaAEscalonamentoFunciona() throws DomusException {
        DomiUM domium = criarDominioComColuna("u1", "c1", "Sala", "col1");
        LocalTime horaInicio = domium.getDataHoraAtual().toLocalTime().plusMinutes(1);
        LocalTime horaFim = horaInicio.plusMinutes(1);
        domium.criarEscalonamento("u1", "c1", "esc1", "Escalonamento", horaInicio, horaFim);

        domium.adicionarAcaoInicioAEscalonamento(
                "u1", "c1", "esc1",
                new ComandoDefinirVolumeColuna("u1", "c1", "col1", 35)
        );
        domium.avancarTempo(1);

        ColunaInteligente coluna = (ColunaInteligente) domium.getDispositivo("c1", "col1");
        assertNotNull(coluna);
        assertEquals(35, coluna.getVolume());
    }

    @Test
    void adicionarAcaoEspecificaValidaAAutomacaoFunciona() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarAutomacao(
                "u1", "c1", "auto1", "Luz baixa",
                "Sala", new CondicaoLuminosidade(30.0, false)
        );

        domium.adicionarAcaoAAutomacao(
                "u1", "c1", "auto1",
                new ComandoDefinirIntensidadeLampada("u1", "c1", "l1", 25)
        );
        domium.atualizarAmbienteDivisao("u1", "c1", "Sala", 20.0, 50.0, 10.0);

        LampadaInteligente lampada = (LampadaInteligente) domium.getDispositivo("c1", "l1");
        assertNotNull(lampada);
        assertEquals(25, lampada.getIntensidade());
    }

    /**
     * Cria um domínio mínimo com utilizador e casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return fachada preparada para os testes
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComUtilizadorECasa(String utilizadorId, String casaId)
            throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador(utilizadorId, "Utilizador");
        domium.criarCasa(utilizadorId, casaId, "Casa");
        return domium;
    }

    /**
     * Cria um domínio mínimo com utilizador, casa e divisão.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @return fachada preparada para os testes
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComUtilizadorCasaEDivisao(String utilizadorId, String casaId,
                                                         String divisaoNome)
            throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa(utilizadorId, casaId);
        domium.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        return domium;
    }

    /**
     * Cria um domínio mínimo com uma lâmpada registada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     * @return fachada preparada para os testes
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
     * @return fachada preparada para os testes
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

    /**
     * Verifica se o histórico de um utilizador contém uma ação.
     *
     * @param utilizador utilizador a consultar
     * @param acao ação esperada
     * @return {@code true} se a ação existir no histórico
     */
    private boolean historicoContemAcao(Utilizador utilizador, String acao) {
        Iterator<RegistoInteracao> iterador = utilizador.getIteradorHistorico();
        while (iterador.hasNext()) {
            if (acao.equals(iterador.next().getAcao())) {
                return true;
            }
        }
        return false;
    }
}
