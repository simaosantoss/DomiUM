package domus.domain.core;

import java.io.Serializable;

/**
 * Define os níveis de permissão possíveis para um utilizador numa casa do
 * sistema.
 */
public enum TipoPermissao implements Serializable {

    /**
     * Permissão de administração, com acesso total sobre a casa.
     */
    ADMIN,

    /**
     * Permissão de utilização normal, adequada a operações correntes.
     */
    NORMAL
}
