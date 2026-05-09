package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.Dispositivo;
import domus.domain.devices.FechaduraInteligente;

/**
 * Comando que destranca uma fechadura através da fachada do domínio.
 */
public class ComandoDestrancarFechadura extends ComandoDispositivo {

    /**
     * Cria um comando para destrancar uma fechadura.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoDestrancarFechadura(String utilizadorId, String casaId, String dispositivoId) {
        super(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDestrancarFechadura(ComandoDestrancarFechadura outro) {
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

                ((FechaduraInteligente) dispositivo).destrancar();
                return true;
            }, "Destrancou a fechadura");
        }
    }

    /**
     * Verifica se o comando pode ser aplicado ao dispositivo indicado.
     *
     * @param dispositivo dispositivo a validar
     * @return {@code true} se o dispositivo for uma fechadura inteligente
     */
    @Override
    public boolean suportaDispositivo(Dispositivo dispositivo) {
        return dispositivo instanceof FechaduraInteligente;
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDestrancarFechadura clone() {
        return new ComandoDestrancarFechadura(this);
    }
}
