package br.com.zup.orangetalents.service;

import br.com.zup.orangetalents.service.dto.VacinacaoRequest;
import br.com.zup.orangetalents.service.exception.EmailUsuarioNaoEncontrado;

public interface VacinacaoService {
    VacinacaoRequest save(VacinacaoRequest vacinacaoRequest) throws EmailUsuarioNaoEncontrado;
}
