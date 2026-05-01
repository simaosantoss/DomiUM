package domus.domain.commands;

import domus.domain.DomiUM;
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
            domium.executarOperacaoDispositivo(getUtilizadorId(), getCasaId(), getDispositivoId(), dispositivo -> {
                if (!(dispositivo instanceof PortaoGaragemInteligente)) {
                    return false;
                }

                ((PortaoGaragemInteligente) dispositivo).fechar();
                return true;
            });
        }
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
