package domus.domain.commands;

import domus.domain.DomiUM;

/**
 * Comando que desliga um dispositivo através da fachada do domínio.
 */
public class ComandoDesligar extends ComandoDispositivo {

    /**
     * Cria um comando para desligar um dispositivo.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoDesligar(String utilizadorId, String casaId, String dispositivoId) {
        super(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDesligar(ComandoDesligar outro) {
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
                dispositivo.desligar();
                return true;
            }, "Desligou o dispositivo");
        }
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDesligar clone() {
        return new ComandoDesligar(this);
    }
}
