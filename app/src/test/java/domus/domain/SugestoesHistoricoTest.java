package domus.domain;

import domus.domain.commands.ComandoLigar;
import domus.domain.core.Casa;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.EscalonamentoJaExisteException;
import domus.domain.exceptions.OperacaoInvalidaException;
import domus.domain.suggestions.SugestaoEscalonamento;
import java.time.LocalTime;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressão das sugestões de escalonamento geradas a partir do
 * histórico de interações.
 */
class SugestoesHistoricoTest {

    @Test
    void geraSugestaoQuandoAcaoSeRepeteTresVezes() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");

        executarLigar(domium, "u1", "c1", "l1", 3);

        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("u1");

        assertTrue(iterador.hasNext());
        SugestaoEscalonamento sugestao = iterador.next();
        assertEquals("c1", sugestao.getCasaId());
        assertEquals("l1", sugestao.getDispositivoId());
        assertEquals("Ligou o dispositivo", sugestao.getAcao());
        assertTrue(sugestao.getOcorrencias() >= 3);
        assertNotNull(sugestao.getMensagem());
        assertFalse(sugestao.getMensagem().isEmpty());
    }

    @Test
    void naoGeraSugestaoAbaixoDoMinimoDeOcorrencias() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");

        executarLigar(domium, "u1", "c1", "l1", 2);

        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("u1");

        assertFalse(iterador.hasNext());
    }

    @Test
    void respeitaMinimoOcorrenciasConfiguravel() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");

        executarLigar(domium, "u1", "c1", "l1", 2);

        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("u1", 2, 5);

        assertTrue(iterador.hasNext());
        assertTrue(iterador.next().getOcorrencias() >= 2);
    }

    @Test
    void respeitaLimiteDeSugestoes() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Utilizador");
        domium.criarCasa("u1", "c1", "Casa");
        domium.adicionarDivisao("u1", "c1", "Sala");
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l1", "Philips", "Hue", 10.0);
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l2", "Philips", "Hue", 8.0);

        executarLigar(domium, "u1", "c1", "l1", 3);
        executarLigar(domium, "u1", "c1", "l2", 3);

        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("u1", 3, 1);

        assertEquals(1, contarSugestoes(iterador));
    }

    @Test
    void utilizadorInexistenteNaoGeraSugestoes() {
        DomiUM domium = new DomiUM();

        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("inexistente");

        assertFalse(iterador.hasNext());
    }

    @Test
    void aceitarSugestaoCriaEscalonamentoNaCasa() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        executarLigar(domium, "u1", "c1", "l1", 3);

        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("u1");
        assertTrue(iterador.hasNext());
        SugestaoEscalonamento sugestao = iterador.next();

        domium.aceitarSugestaoEscalonamento("u1", "esc1", "Ligar automaticamente", sugestao);

        Casa casa = domium.getCasa("c1");
        assertNotNull(casa);
        assertNotNull(casa.getEscalonamento("esc1"));
    }

    @Test
    void aceitarSugestaoDeOutroUtilizadorLancaOperacaoInvalida() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("admin", "Administrador");
        domium.criarCasa("admin", "c1", "Casa");
        domium.criarUtilizador("u2", "Utilizador");

        SugestaoEscalonamento sugestao = new SugestaoEscalonamento(
                "admin",
                "c1",
                "l1",
                "Ligou o dispositivo",
                LocalTime.of(10, 30),
                3,
                "Sugestão de teste"
        );

        assertThrows(OperacaoInvalidaException.class, () ->
                domium.aceitarSugestaoEscalonamento("u2", "esc1", "Ligar automaticamente", sugestao)
        );
    }

    @Test
    void aceitarSugestaoParaEscalonamentoExistenteLancaExcecao() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        executarLigar(domium, "u1", "c1", "l1", 3);
        Iterator<SugestaoEscalonamento> iterador = domium.getSugestoesEscalonamento("u1");
        assertTrue(iterador.hasNext());
        SugestaoEscalonamento sugestao = iterador.next();
        domium.criarEscalonamento(
                "u1", "c1", "esc1", "Escalonamento existente",
                LocalTime.of(9, 0), LocalTime.of(9, 1)
        );

        assertThrows(EscalonamentoJaExisteException.class, () ->
                domium.aceitarSugestaoEscalonamento("u1", "esc1", "Ligar automaticamente", sugestao)
        );
    }

    /**
     * Cria um domínio mínimo com uma lâmpada registada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     * @return fachada preparada para os testes
     */
    private DomiUM criarDominioComLampada(String utilizadorId, String casaId,
                                          String divisaoNome, String dispositivoId) throws DomusException {
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
     * Executa repetidamente o comando de ligar dispositivo.
     *
     * @param domium fachada do domínio
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param vezes número de execuções
     */
    private void executarLigar(DomiUM domium, String utilizadorId,
                               String casaId, String dispositivoId, int vezes) {
        for (int i = 0; i < vezes; i++) {
            domium.executarComando(new ComandoLigar(utilizadorId, casaId, dispositivoId));
        }
    }

    /**
     * Conta as sugestões disponibilizadas por um iterador.
     *
     * @param iterador iterador a percorrer
     * @return número de sugestões encontradas
     */
    private int contarSugestoes(Iterator<SugestaoEscalonamento> iterador) {
        int total = 0;
        while (iterador.hasNext()) {
            iterador.next();
            total++;
        }
        return total;
    }
}
