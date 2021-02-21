package br.com.zup.orangetalents.service;

import br.com.zup.orangetalents.repository.UsuarioRepository;
import br.com.zup.orangetalents.repository.VacinacaoRepository;
import br.com.zup.orangetalents.repository.entity.Usuario;
import br.com.zup.orangetalents.repository.entity.Vacinacao;
import br.com.zup.orangetalents.repository.entity.VacinacaoPk;
import br.com.zup.orangetalents.service.dto.VacinacaoRequest;
import br.com.zup.orangetalents.service.exception.EmailUsuarioNaoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VacinacaoServiceImpl implements VacinacaoService {

    @Autowired
    VacinacaoRepository vacinacaoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public VacinacaoRequest save(VacinacaoRequest vacinacaoRequest) throws EmailUsuarioNaoEncontrado {
        try {
            Usuario usuario = usuarioRepository.findByEmail(vacinacaoRequest.getEmailUsuario());
            Vacinacao vacinacao = new Vacinacao(new VacinacaoPk(vacinacaoRequest.getDataVacinacao(), vacinacaoRequest.getNome(), usuario));
            vacinacaoRepository.save(vacinacao);
            return vacinacaoRequest;
        } catch (DataIntegrityViolationException ex) {
            throw new EmailUsuarioNaoEncontrado();
        }
    }
}
