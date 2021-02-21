# Desafio Orange Talents

Olá! Hoje nós vamos ver como implementamos uma API usando **Java** como linguagem e **Spring + Hibernate** como stacks de tecnologia core da aplicação.


## Contexto do desafio

> Você está fazendo uma API REST que precisará controlar a aplicação de vacinas entre a população brasileira. O primeiro passo deve ser a construção de um cadastro de usuários, sendo obrigatórios dados como: nome, e-mail, CPF e data de nascimento, onde e-mail e CPF devem ser únicos.  
> 
> O segundo passo é criar um cadastro de aplicação de vacinas, sendo obrigatórios dados como: nome da vacina, e-mail do usuário e a data que foi realizada a vacina.
> 
>  Você deve construir apenas dois endpoints neste sistema, o cadastro do usuário e o cadastro da aplicação da vacina. Caso os cadastros estejam corretos, é necessário voltar o Status 201, caso hajam erros de preenchimento de dados, o Status deve ser 400.


## Proposta de solução

O desafio então é uma api simples com só o C do CRUD em dois endpoints. Em vista da simplicidade do desafio vou buscar trazer uma solução rápida sem fugir do escopo utilizando os próprios recursos que o Spring + Hibernate nos dão. Vamos então desenvolver uma aplicação local com todos os requisitos e iniciar o projeto!

## Iniciando o projeto

O primeiro passo é iniciar o projeto, para isso vamos usar o [Spring Initializr](https://start.spring.io/). Então iniciei com as configurações a seguir:

![Spring Initializr](https://i.ibb.co/C84f7Lx/spring.png)

### Spring
Spring Web é o módulo básico do mundo Spring quando falamos de aplicações REST. Já o Spring Data JPA traz para nós tanto o Spring Data e o Hibernate, ou seja, vai ser usado o JPA para persistir dados em um banco relacional de sua preferência e o Hibernate como nosso ORM. A vantagem do Spring Data é que ele traz para nós interfaces como a que vamos usar: Repository, que vai agilizar o desenvolvimento de qualquer DML sem escrever uma linha de SQL.

### Java 8 vs Java 11

O bom e confiável Java 8 vai ficar para trás dessa vez. Pensando a longo prazo o essa versão embora em LTS não tem mais novas features, muitas empresas já estão migrando do Java 8 para o Java 11 antes que vire mais um legado. Por isso escolhi o Java 11 para esse projeto, uma versão tão estável quanto a 8 mas com uma vida útil bem maior.

### H2

O banco de dados em memória do H2 será muito útil para nós acelerarmos o desenvolvimento da aplicação pensando que nosso desenvolvimento não vai para um ambiente produtivo ainda. Se essa aplicação for implantada em um serviço de cloud público, por exemplo, AWS, nós poderíamos usar o próprio recurso de RDS com outro banco relacional, como um MySQL, fica a gosto do freguês. Mas isso não está no nosso escopo.


### "Arquitetura"

Aqui não vamos complicar, poderiamos até pensar em uma arquitetura da moda, como *Clean Architecture*, Arquitetura Hexagonal e até *Ports and Adapters*, porém para uma aplicação tão pequena, podemos seguir com um MVC simples, sem o View só o MC já que é API REST back-end. O desenho do fluxo seria mais ou menos assim:

![Arquitetura](https://i.ibb.co/rZmmkmv/arquitetura.png)

Portanto, teremos 3 camadas na aplicação, a Controller que vai ser responsável de expor os endpoints da API e conterá os DTOs, que são os objetos que vamos expor para os serviços consumidores da API, a camada Service que será o meio de comunicação entre a Controller e a Repository e conterá as regras de negócio da aplicação, por último a Repository será responsável de comunicar com o banco através do Hibernate + JPA, nela conterá também as entidades da aplicação.
### Classe Principal

#### OrangetalentsApplication.java
```java
@SpringBootApplication  
public class OrangetalentsApplication {  

	public static void main(String[] args) {  
		SpringApplication.run(OrangetalentsApplication.class, args);  
	}  

}
```

### Entidades

#### Usuario.java
```java
@Entity  
@Table(name = "USUARIOS")  
public class Usuario {  

	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO)  
	@Column(name = "id")  
	private Long id;  

	@NotNull  
	@Column(name = "cpf", unique = true)  
	private String cpf;  

	@NotNull  
	@Column(name = "data_nascimento")  
	private Date dataNascimento;  

	@NotNull  
	@Column(name = "email", unique = true)  
	private String email;  

	@NotNull  
	@Column(name = "nome")  
	private String nome;  

	// Construtores, getters e setters ... //

}
```
#### Vacinacao.java
```java
@Entity  
@Table(name = "VACINACAO")  
public class Vacinacao {  

	@EmbeddedId  
	private VacinacaoPk pk;  

	// Construtores, getters e setters ... //
}
```
#### VacinacaoPk.java
```java
@Embeddable  
public class VacinacaoPk implements Serializable {  

	private static final long serialVersionUID = 1L;  

	@Column(name = "data_vacinacao")  
	private Date dataVacinacao;  

	@Column(name = "nome_vacina")  
	private String nome;  

	@ManyToOne(fetch = FetchType.LAZY)  
	@JoinColumn(name = "usuario_id")  
	private Usuario usuario;  

	// Construtores, getters e setters ... //

	@Override  
	public boolean equals(Object o) {  
		if (this == o) return true;  
		if (o == null || getClass() != o.getClass()) return false;  

		VacinacaoPk that = (VacinacaoPk) o;  

		if (getDataVacinacao() != null ? !getDataVacinacao().equals(that.getDataVacinacao()) : that.getDataVacinacao() != null)  
			return false;  
		if (getNome() != null ? !getNome().equals(that.getNome()) : that.getNome() != null) return false;  
		return getUsuario() != null ? getUsuario().equals(that.getUsuario()) : that.getUsuario() == null;  
	}  

	@Override  
	public int hashCode() {  
		int result = getDataVacinacao() != null ? getDataVacinacao().hashCode() : 0;  
		result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);  
		result = 31 * result + (getUsuario() != null ? getUsuario().hashCode() : 0);  
		return result;  
	}  
}
```

Não há nada de novo nas nossas entidades, somente o bom e sólido JPA. 

Mas por qual razão temos 3 classes ao invés de duas, já que temos só duas entidades nos requisitos? 
- Bom, a entidade  **Vacinação** tem uma chave primária composta, uma das maneiras é criar uma classe com a anotação **@Embeddable**  e nela vai conter os campos da chave composta que vai ser referenciada na classe pai com a anotação **@EmbeddedId**. 

- Outro ponto notável é a anotação **@ManyToOne**,  ela permite mapear a coluna chave estrangeira no mapeamento da entidade filha para que a filha tenha uma referência de objeto da sua entidade pai. Existem outras maneiras para fazer esse mapeamento, poderiamos escolher um mapeamento bidirecional, inserindo uma coleção de entidades filhas dentro da entidade pai, com esse propósito o JPA oferece a anotação **@OneToMany**.

- No nosso escopo se realmente fosse necessário poderíamos realizar uma Query simples que também nos dá oportunidades de otimizar a performance.

- Mais um pouco do básico, toda classe **@Embeddable** a documentação do JPA define que a classe precisa implementar a interface **Serializable** junto com os métodos **equals()** e **hashCode()**.

- A anotação **@ManyToOne** usa **FetchType.LAZY** senão nós aceitamos o padrão **FetchType.EAGER** que é horrível para a performance, teríamos o problema N+1 que é bem melhor explicado [aqui](https://vladmihalcea.com/n-plus-1-query-problem/).
### DDL gerado pelo Hibernate
```sql
drop table if exists USUARIOS CASCADE 


drop table if exists VACINACAO CASCADE 


drop sequence if exists hibernate_sequence
create sequence hibernate_sequence start with 1 increment by 1

create table USUARIOS (
	id bigint not null,
	cpf varchar(255) not null,
	data_nascimento timestamp not null,
	email varchar(255) not null,
	nome varchar(255) not null,
	primary key (id)
)

create table VACINACAO (
	data_vacinacao timestamp not null,
	nome_vacina varchar(255) not null,
	usuario_id bigint not null,
	primary key (data_vacinacao, nome_vacina, usuario_id)
)

alter table USUARIOS 
add constraint UK_9cpfdo5rgf5myc2uintsipove unique (cpf)

alter table USUARIOS 
add constraint UK_e712h12jepnwbmosco0e6hpqk unique (email)

alter table VACINACAO 
add constraint FK20c9jbo0vykdmuxr8inliira8 
foreign key (usuario_id) 
references USUARIOS
```
### Repository
#### BaseRepository.java
```java
@NoRepositoryBean  
public interface BaseRepository<T, ID> extends Repository<T, ID> {  
    <S extends T> S save(S entity);  
}
```
#### UsuarioRepository.java
```java
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {  
    Usuario findByEmail(String email);  
}
```
#### VacinacaoRepository.java
```java
public interface VacinacaoRepository extends BaseRepository<Vacinacao, VacinacaoPk> {  
}
```

Primeiro criei a interface **BaseRepository.java** que estende da interface do **Repository** do Spring Data, que vai gerar todo o SQL necessário do método **save()**, o INSERT em qualquer tabela do banco.

A anotação **@NoRepositoryBean** define pro Spring que a nossa interface não é um Bean, mas sim vamos usar ela para implementar os repositórios que realmente vamos usar.

Dentro do **UsuarioRepository.java** nós definimos mais um método, o **findByEmail()** que executará uma consulta na tabela de usuários passando o e-mail do usuário, o e-mail vai ser fornecido na chamada da API para cadastrar uma vacinação daquele usuário. 

Já na classe **VacinacaoRepository** não definimos nenhum método adicional pois vamos usar somente o método **save()** que estendemos do BaseRepository.

Por que não usamos o **Spring Data REST**?
- O Spring Data REST também é uma maneira muito rápida de criar endpoints REST de um CRUD básico. Porém na nossa aplicação só precisamos expor o Create, não é seguro expor endpoints que não queremos que fiquem expostos. Então, usar o Spring Data REST e bloquear as rotas que não queremos é mais trabalhoso do que a maneira como estamos fazendo. Caso necessário, é muito simples adicionarmos outras rotas para a nossa aplicação e temos o total controle que estamos usando somente o necessário.

### DTO
#### UsuarioRequest.java
```java
public class UsuarioRequest {  
	
	@CPF  
	@NotBlank  
	private String cpf;  

	@NotNull  
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")  
	private Date dataNascimento;  

	@Email  
	@NotBlank  
	private String email;  

	@NotBlank  
	private String nome;  

	// Construtores, getters e setters ... //

}
```
#### VacinacaoRequest.java
```java
public class VacinacaoRequest {  

	@NotNull  
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")  
	private Date dataVacinacao;  

	@Email  
	@NotBlank  
	private String emailUsuario;  

	@NotBlank  
	private String nome;
	
	// Construtores, getters e setters ... //
}
```
DTO ou Objeto de Transferência de Dados aqui estão sendo utilizados para definir os dados que serão transferido durante as requisições HTTP da nossa REST API, assim temos o controle de quais dados queremos expor para as outras aplicações que vão consumir a nossa API. **UsuarioRequest.java** e **VacinacaoResquest.java** são os POJOs dos nossos endpoints, os dados do cadastro de usuário e vacinação que vão ser enviados em forma de JSON no body das requests e enviados de volta no response.

Aqui usei o BeanValidation para realizar a validação desses dados na ponta do cliente, caso os dados não estejam preenchidos corretamente, a aplicação vai devolver um Bad Request, para nós brasileiros existe o hibernate.validator.constraints.br.CPF que já valida a String de entrada como um CPF válido.

### MapStruct
#### UsuarioMapper.java
```java
@Mapper(componentModel = "spring")  
public interface UsuarioMapper {  
    Usuario usuarioRequestToUsuario(UsuarioRequest usuarioRequest);  
}
```
#### UsuarioMapperImpl.java
```java
@Generated(  
value = "org.mapstruct.ap.MappingProcessor",  
date = "2021-02-20T01:23:56-0300",  
comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.8 (JetBrains s.r.o.)"
)  
@Component  
public class UsuarioMapperImpl implements UsuarioMapper {  
	
	@Override  
	public Usuario usuarioRequestToUsuario(UsuarioRequest usuarioRequest) {  
		if ( usuarioRequest == null ) {  
			return null;  
		}  

		Usuario usuario = new Usuario();  

		usuario.setCpf( usuarioRequest.getCpf() );  
		usuario.setDataNascimento( usuarioRequest.getDataNascimento() );  
		usuario.setEmail( usuarioRequest.getEmail() );  
		usuario.setNome( usuarioRequest.getNome() );  

		return usuario;  
	}  
}
```

MapStruct é uma biblioteca muito útil para mapear objetos, no nosso caso precisamos mapear o DTO do usuário para a entidade.
- Primeiro precisamos criar uma interface e inserir a anotação **@Mapper**, e dentro dessa interface definimos a assinatura do método que receberá um objeto da classe de partida e retornará um objeto da classe alvo.
- No arquivo **UsuarioMapper.java** nós definimos o método **usuarioRequestToUsuario()** que vai transformar um objeto **UsuarioRequest** para a nossa entidade **Usuario**.
- Depois com um mvn clean install o MapStruct vai gerar automaticamente a implementação dessa interface, como podemos ver na classe **UsuarioMapperImpl.java**. Como as duas classes tem atributos com o mesmo nome não precisamos explicitar qual atributo é qual na classe referente, mas poderiamos fazer essa referência se fosse necessário.
### Exception
#### EmailUsuarioNaoEncontrado.java
```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "E-mail do usuário não encontrado.")  
public class EmailUsuarioNaoEncontrado extends Exception {  }
```
#### UsuarioJaExisteException.java
```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Usuário já existe.")  
public class UsuarioJaExisteException extends Exception { }
```
Aqui criei exceções personalizadas para os dois casos de violação na nossa base, ao invés de lançar uma exceção do banco para o cliente, podemos personalizar nossas exceções junto com o status HTTP graças a anotação **@ResponseStatus** do Spring Web, essas exceções serão lançadas na camada Service.

### Service
#### UsuarioService.java
```java
public interface UsuarioService {  
    UsuarioRequest save(UsuarioRequest usuarioRequest) throws UsuarioJaExisteException;  
}
```
#### UsuarioServiceImpl.java
```java
@Service  
public class UsuarioServiceImpl implements UsuarioService {  
	
	@Autowired  
	UsuarioRepository usuarioRepository;  

	@Autowired  
	UsuarioMapper usuarioMapper;  

	@Override  
	public UsuarioRequest save(UsuarioRequest usuarioRequest) throws UsuarioJaExisteException {  
		try {  
			usuarioRepository.save(usuarioMapper.usuarioRequestToUsuario(usuarioRequest));  
			return usuarioRequest;  
		} catch (DataIntegrityViolationException ex) {  
			throw new UsuarioJaExisteException();  
		}  
	} 
}
```
#### VacinacaoService.java
```java
public interface VacinacaoService {  
    VacinacaoRequest save(VacinacaoRequest vacinacaoRequest) throws EmailUsuarioNaoEncontrado;  
}
```
#### VacinacaoServiceImpl.java
```java
@Service  
public class VacinacaoServiceImpl implements VacinacaoService {  

	@Autowired  
	VacinacaoRepository vacinacaoRepository;  

	@Autowired  
	UsuarioRepository usuarioRepository;  

	@Override  
	public VacinacaoRequest save(VacinacaoRequest vacinacaoRequest) {  
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
```
Os services contém as regras de negócio da nossa aplicação. Neles são injetadas as dependências com a anotação **@Autowired** e definimos o Bean como **@Service** pois serão injetados nos **Controllers** da aplicação. 
- No **UsuarioServiceImpl.java** foram injetados o mapper e o repository correspondente, primeiro então mapeando a requisição do cliente para a nossa entidade e depois chamando o método **save()** para persistir os dados no banco.
- No **VacinacaoServiceImpl.java** foram injetados tanto o repository de usuários quanto de vacinação. Primeiro precisamos consultar o usuário referente ao e-mail passado na requisição para depois gravar a vacinação no banco.

### Controller
#### UsuarioController.java
```java
@RestController  
@Validated  
public class UsuarioController {  

	@Autowired  
	private UsuarioService usuarioService;  

	@ResponseStatus(HttpStatus.CREATED)  
	@PostMapping("orangetalents/v1/usuario")  
	public ResponseEntity<UsuarioRequest> postUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {  
		try {  
		    return new ResponseEntity<>(this.usuarioService.save(usuarioRequest), HttpStatus.CREATED);  
		} catch (UsuarioJaExisteException ex) {  
		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe.", ex);  
		} 
	}  
}
```
#### VacinacaoController.java
```java
@RestController  
public class VacinacaoController {  

	@Autowired  
	VacinacaoService vacinacaoService;  

	@ResponseStatus(HttpStatus.CREATED)  
	@PostMapping("orangetalents/v1/vacinacao")  
	public ResponseEntity<VacinacaoRequest> postUsuario(@Valid @RequestBody VacinacaoRequest vacinacaoRequest) {  
		try {  
		    return new ResponseEntity<>(this.vacinacaoService.save(vacinacaoRequest), HttpStatus.CREATED);  
		} catch (EmailUsuarioNaoEncontrado ex) {  
		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail do usuário não encontrado.", ex);  
		} 
	}  
}
```
Nos controllers são definidos os endpoints da nossa REST API.
- Em cada um injetamos o seu respectivo **@Service**.
- Definimos o status HTTP que queremos retornar para o cliente com a anotação **@ResponseStatus(HttpStatus.CREATED)**.
- A anotação **@PostMapping()** mapeia o método POST junto com a URI da rota desejada.
- A anotação **@Valid** faz o BeanValidation de acordo com as anotações que definimos nos DTOs anteriormente.

### Testes de Unidade

Os testes de unidade existem para testar uma unidade (ba dum ts) de código, ou seja, diferente dos testes funcionais que vão testar as funcionalidades esperadas da aplicação, ou integrados que vão testar a integração da aplicação com outras aplicações como um banco de dados ou outro serviço, os testes de unidade vão testar uma única classe e seus métodos, se eles se comportam como deveria, *mockando* quaisquer outras unidades injetadas.

Esse tipo de teste é importantíssimo para garantir a integridade do código já existente. Toda vez que a aplicação sofrer uma melhoria, refatoração, etc, devemos garantir que a unidade de código continua realizando o que deveria estar fazendo, senão o teste quebra.

Então o teste de unidade é a base da pirâmide de testes que uma aplicação se submete e essa etapa normalmente é desenvolvida pelos próprios desenvolvedores envolvidos. O mais importante é entender que esse teste deve testar as regras de negócio da aplicação, muitas vezes a empresa ou o desenvolvedor se ilude com a cobertura do código e como meta quer cobrir 100% das linhas de código, mas só isso não garante que as regras de negócio estão sendo devidamente testadas.

Abaixo você verá várias anotações do Spring e do JUnit para a configuração dos testes de unidade rodarem devidamente.


#### UsuarioControllerTest.java
```java
@RunWith(SpringRunner.class)  
@WebMvcTest(UsuarioController.class)  
@AutoConfigureMockMvc  
public class UsuarioControllerTest {  

	@MockBean  
	UsuarioService usuarioService;  

	@Autowired  
	private MockMvc mockMvc;  

	ObjectMapper mapper = new ObjectMapper();  

	@Test  
	public void deve_retornar_status_http_criado() throws Exception {  
		String expectedDataNascimento = "19/02/2021";  
		UsuarioRequest request = new UsuarioRequest();  
		request.setCpf("31856332020");  
		request.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(expectedDataNascimento));  
		request.setEmail("test@email");  
		request.setNome("Test");  

		when(usuarioService.save(any(UsuarioRequest.class))).thenReturn(request);  

		mockMvc.perform(post("/orangetalents/v1/usuario")  
				.content(mapper.writeValueAsString(request))  
				.contentType(MediaType.APPLICATION_JSON))  
				.andExpect(status().isCreated())  
				.andExpect(jsonPath("$.cpf").value(request.getCpf()))  
				.andExpect(jsonPath("$.dataNascimento").value(expectedDataNascimento))  
				.andExpect(jsonPath("$.email").value(request.getEmail()))  
				.andExpect(jsonPath("$.nome").value(request.getNome()));  
	}  
}
```
#### UsuarioRepositoryTest.java
```java
package br.com.zup.orangetalents;  
  
import br.com.zup.orangetalents.repository.UsuarioRepository;  
import br.com.zup.orangetalents.repository.entity.Usuario;  
import org.junit.jupiter.api.BeforeEach;  
import org.junit.jupiter.api.Test;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;  
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;  
import org.springframework.dao.DataIntegrityViolationException;  
  
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
  
import static org.junit.Assert.assertThrows;  
  
@DataJpaTest  
class UsuarioRepositoryTest {  

	@Autowired  
	private TestEntityManager tem;  

	@Autowired  
	private UsuarioRepository usuarioRepository;  

	private static final int NUM_ROWS = 25;  
	private static final String BASE_CPF = "->Test CPF";  
	private static final String BASE_DATA = "0/02/2000";  
	private static final String BASE_EMAIL = "->Test Email";  
	private static final String BASE_NOME = "->Test Nome";  

	@BeforeEach  
	public void inserirUsuariosTeste() throws ParseException {  
		Usuario usuario;  

		for (int i=0; i<NUM_ROWS; i++) {  
			usuario = new Usuario(
				i+BASE_CPF,  
				new SimpleDateFormat("dd/MM/yyyy").parse(i+BASE_DATA),  
				i+BASE_EMAIL,  
				i+BASE_NOME
			);  
			tem.persist(usuario);  
		}  
	}  

	@Test  
	void deve_lancar_excecao_no_save() throws ParseException {  
		Usuario usuario = new Usuario(  
							"Novo CPF",  
							new SimpleDateFormat("dd/MM/yyyy").parse("19/02/2021"),  
							"Novo Email",  
							"Novo Nome"  
		);  

		usuario.setEmail(usuarioRepository.findByEmail(1 + BASE_EMAIL).getEmail());  

		assertThrows(DataIntegrityViolationException.class, () -> {  
			usuarioRepository.saveAndFlush(usuario);  
		});  
	}  
}
```
#### UsuarioServiceImplTest.java
```java
@RunWith(MockitoJUnitRunner.class)  
public class UsuarioServiceImplTest {  

	@Mock  
	UsuarioRepository usuarioRepository;  

	@Mock  
	UsuarioMapper usuarioMapper;  

	@InjectMocks  
	UsuarioServiceImpl usuarioService;  

	@Test  
	public void quando_salvar_deve_retornar_usuario() throws ParseException, UsuarioJaExisteException {  
		String expectedDataNascimento = "19/02/2021";  
		UsuarioRequest request = new UsuarioRequest(  
			"31856332020",  
			new SimpleDateFormat("dd/MM/yyyy").parse(expectedDataNascimento),  
			"test@email",  
			"Test"  
		);  

		when(usuarioMapper.usuarioRequestToUsuario(any(UsuarioRequest.class))).thenReturn(new Usuario());  
		when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());  

		UsuarioRequest usuarioCriado = usuarioService.save(request);  

		assertEquals(usuarioCriado.getCpf(), request.getCpf());  
		assertEquals(usuarioCriado.getDataNascimento(), request.getDataNascimento());  
		assertEquals(usuarioCriado.getEmail(), request.getEmail());  
		assertEquals(usuarioCriado.getNome(), request.getNome());  
	}  
}
```
#### VacinacaoControllerTest.java
```java
@RunWith(SpringRunner.class)  
@WebMvcTest(VacinacaoController.class)  
@AutoConfigureMockMvc  
public class VacinacaoControllerTest {  
	
	@MockBean  
	VacinacaoService vacinacaoService;  

	@Autowired  
	private MockMvc mockMvc;  

	ObjectMapper mapper = new ObjectMapper();  

	@Test  
	public void deve_retornar_status_http_criado() throws Exception {  
		String expectedDataVacinacao = "19/02/2021";  
		VacinacaoRequest request = new VacinacaoRequest();  
		request.setEmailUsuario("test@email");  
		request.setDataVacinacao(new SimpleDateFormat("dd/MM/yyyy").parse(expectedDataVacinacao));  
		request.setNome("Test");  

		when(vacinacaoService.save(any(VacinacaoRequest.class))).thenReturn(request);  

		mockMvc.perform(post("/orangetalents/v1/vacinacao")  
				.content(mapper.writeValueAsString(request))  
				.contentType(MediaType.APPLICATION_JSON))  
				.andExpect(status().isCreated())  
				.andExpect(jsonPath("$.dataVacinacao").value(expectedDataVacinacao))  
				.andExpect(jsonPath("$.emailUsuario").value(request.getEmailUsuario()))  
				.andExpect(jsonPath("$.nome").value(request.getNome()));  
	}  
}
```
#### VacinacaoServiceImpl.java
```java
@RunWith(MockitoJUnitRunner.class)  
public class VacinacaoServiceImplTest {  

	@Mock  
	UsuarioRepository usuarioRepository;  

	@Mock  
	VacinacaoRepository vacinacaoRepository;  

	@InjectMocks  
	VacinacaoServiceImpl vacinacaoService;  

	@Test  
	public void quando_salvar_deve_retornar_usuario() throws ParseException, EmailUsuarioNaoEncontrado {  
		String expectedDataVacinacao = "19/02/2021";  

		VacinacaoRequest request = new VacinacaoRequest(  
			new SimpleDateFormat("dd/MM/yyyy").parse(expectedDataVacinacao),  
			"test@email",  
			"Test"  
		);  

		when(usuarioRepository.findByEmail(any(String.class))).thenReturn(new Usuario());  
		when(vacinacaoRepository.save(any(Vacinacao.class))).thenReturn(new Vacinacao());  

		VacinacaoRequest vacinacaoCriada = vacinacaoService.save(request);  

		assertEquals(vacinacaoCriada.getDataVacinacao(), request.getDataVacinacao());  
		assertEquals(vacinacaoCriada.getEmailUsuario(), request.getEmailUsuario());  
		assertEquals(vacinacaoCriada.getNome(), request.getNome());  
	}  
}
```
#### VacinacaoTest.java
```java
@RunWith(MockitoJUnitRunner.class)  
public class UsuarioTest {  

	private UsuarioMapperImpl usuarioMapper;  

	@Before  
	public void setup() {  
		usuarioMapper = new UsuarioMapperImpl();  
	}  

	@Test  
	public void cpf_deve_ser_setado_sem_pontos_e_tracos() throws ParseException {  
		String cpfEsperado = "31856332020";  
		String dataNascimentoEsperada = "19/02/2021";  
		String emailEsperado = "test@email";  
		String nomeEsperado = "Test";  

		Usuario usuario = new Usuario(  
			"318.563.320-20",  
			new SimpleDateFormat("dd/MM/yyyy").parse("19/02/2021"),  
			"test@email",  
			"Test"  
		);  

		assertEquals(cpfEsperado, usuario.getCpf());  
		assertEquals( new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoEsperada), usuario.getDataNascimento());  
		assertEquals(emailEsperado, usuario.getEmail());  
		assertEquals(nomeEsperado, usuario.getNome());  
	}  

	@Test  
	public void cpf_deve_ser_setado_sem_pontos_e_tracos_usando_mapper() throws ParseException {  
		String cpfEsperado = "31856332020";  
		String dataNascimentoEsperada = "19/02/2021";  
		String emailEsperado = "test@email";  
		String nomeEsperado = "Test";  

		UsuarioRequest usuarioRequest = new UsuarioRequest(  
			"318.563.320-20",  
			new SimpleDateFormat("dd/MM/yyyy").parse("19/02/2021"),  
			"test@email",  
			"Test"  
		);  

		Usuario usuario = usuarioMapper.usuarioRequestToUsuario(usuarioRequest);  

		assertEquals(cpfEsperado, usuario.getCpf());  
		assertEquals( new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoEsperada), usuario.getDataNascimento());  
		assertEquals(emailEsperado, usuario.getEmail());  
		assertEquals(nomeEsperado, usuario.getNome());  
	}  
}
```
### SwaggerUI
Um primeiro passo para a documentação da API é o Swagger, ou OpenAPI, ele tem a própria interface que facilita a visualização das rotas da API, podemos acessar através do link:

> http://localhost:8081/swagger-ui.html

Ele lê as anotações do Spring nos Controllers e já documenta todas as rotas para nós.

### cURL
Com o cURL, ou pelo postman por exemplo, podemos fazer requisições da API:
```c
curl --location --request POST 'http://localhost:8081/orangetalents/v1/usuario' \
--header 'Content-Type: application/json' \
--header 'Cache-Control: no-cache' \
--data-raw '{
    "cpf": "708.707.970-00",
    "dataNascimento": "01/01/2000",
    "email": "email@email",
    "nome": "TESTE"
}'
```
```c
curl --location --request POST 'http://localhost:8081/orangetalents/v1/vacinacao' \
--header 'Content-Type: application/json' \
--data-raw '{
    "dataVacinacao": "18/02/2022",
    "nome": "CORONAVAC",
    "emailUsuario": "email@email"
}'
```
## Próximos passos e conclusão
Concluímos! A aplicação roda localmente mas não está pronta para um ambiente produtivo, quais seriam os próximos passos a serem considerados antes de subir para produção?

- Banco de Dados: O H2 definitivamente não é um banco preparado para produção, usado em memória cada vez que a aplicação subisse todos os dados seriam apagados. Existem *n* alternativas de bancos de dados relacionais que poderiamos usar, essa decisão depende mais do cliente e das possibilidades do servidor. 

- Servidor: Aqui temos infinitas possibilidades, se fosse decidido subir em um serviço de cloud pública, como a AWS por exemplo, poderiamos usufruir de vários recursos dela: ECS, EC2, Load Balancer, API Gateway, CodePipeline, Secrets Manager, RDS, são recursos infinitos... Para definirmos isso precisamos entender qual é o escopo e os requisitos da aplicação final, o que fizemos aqui é o MVP do MVP.

- Segurança: A aplicação lida com dados sensíveis dos usuários, se essa aplicação fosse para produção agora, qualquer um conseguiria pegar esses dados. A primeiro passo a desenvolvido é a criptografia dos dados no banco, assim ninguém que tenha acesso a ele consiga ler esses dados sensíveis. Mas isso é o básico do básico, precisamos atender os princípios da segurança da informação: confidencialidade, integridade, disponibilidade, autenticidade e legalidade. Para isso o Spring oferece vários módulos de segurança com o Spring Security a serem explorados e usados conforme a necessidade. Poderiamos implementar uma autorização para fazer chamadas na API, como um token JWT que conteria informações do cliente que está fazendo requisições assim se o serviço fosse permitido, ele estaria autorizado a fazer a requisição.

- Domínio: Precisamos pensar no domínio das informações da aplicação, como dos nomes das vacinas, poderia existir uma tabela no banco com os nomes das vacinas que podem ser cadastradas na vacinação dos usuários, essa tabela poderia ser consultada por outros serviços, como um front-end para aplicação, e na tela de cadastro das vacinações existe um select com os nomes das vacinas cadastradas, mantendo a integridade da informação no banco.

- Outras funcionalidades: Por enquanto a aplicação só realiza cadastro das informações, mas poderíamos implantar consultas, atualizações, etc. O sistema pode evoluir muito mais, se quiser exercitar é só clonar o repositório na sua máquina e praticar!

*Projeto na íntegra pode ser encontrado neste [repositório](https://github.com/AndreLars/orangetalents) no github.*
