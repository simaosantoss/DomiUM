package domus.domain;

import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.core.TipoPermissao;
import domus.domain.devices.Dispositivo;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.SemPermissaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de regressão da gestão de permissões de utilizadores por casa.
 */
class DomiUMPermissoesTest {

    @Test
    void administradorPodeAtribuirPermissaoNormal() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarUtilizador("u2", "Utilizador Normal");

        domium.atribuirPermissaoCasa("u1", "c1", "u2", TipoPermissao.NORMAL);
        domium.executarComandoValidado(new ComandoLigar("u2", "c1", "l1"));
        domium.executarComandoValidado(new ComandoDesligar("u2", "c1", "l1"));

        Dispositivo dispositivo = domium.getDispositivo("c1", "l1");
        assertNotNull(dispositivo);
        assertFalse(dispositivo.isLigado());
    }

    @Test
    void utilizadorNormalNaoPodeAdministrarCasa() throws DomusException {
        DomiUM domium = criarDominioComLampada("u1", "c1", "Sala", "l1");
        domium.criarUtilizador("u2", "Utilizador Normal");
        domium.atribuirPermissaoCasa("u1", "c1", "u2", TipoPermissao.NORMAL);

        assertThrows(SemPermissaoException.class, () ->
                domium.adicionarDivisao("u2", "c1", "Cozinha")
        );
        assertThrows(SemPermissaoException.class, () ->
                domium.adicionarDispositivo(
                        "u2", "c1", "Sala", "lampada", "l2",
                        "Philips", "Hue", 8.0
                )
        );
    }

    @Test
    void administradorPodeAtribuirPermissaoAdmin() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Administrador");
        domium.criarUtilizador("u2", "Segundo Administrador");
        domium.criarUtilizador("u3", "Utilizador Normal");
        domium.criarCasa("u1", "c1", "Casa");

        domium.atribuirPermissaoCasa("u1", "c1", "u2", TipoPermissao.ADMIN);
        domium.atribuirPermissaoCasa("u2", "c1", "u3", TipoPermissao.NORMAL);
        domium.criarCenario("u3", "c1", "cinema", "Ver Cinema");

        assertTrue(domium.getIteradorCenarios("c1").hasNext());
    }

    @Test
    void utilizadorSemAdminNaoPodeAtribuirPermissao() throws DomusException {
        DomiUM domium = new DomiUM();
        domium.criarUtilizador("u1", "Administrador");
        domium.criarUtilizador("u2", "Utilizador Sem Permissao");
        domium.criarCasa("u1", "c1", "Casa");

        assertThrows(SemPermissaoException.class, () ->
                domium.atribuirPermissaoCasa("u2", "c1", "u2", TipoPermissao.NORMAL)
        );
    }

    /**
     * Cria um domínio mínimo com uma lâmpada registada.
     *
     * @param utilizadorId identificador do utilizador administrador
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
        domium.criarUtilizador(utilizadorId, "Administrador");
        domium.criarCasa(utilizadorId, casaId, "Casa");
        domium.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        domium.adicionarDispositivo(
                utilizadorId, casaId, divisaoNome, "lampada", dispositivoId,
                "Philips", "Hue", 10.0
        );
        return domium;
    }
}
