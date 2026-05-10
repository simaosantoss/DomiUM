package domus.domain;

import domus.domain.conditions.CondicaoLuminosidade;
import domus.domain.core.TipoPermissao;
import domus.domain.devices.Dispositivo;
import domus.domain.exceptions.AutomacaoNaoExisteException;
import domus.domain.exceptions.CenarioNaoExisteException;
import domus.domain.exceptions.DispositivoNaoExisteException;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.EscalonamentoNaoExisteException;
import domus.domain.exceptions.SemPermissaoException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes de regressão das operações de remoção expostas pela fachada DomiUM.
 */
class DomiUMRemocoesTest {

    @Test
    void administradorRemoveDispositivo() throws DomusException {
        DomiUM domium = criarDominioComLampada();

        domium.removerDispositivo("u1", "c1", "l1");

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNull(dispositivo);
    }

    @Test
    void utilizadorNormalNaoRemoveDispositivo() throws DomusException {
        DomiUM domium = criarDominioComLampada();
        domium.criarUtilizador("u2", "Utilizador Normal");
        domium.atribuirPermissaoCasa("u1", "c1", "u2", TipoPermissao.NORMAL);

        assertThrows(SemPermissaoException.class, () ->
                domium.removerDispositivo("u2", "c1", "l1")
        );
    }

    @Test
    void removerDispositivoInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorCasaEDivisao();

        assertThrows(DispositivoNaoExisteException.class, () ->
                domium.removerDispositivo("u1", "c1", "l1")
        );
    }

    @Test
    void administradorRemoveCenario() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();
        domium.criarCenario("u1", "c1", "cenario1", "Cenário");

        domium.removerCenario("u1", "c1", "cenario1");

        assertFalse(domium.getIteradorCenarios("c1").hasNext());
    }

    @Test
    void administradorRemoveEscalonamento() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();
        domium.criarEscalonamento(
                "u1", "c1", "esc1", "Escalonamento",
                LocalTime.of(8, 0), LocalTime.of(8, 1)
        );

        domium.removerEscalonamento("u1", "c1", "esc1");

        assertFalse(domium.getIteradorEscalonamentos("c1").hasNext());
    }

    @Test
    void administradorRemoveAutomacao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorCasaEDivisao();
        domium.criarAutomacao(
                "u1", "c1", "auto1", "Automação", "Sala",
                new CondicaoLuminosidade(30.0, false)
        );

        domium.removerAutomacao("u1", "c1", "auto1");

        assertFalse(domium.getIteradorAutomacoes("c1").hasNext());
    }

    @Test
    void utilizadorNormalNaoRemoveAgregado() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();
        domium.criarUtilizador("u2", "Utilizador Normal");
        domium.atribuirPermissaoCasa("u1", "c1", "u2", TipoPermissao.NORMAL);
        domium.criarCenario("u1", "c1", "cenario1", "Cenário");

        assertThrows(SemPermissaoException.class, () ->
                domium.removerCenario("u2", "c1", "cenario1")
        );
    }

    @Test
    void removerEntidadeDuasVezesLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();
        domium.criarCenario("u1", "c1", "cenario1", "Cenário");
        domium.removerCenario("u1", "c1", "cenario1");

        assertThrows(CenarioNaoExisteException.class, () ->
                domium.removerCenario("u1", "c1", "cenario1")
        );
    }

    @Test
    void removerEscalonamentoInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();

        assertThrows(EscalonamentoNaoExisteException.class, () ->
                domium.removerEscalonamento("u1", "c1", "esc1")
        );
    }

    @Test
    void removerAutomacaoInexistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();

        assertThrows(AutomacaoNaoExisteException.class, () ->
                domium.removerAutomacao("u1", "c1", "auto1")
        );
    }

    /**
     * Cria um domínio com utilizador administrador e casa.
     *
     * @return domínio preparado para testes
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComUtilizadorECasa() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Administrador");
        domium.criarCasa("u1", "c1", "Casa");
        return domium;
    }

    /**
     * Cria um domínio com utilizador, casa e divisão.
     *
     * @return domínio preparado para testes
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComUtilizadorCasaEDivisao() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorECasa();
        domium.adicionarDivisao("u1", "c1", "Sala");
        return domium;
    }

    /**
     * Cria um domínio mínimo com uma lâmpada.
     *
     * @return domínio preparado para testes
     * @throws DomusException se a preparação falhar
     */
    private DomiUM criarDominioComLampada() throws DomusException {
        DomiUM domium = criarDominioComUtilizadorCasaEDivisao();
        domium.adicionarDispositivo(
                "u1", "c1", "Sala", "lampada", "l1",
                "Philips", "Hue", 10.0
        );
        return domium;
    }
}
