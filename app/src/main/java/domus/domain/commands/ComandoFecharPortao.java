package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.Dispositivo;
import domus.domain.devices.PortaoGaragemInteligente;

/**
 * Comando que fecha um portão de garagem através da fachada do domínio.
 */
public class ComandoFecharPortao extends ComandoDispositivo {

    /**
     * Cria um comando para fechar um portão de garagem.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoFecharPortao(String utilizadorId, String casaId, String dispositivoId) {
        super(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoFecharPortao(ComandoFecharPortao outro) {
        super(outro);
    }

    /**
     * Executa o comando sobre a fachada indicada.
     *
     * @param domium fachada do domínio
     */
    @Override
    public void execute(DomiUM domium) {
        if (domium != null) {
            domium.executarOperacaoDispositivoComDescricao(getUtilizadorId(), getCasaId(), getDispositivoId(), dispositivo -> {
                if (!(dispositivo instanceof PortaoGaragemInteligente)) {
                    return false;
                }

                ((PortaoGaragemInteligente) dispositivo).fechar();
                return true;
            }, "Fechou o portão");
        }
    }

    /**
     * Verifica se o comando pode ser aplicado ao dispositivo indicado.
     *
     * @param dispositivo dispositivo a validar
     * @return {@code true} se o dispositivo for um portão de garagem inteligente
     */
    @Override
    public boolean suportaDispositivo(Dispositivo dispositivo) {
        return dispositivo instanceof PortaoGaragemInteligente;
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoFecharPortao clone() {
        return new ComandoFecharPortao(this);
    }
}
