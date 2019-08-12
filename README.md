# Sistema de Informações e Gestão Acadêmica

## Índice

1. [Propósito](#1-propósito)
2. [Documentação](#2-documentação)
3. [Membros do Time](#3-membros-do-time)
4. [Pré-requisitos](#4-pré-requisitos)
5. [Instalação](#5-instalação)
6. [Definições, Acrônimos e Abreviações](#6-definições-acrônimos-e-abreviações)
7. [Ambientes](#7-ambientes)
8. [Nossos Padrões de Código](#8-nossos-padrões-de-código)
9. [Execução de Testes](#9-execução-de-testes)
10. [Método de Versionamento](#10-método-de-versionamento)
11. [Contribuições ao Repositório](#11-contribuições-ao-repositório)
12. [Critérios de Aceitação de Contribuições](#12-critérios-de-aceitação-de-contribuições)
	

## 1. Propósito
----

<Descrição do propósito do sistema>

### 2. Documentação
----

<Links para documentação técnica do sistema>

### 3. Membros do Time
----

Bernadette Loscio 
Helton Santos 
Lairson Alencar
Wilker Santos


### 4. Pré-requisitos
----

<Ferramentas necessárias para se trabalhar no projeto>

| Tipo / Função | Ferramenta | Versão |
| ------------- | ---------- | ------ |
| Análise estática de código-fonte | Sonarqube | 5.6.6 |
| Biblioteca de build | ? | ? |
| Gerenciamento do controle de versões | GitLab | 9.5.1 |
| Framework de persistência | Hibernate | ? |
| Gerenciador de dependências | ?  | ? |
| IDE | Eclipse | ? |
| Integração contínua | Jenkins | 2.72 |
| Kit de desenvolvimento | Java | ? |
| Servidor de aplicação | ? | ? |
| Sistema operacional | ? | ? |

### 5. Instalação
----

<Como instalar e configurar a ferramenta>


### 6. Definições, Acrônimos e Abreviações
----

-

### 7. Ambientes
----

<Hosts e seus respectivos endereços, em cada um dos ambientes (desenvolvimento, testes, homologação e produção)

### 8. Nossos Padrões de Código
----

<Se houver, descrição do padrão de codificação utilizado>

### 9. Execução de Testes
----

<Quais serão as abordagens para execução de testes>

### 10. Método de Versionamento
----

<Formato da numeração de versão da ferramenta)

### 11. Contribuições ao Repositório
----

1. Atualizar o código local.
```sh
[usuario@localhost siga]$ git remote update -p
```
```sh
[usuario@localhost siga]$ git pull
```

2. Criar nova branch referente a um chamado.
```sh
[usuario@localhost siga]$ git checkout -b <branch> origin/master
**Ex.: git checkout -b ENH#0001 origin/master**
```

    Padrão: **[TIPO]#[NumeroIssue]**

    Ex.: BUG#3

    **Tipos disponíveis**:
    
    ###### BUG - Bug (Correção de bugs)
    ###### DOC - Documentation (Somente documentação é alterada)
    ###### ENH - Enhancement (Melhoria. Alteração envolve refactoring ou alteração que não está relacionada a um bug no sistema)
    ###### FEA - Feature (nova função ou novo requisito)
    ###### HOT - Hotfix (chamados imediatos)
    ###### QUA - Quality (Alterações realizadas em arquivos de configuração ou outro tipo de arquivo que somente a equipe de configuração possa alterar)

3. Criar **merge request** para a master do projeto.

    Padrão:

    **Título:** deve referenciar a mudança a ser realizada no código.

    **Descrição:** deve detalhar as mudanças.

    **ATENÇÃO**: O **merge request** deve possuir o identificador **[WIP]** no título do merge request.

4. Codificar.

5. Adicionar arquivos modificados.
```sh
[usuario@localhost IAGO]$ **git add arquivo**
**Ex.: git add CONTRIBUTING.md**
```

6. Commitar arquivos e escrever mensagem significativa.
```sh
[usuario@localhost IAGO]$ **git commit**
**Ex.: git commit**
```

7. Enviar branch para repositório remoto. 
```sh
[usuario@localhost siga]$ **git push origin branch**)
**Ex.: git push origin DOC#3**
```

8. Testar.

9. Retirar o prefixo **[WIP]** do **merge request**.

### 12. Critérios de Aceitação de Contribuições
----

1. Arquivos do tipo:

    .classpath

    .project

2. Branch deve possuir o padrão estabelecido no **item 2** da seção **Contribuições ao Repositório**.

---
#### Dúvidas, sugestões ou reclamações? Favor entrar em contato com a CQS (Coordenação de Qualidade de Software) do Núcleo de Tecnologia da Informação.

#### Copyright © UFPE - 2017
