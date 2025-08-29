# Desafio Pix

Este projeto implementa um sistema de gerenciamento de chaves Pix via linha de comando.

## Como executar

1. Compile o projeto Java normalmente (exemplo: `javac ...` ou via sua IDE).
2. Execute o programa principal, passando os parâmetros conforme abaixo.

## Parâmetros de linha de comando

Os comandos devem ser passados via argumentos, utilizando o seguinte mapa de parâmetros:

| Parâmetro | Descrição                        | Exemplo           |
|-----------|----------------------------------|-------------------|
| -i        | Instituição bancária             | 001               |
| -a        | Agência bancária                 | 1234              |
| -c        | Número da conta                  | 56789-0           |
| -tc       | Tipo de conta bancária           | CORRENTE          |
| -t        | Tipo de chave Pix                | CPF, EMAIL, etc   |
| -v        | Valor da chave Pix               | 12345678900       |
| -m        | Validade em meses (opcional)     | 6                 |
| -cmd      | Comando a ser executado          | cadastrar         |

### Exemplo de uso

```sh
java -jar desafio-pix.jar -cmd cadastrar -i 001 -a 1234 -c 56789-0 -tc CORRENTE -t CPF -v 12345678900
```

Para cadastrar uma chave com validade:

```sh
java -jar desafio-pix.jar -cmd cadastrar -i 001 -a 1234 -c 56789-0 -tc CORRENTE -t CPF -v 12345678900 -m 6
```

## Comandos disponíveis

- **cadastrar**: Cadastra uma nova chave Pix.
- Outros comandos podem ser implementados conforme o projeto evolui.

## Observações

- Todos os parâmetros são obrigatórios, exceto `-m` (validade em meses).
- O comando deve ser passado via `-cmd`.
- O sistema salva as chaves em arquivo local e permite consultar e gerenciar o status das chaves.

---

Projeto desenvolvido para fins didáticos.
