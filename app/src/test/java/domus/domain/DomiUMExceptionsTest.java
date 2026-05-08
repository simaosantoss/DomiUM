package domus.domain;

import domus.domain.commands.ComandoLigar;
import domus.domain.core.Utilizador;
import domus.domain.exceptions.DispositivoNaoExisteException;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.SemPermissaoException;
import domus.domain.history.RegistoInteracao;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressão das exceções de domínio expostas pela fachada DomiUM.
 */
class DomiUMExceptionsTest {

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
    void executarComandoValidadoValidoRegistaHistorico() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");

        domium.executarComandoValidado(new ComandoLigar("u1", "c1", "l1"));

        Utilizador utilizador = domium.getUtilizador("u1");
        assertNotNull(utilizador);
        assertTrue(historicoContemAcao(utilizador, "Ligou o dispositivo"));
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
