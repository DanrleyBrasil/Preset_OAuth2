# Projeto - Configuração Inicial

## Descrição do Projeto

Este projeto é um preset que utiliza OAuth02 Resource para realizar a autenticação e geração de tokens jwt! 
Foi criado com o objetivo de agilizar o start de API's usando Spring Boot! 

## Características Gerais: 
   - Autenticação JWT
   - OAuth2
   - Swagger embarcado e configurado
   - Documentado com Javadoc

## Requisitos
1. **Instale o JDK 21**  
   Este projeto requer o **Java 21**. Certifique-se de ter a versão correta instalada em seu ambiente.

2. **Configure as dependências do projeto**  
   - Abra sua IDE preferida (como IntelliJ ou Eclipse).  
   - Permita que o sistema faça o download automático das dependências definidas no arquivo `pom.xml`.

## Configuração do OpenSSL
3. **Instale e configure o OpenSSL**  
   - Baixe o OpenSSL pelo link oficial: [OpenSSL Binaries](https://wiki.openssl.org/index.php/Binaries).  
   - Adicione o OpenSSL à variável de ambiente `PATH`.  
   - Verifique a instalação executando o comando:  
     ```bash
     openssl version
     ```

4. **Gerar chaves privadas e públicas**  
   - Antes de gerar novas chaves, exclua os arquivos padrão existentes no projeto:  
     - **app.key**  
     - **app.pub**  
     Ambos estão localizados em:  
     ```
     {seu_projeto}/src/main/resources
     ```
   - Siga os passos abaixo para gerar novas chaves:  
     1. Navegue até o diretório de recursos:  
        ```bash
        cd {seu_projeto}/src/main/resources
        ```
     2. Gere a chave privada:  
        ```bash
        openssl genrsa > app.key
        ```
     3. Gere a chave pública com base na chave privada:  
        ```bash
        openssl rsa -in app.key -pubout -out app.pub
        ```
     4. Verifique se as chaves foram geradas corretamente no diretório mencionado:  
        - **app.key** deve começar com:  
          ```
          -----BEGIN PRIVATE KEY-----
          ```
        - **app.pub** deve começar com:  
          ```
          -----BEGIN PUBLIC KEY-----
          ```

## Dicas Gerais          
1. **Configurando Usuário Admin Padrão**  
    - Antes de executar o projeto se atente a classe `AdminUserConfig.java` pois nela são criados os usuários padrões! 
    - Altere o usuário ADMIN padrão de acordo com a necessidade do seu projeto!

2. **Configuração do Banco de Dados**
    - O exemplo deste preset utiliza como base de dados `MySQL`. Ajuste o arquivo `pom.xml` de acordo com o seu projeto! 
    - Não esqueça também de configurar o arquivo `application.properties` para se comunicar com a base de dados adequada!
    - O trecho `spring.jpa.hibernate.ddl-auto=update` no arquivo `application.properties`deve ser removido ou alterado antes de pensar em colocar o projeto em produção
  
3. **Roles**
     - As Roles são configuradas na entidade `Role` a qual, por default no projeto possui `ADMIN` e `USER`.
     - Para este projeto estou utilizado o seguinte formato, logo abaixo do mapeamento das rotas: 
         `@PreAuthorize("hasAuthority('SCOPE_ADMIN')")` <- Autorizando roles ADMIN 
         `@PreAuthorize("hasAuthority('SCOPE_USER')")` <- Autorizando roles USER
      Exemplos: 
         ```bash
             @GetMapping("/users")
             @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
             public ResponseEntity<List<User>> listUsers() {
                 var users = userRepository.findAll();
                 return ResponseEntity.ok(users);
             }
             
             @GetMapping("/users2")
             @PreAuthorize("hasAuthority('SCOPE_USER')")
             public ResponseEntity<List<User>> listUsersTwo() {
                 var users = userRepository.findAll();
                 return ResponseEntity.ok(users);
             }
         ```
      - Ao adicionar uma nova ROLE você poderá forçar ela na rota usando o prefixo `SCOPE_`, por exemplo:
         - Nova role `GESTOR`
         - `@PreAuthorize("hasAuthority('SCOPE_GESTOR')")`

## Observações
- Certifique-se de que as configurações estejam completas antes de iniciar o projeto.
- Este projeto usa chaves específicas para operações de segurança. Nunca compartilhe as chaves privadas.

## Dúvidas
Para mais informações ou suporte, entre em contato com o time de desenvolvimento.

## Acompanhe as novidades através das Releases
