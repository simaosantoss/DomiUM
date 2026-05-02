package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.FechaduraInteligente;

/**
 * Comando que tranca uma fechadura através da fachada do domínio.
 */
public class ComandoTrancarFechadura extends ComandoDispositivo {

    /**
     * Cria um comando para trancar uma fechadura.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoTrancarFechadura(String utilizadorId, String casaId, String dispositivoId) {
        super(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoTrancarFechadura(ComandoTrancarFechadura outro) {
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
                if (!(dispositivo instanceof FechaduraInteligente)) {
                    return false;
                }

                ((FechaduraInteligente) dispositivo).trancar();
                return true;
            }, "Trancou a fechadura");
        }
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoTrancarFechadura clone() {
        return new ComandoTrancarFechadura(this);
    }
}
