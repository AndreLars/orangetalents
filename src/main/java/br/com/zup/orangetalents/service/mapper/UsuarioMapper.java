package br.com.zup.orangetalents.service.mapper;

import br.com.zup.orangetalents.repository.entity.Usuario;
import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario usuarioRequestToUsuario(UsuarioRequest usuarioRequest);
}
