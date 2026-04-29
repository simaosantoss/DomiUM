package domus.domain.factories;

import domus.domain.devices.Dispositivo;
import domus.domain.devices.FechaduraInteligente;

/**
 * Factory responsável pela criação de fechaduras inteligentes.
 */
public class FechaduraInteligenteFactory implements DispositivoFactory {

    /**
     * Cria uma nova fechadura inteligente com os dados base fornecidos.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return nova fechadura inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new FechaduraInteligente(id, marca, modelo, consumo);
    }
}
