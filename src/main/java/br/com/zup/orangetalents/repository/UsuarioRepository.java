package br.com.zup.orangetalents.repository;

import br.com.zup.orangetalents.repository.entity.Usuario;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
