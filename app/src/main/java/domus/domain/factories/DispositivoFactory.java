package domus.domain.factories;

import domus.domain.devices.Dispositivo;

/**
 * Define o contrato de criação de dispositivos no sistema DomusControl.
 *
 * Cada implementação desta interface conhece a forma de instanciar um tipo
 * concreto de dispositivo a partir dos dados base comuns ao domínio.
 */
public interface DispositivoFactory {

    /**
     * Cria um novo dispositivo com base nos dados fornecidos.
     *
     * A criação concreta depende da implementação da factory, que poderá
     * complementar os dados recebidos com valores por defeito adequados ao tipo
     * de dispositivo que representa.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return novo dispositivo criado pela factory
     */
    Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo);
}
