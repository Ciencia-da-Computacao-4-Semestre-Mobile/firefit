package com.example.myapplication

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

// Simulação simples do serviço da Home
class HomeService {

    fun inscreverEmAula(usuarioLogado: Boolean, aulaDisponivel: Boolean): Boolean {
        return usuarioLogado && aulaDisponivel
    }

    // 🟢 Função necessária para o Teste 5 funcionar
    fun verificarMetaSono(metaDiaria: Int, horasDormidas: Int): Boolean {
        return horasDormidas >= metaDiaria
    }
}


class HomeServiceTest {

    @Test
    fun `Teste 4 – Inscrição em aula`() {

        // GIVEN que o usuário está logado e existe aula disponível
        val usuarioLogado = true
        val aulaDisponivel = true
        val homeService = HomeService()

        // WHEN o usuário seleciona e confirma inscrição
        val resultado = homeService.inscreverEmAula(usuarioLogado, aulaDisponivel)

        // THEN o sistema deve registrar inscrição com sucesso
        assertTrue(resultado)
    }

    @Test
    fun `Teste 5 – Meta de Sono`() {

        // GIVEN que o usuário tem meta de sono configurada (ex: 8h)
        val metaDiariaSono = 8
        val horasDormidasNoDia = 8  // você pode testar com 6 ou 7 depois para ver falhar

        val service = HomeService()

        // WHEN o usuário registra as horas dormidas
        val resultado = service.verificarMetaSono(metaDiariaSono, horasDormidasNoDia)

        // THEN deve informar se a meta foi atingida
        assertTrue(resultado) // Aqui esperamos que tenha atingido a meta
    }

}
