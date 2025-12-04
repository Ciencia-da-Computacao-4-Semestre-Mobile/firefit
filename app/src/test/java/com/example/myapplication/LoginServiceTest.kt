package com.example.myapplication

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LoginServiceTest {

    @Test
    fun `Teste 1 – Login válido`() {

        // GIVEN que o usuário possui cadastro válido
        val emailCorreto = "user@email.com"
        val senhaCorreta = "123"

        // serviço de login bem simples
        val loginOk = emailCorreto == "user@email.com" && senhaCorreta == "123"

        // WHEN usuário informa email e senha corretos
        val resultado = loginOk

        // THEN deve autenticar com sucesso
        assertTrue(resultado)
    }


    @Test
    fun `Teste 2 – Login inválido`() {

        // GIVEN que o usuário está na tela de login
        val emailInformado = "user@email.com"
        val senhaErrada = "999"      // senha incorreta

        // serviço de login simulando a verificação
        val loginFalhou = emailInformado == "user@email.com" && senhaErrada == "123"

        // WHEN o usuário informa senha incorreta
        val resultado = loginFalhou

        // THEN o sistema deve negar o acesso
        assertFalse(resultado)
    }


    @Test
    fun `Teste 3 – Campos obrigatórios`() {

        // GIVEN que o usuário tenta fazer login
        val emailVazio = ""
        val senhaVazia = ""

        // Simula validação de campos obrigatórios
        val camposValidos = emailVazio.isNotBlank() && senhaVazia.isNotBlank()

        // WHEN ele deixa campos obrigatórios vazios
        val resultado = camposValidos

        // THEN deve exibir mensagem de erro (ou seja, resultado deve ser falso)
        assertFalse(resultado) // Significa falha no login -> esperado
    }

}
