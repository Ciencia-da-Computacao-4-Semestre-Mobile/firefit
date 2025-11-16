# 🔥 FireFit – Aplicativo de Academia em Kotlin + Firebase

O **FireFit** é um aplicativo mobile desenvolvido em **Kotlin** com backend em **Firebase**, criado para oferecer aos usuários uma experiência completa dentro e fora da academia.  
O app inclui controle de treinos, eventos, metas, estatísticas corporais (incluindo IMC) e um sistema moderno de login.

---

## 🚀 Funcionalidades

### 🔐 Autenticação
- Login rápido e seguro via Firebase Authentication  
- Função **Esqueci minha senha**  
- Tela de **Registro** com dados básicos  
- Validação de campos  

---

## 🏠 Home (Tela Inicial)
- 📅 **Eventos da academia**  
- 🔢 **Contador de idas à academia**  
- 🎯 **Card com metas diárias e semanais**  
- Interface moderna baseada no design do Figma

---

## 🗓 Eventos
- Página exclusiva com **eventos disponíveis na academia**  
- Opção de **salvar eventos preferidos**  
- Informações detalhadas: horário, categoria e descrição  

---

## 🧍 Área do Usuário
- Edição dos dados pessoais  
- Cálculo instantâneo de **IMC**  
- Lista com:
  - ✔ Treinos salvos  
  - ✔ Eventos salvos  

---

## 📊 Estatísticas
- Indicação visual:
  - ⚠ Obeso  
  - 👍 Peso ideal  
- Acompanhamento de:
  - Metas diárias  
  - Metas semanais  
- Gráficos e indicadores de progresso (Firebase Database/Firestore)

---

## 🏋️‍♂️ Treinos
Diversas categorias:
- Alongamentos  
- Cardio  
- Perda de peso  
- Definição muscular  

Cada treino contém:
- ⏱ Tempo recomendado  
- 🔁 Número de repetições  
- 📝 Instruções detalhadas  

---

## 🛠 Tecnologias Utilizadas

### **Frontend / App**
- Kotlin  
- Android Studio  
- ViewBinding / Jetpack Components  
- ConstraintLayout  

### **Backend**
- Firebase Authentication  
- Firebase Firestore ou Realtime Database  
- Firebase Storage (para imagens, se necessário)

---

## 📂 Estrutura do Projeto
app/
├─ java/com/firefit/
│ ├─ auth/
│ ├─ home/
│ ├─ events/
│ ├─ user/
│ ├─ stats/
│ └─ training/
└─ res/
├─ layout/
├─ drawable/
├─ values/
└─ mipmap/

---

## ▶ Como Executar
1. Clone o repositório:
   ```bash
   git clone https://github.com/Ciencia-da-Computacao-4-Semestre-Mobile/firefit
Abra no Android Studio

Configure o Firebase no arquivo google-services.json

Rode o app em um emulador ou dispositivo físico

🔮 Próximas Funcionalidades (Roadmap)
Notificações push para eventos

Sistema de recompensas por metas batidas

Ranking entre amigos

Dark/Light mode

Treinos personalizados por IA

🤝 Contribuição
🚧 Projeto aberto a contribuições!
Passos:

Fork

Nova branch

Commit

Pull Request

📄 Licença
Este projeto está sob licença MIT.
