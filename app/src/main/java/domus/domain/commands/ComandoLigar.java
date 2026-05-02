package domus.domain.commands;

import domus.domain.DomiUM;

/**
 * Comando que liga um dispositivo através da fachada do domínio.
 */
public class ComandoLigar extends ComandoDispositivo {

    /**
     * Cria um comando para ligar um dispositivo.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoLigar(String utilizadorId, String casaId, String dispositivoId) {
        super(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoLigar(ComandoLigar outro) {
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
                dispositivo.ligar();
                return true;
            }, "Ligou o dispositivo");
        }
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoLigar clone() {
        return new ComandoLigar(this);
    }
}
