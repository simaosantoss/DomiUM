package domus.domain;

import domus.domain.commands.ComandoLigar;
import domus.domain.core.Casa;
import domus.domain.core.Utilizador;
import domus.domain.history.RegistoInteracao;
import domus.domain.statistics.ResumoCasaConsumo;
import domus.domain.statistics.ResumoDivisaoDispositivos;
import domus.domain.suggestions.SugestaoEscalonamento;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressão da fachada principal do domínio DomusControl.
 */
class DomiUMTest {

    /**
     * Diretório temporário usado pelos testes de persistência.
     */
    @TempDir
    private Path tempDir;

    @Test
    void criarCasaAtribuiPermissaoAdminEAceitaAdicionarDivisao() {
        DomiUM domium = new DomiUM();

        domium.criarUtilizador("u1", "Simao");
        domium.criarCasa("u1", "c1", "Casa Principal");
        domium.adicionarDivisao("u1", "c1", "Sala");

        Casa casa = domium.getCasa("c1");
        assertNotNull(casa);
        assertTrue(casa.contemDivisao("Sala"));
    }

    @Test
    void adicionarDispositivoEExecutarComandoLigarRegistaHistorico() {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);

        domium.executarComando(new ComandoLigar("u1", "c1", "l1"));

        Utilizador utilizador = domium.getUtilizador("u1");
        assertNotNull(utilizador);
        assertTrue(historicoContemAcao(utilizador, "Ligou o dispositivo"));
    }

    @Test
    void comandoSemPermissaoNaoRegistaHistorico() {
        DomiUM domium = criarDominioComLampada("admin", "c1", "Sala", "l1", 10.0);
        domium.criarUtilizador("u2", "Utilizador Sem Permissao");

        domium.executarComando(new ComandoLigar("u2", "c1", "l1"));

        Utilizador utilizador = domium.getUtilizador("u2");
        assertNotNull(utilizador);
        assertFalse(utilizador.getIteradorHistorico().hasNext());
    }

    @Test
    void cenarioExecutaComandosERegistaHistorico() {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);

        domium.criarCenario("u1", "c1", "noite", "Modo Noite");
        domium.adicionarComandoACenario("u1", "c1", "noite", new ComandoLigar("u1", "c1", "l1"));
        domium.executarCenario("u1", "c1", "noite");

        Utilizador utilizador = domium.getUtilizador("u1");
        assertNotNull(utilizador);
        assertTrue(historicoContemAcao(utilizador, "Ligou o dispositivo"));
    }

    @Test
    void persistenciaGuardaECarregaEstadoBasico() throws IOException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);
        Path ficheiro = this.tempDir.resolve("estado.dat");

        domium.gravarEstado(ficheiro.toString());
        DomiUM carregado = DomiUM.carregarEstado(ficheiro.toString());

        assertNotNull(carregado.getCasa("c1"));
        assertNotNull(carregado.getUtilizador("u1"));

        Files.deleteIfExists(ficheiro);
    }

    @Test
    void estatisticasDevolvemCasaMaiorConsumo() {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Simao");
        domium.criarCasa("u1", "c1", "Casa Baixo Consumo");
        domium.adicionarDivisao("u1", "c1", "Sala");
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l1", "Philips", "Hue", 5.0);
        domium.criarCasa("u1", "c2", "Casa Alto Consumo");
        domium.adicionarDivisao("u1", "c2", "Sala");
        domium.adicionarDispositivo("u1", "c2", "Sala", "lampada", "l2", "Philips", "Hue", 20.0);

        ResumoCasaConsumo resumo = domium.getCasaMaiorConsumo();

        assertNotNull(resumo);
        assertEquals("c2", resumo.getCasaId());
    }

    @Test
    void topDivisoesComMaisDispositivosDevolveResultados() {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Simao");
        domium.criarCasa("u1", "c1", "Casa Principal");
        domium.adicionarDivisao("u1", "c1", "Sala");
        domium.adicionarDivisao("u1", "c1", "Cozinha");
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l1", "Philips", "Hue", 10.0);
        domium.adicionarDispositivo("u1", "c1", "Sala", "lampada", "l2", "Philips", "Hue", 8.0);
        domium.adicionarDispositivo("u1", "c1", "Cozinha", "lampada", "l3", "Philips", "Hue", 6.0);

        Iterator<ResumoDivisaoDispositivos> iterador = domium.getTop3DivisoesComMaisDispositivos();

        assertTrue(iterador.hasNext());
        ResumoDivisaoDispositivos primeiro = iterador.next();
        assertEquals("Sala", primeiro.getNomeDivisao());
        assertEquals(2, primeiro.getNumeroDispositivos());
    }

    @Test
    void sugestoesEscalonamentoSaoGeradasComAcoesRepetidas() {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1", 10.0);

        domium.executarComando(new ComandoLigar("u1", "c1", "l1"));
        domium.executarComando(new ComandoLigar("u1", "c1", "l1"));
        domium.executarComando(new ComandoLigar("u1", "c1", "l1"));

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

    /**
     * Cria um domínio mínimo com utilizador, casa, divisão e uma lâmpada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     * @param consumo consumo base por hora
     * @return fachada preparada para testes
     */
    private DomiUM criarDominioComLampada(String utilizadorId, String casaId,
                                          String divisaoNome, String dispositivoId,
                                          double consumo) {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador(utilizadorId, "Utilizador");
        domium.criarCasa(utilizadorId, casaId, "Casa");
        domium.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        domium.adicionarDispositivo(
                utilizadorId, casaId, divisaoNome, "lampada", dispositivoId,
                "Philips", "Hue", consumo
        );
        return domium;
    }

    /**
     * Verifica se o histórico de um utilizador contém uma ação.
     *
     * @param utilizador utilizador a analisar
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
