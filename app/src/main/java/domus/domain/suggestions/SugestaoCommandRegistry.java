package domus.domain.suggestions;

import domus.domain.commands.ComandoAbrirPortao;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoDestrancarFechadura;
import domus.domain.commands.ComandoFecharPortao;
import domus.domain.commands.ComandoLigar;
import domus.domain.commands.ComandoTrancarFechadura;
import domus.domain.commands.Command;
import java.io.Serializable;

/**
 * Converte sugestões de escalonamento em comandos executáveis do domínio.
 *
 * Nesta fase apenas são suportadas ações simples sem parâmetros adicionais.
 */
public class SugestaoCommandRegistry implements Serializable {

    /**
     * Cria o comando correspondente a uma sugestão.
     *
     * Se a sugestão for inválida ou a ação textual não for suportada, é
     * devolvido {@code null}.
     *
     * @param utilizadorId identificador do utilizador associado ao comando
     * @param sugestao sugestão a converter
     * @return comando correspondente, ou {@code null} se não for possível
     *         converter
     */
    public Command criarComando(String utilizadorId, SugestaoEscalonamento sugestao) {
        if (utilizadorId == null || sugestao == null) {
            return null;
        }

        String casaId = sugestao.getCasaId();
        String dispositivoId = sugestao.getDispositivoId();
        String acao = sugestao.getAcao();
        if (casaId == null || dispositivoId == null || acao == null) {
            return null;
        }

        if ("Ligou o dispositivo".equals(acao)) {
            return new ComandoLigar(utilizadorId, casaId, dispositivoId);
        }
        if ("Desligou o dispositivo".equals(acao)) {
            return new ComandoDesligar(utilizadorId, casaId, dispositivoId);
        }
        if ("Abriu o portão".equals(acao)) {
            return new ComandoAbrirPortao(utilizadorId, casaId, dispositivoId);
        }
        if ("Fechou o portão".equals(acao)) {
            return new ComandoFecharPortao(utilizadorId, casaId, dispositivoId);
        }
        if ("Trancou a fechadura".equals(acao)) {
            return new ComandoTrancarFechadura(utilizadorId, casaId, dispositivoId);
        }
        if ("Destrancou a fechadura".equals(acao)) {
            return new ComandoDestrancarFechadura(utilizadorId, casaId, dispositivoId);
        }

        return null;
    }
}
