package domus.domain.commands;

import domus.domain.DomiUM;

/**
 * Comando que abre um portão de garagem através da fachada do domínio.
 */
public class ComandoAbrirPortao extends ComandoDispositivo {

    /**
     * Cria um comando para abrir um portão de garagem.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoAbrirPortao(String utilizadorId, String casaId, String dispositivoId) {
        super(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoAbrirPortao(ComandoAbrirPortao outro) {
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
            domium.abrirPortao(getUtilizadorId(), getCasaId(), getDispositivoId());
        }
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoAbrirPortao clone() {
        return new ComandoAbrirPortao(this);
    }
}
