package domus.demo;

import domus.domain.DomiUM;
import domus.domain.core.Casa;
import domus.domain.exceptions.DomusException;
import domus.domain.statistics.ResumoDivisaoDispositivos;
import domus.domain.suggestions.SugestaoEscalonamento;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes do estado de demonstração usado pela aplicação de consola.
 */
class EstadoDemonstracaoTest {

    @Test
    void popularCriaEstadoDemonstracaoComDadosPrincipais() throws DomusException {
        DomiUM model = new DomiUM();

        EstadoDemonstracao.popular(model);

        assertNotNull(model.getUtilizador("demo_u1"));

        Casa casa = model.getCasa("demo_c1");
        assertNotNull(casa);
        assertTrue(casa.contemDivisao("Sala"));
        assertTrue(casa.contemDivisao("Quarto"));
        assertTrue(casa.contemDivisao("Garagem"));
        assertNotNull(casa.getCenario("demo_cenario_noite"));
        assertNotNull(casa.getEscalonamento("demo_esc_manha"));

        Iterator<SugestaoEscalonamento> sugestoes = model.getSugestoesEscalonamento("demo_u1");
        assertTrue(sugestoes.hasNext());

        Iterator<ResumoDivisaoDispositivos> divisoes = model.getTop3DivisoesComMaisDispositivos();
        assertTrue(divisoes.hasNext());
    }
}
