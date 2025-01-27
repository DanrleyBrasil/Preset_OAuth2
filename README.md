# Projeto - Configuração Inicial

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

## Observações
- Certifique-se de que as configurações estejam completas antes de iniciar o projeto.
- Este projeto usa chaves específicas para operações de segurança. Nunca compartilhe as chaves privadas.

## Dúvidas
Para mais informações ou suporte, entre em contato com o time de desenvolvimento.
