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

    fun calcularProgressoAgua(metaMl: Int, ingeridoMl: Int): Int {
        return (ingeridoMl * 100) / metaMl  // retorna porcentagem
    }

    // Retorna uma lista de eventos (pode ser aulas, treino, yoga, etc)
    fun listarEventosDisponiveis(): List<String> {
        return listOf("Musculação", "Crossfit", "Yoga", "Funcional")
    }

    // Conta quantas vezes o usuário abriu o app
    fun registrarUsoApp(contadorAtual: Int): Int {
        return contadorAtual + 1
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

    @Test
    fun `Teste 6 – Meta de Água`() {

        // GIVEN meta diária configurada
        val metaDiariaAgua = 2000 // ml
        val ingeridoHoje = 1500  // ml já consumidos

        val service = HomeService()

        // WHEN usuário registra ingestão de água
        val progresso = service.calcularProgressoAgua(metaDiariaAgua, ingeridoHoje)

        // THEN deve retornar porcentagem alcançada
        assertEquals(75, progresso) // 1500ml de 2000ml = 75%
    }

    @Test
    fun `Teste 7 – Mostrar eventos disponíveis`() {

        // GIVEN que existem eventos cadastrados no sistema
        val service = HomeService()

        // WHEN o usuário acessa a tela de eventos
        val eventos = service.listarEventosDisponiveis()

        // THEN deve exibir a lista de eventos disponíveis
        assertTrue(eventos.isNotEmpty())
        assertEquals(4, eventos.size) // Deve ter exatamente 4 eventos
        assertTrue(eventos.contains("Yoga"))
    }

    @Test
    fun `Teste 8 – Contagem de uso do app`() {

        // GIVEN que o usuário já abriu o app 5 vezes
        val usoInicial = 5
        val service = HomeService()

        // WHEN ele abre novamente
        val novoTotal = service.registrarUsoApp(usoInicial)

        // THEN deve somar +1 no contador de uso
        assertEquals(6, novoTotal) // 5 → 6
    }
}
