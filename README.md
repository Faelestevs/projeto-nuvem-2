# Projeto Integrador – Cloud Developing 2025/2

> CRUD simples + API Gateway + Lambda /report + RDS + CI/CD

**Grupo**:
<!-- no máximo 5 alunos -->

1. RA - nome - responsabilidade
2. RA - nome - responsabilidade
3. 10420414 - Raffael Esteves - responsabilidade


## 1. Visão geral
<!-- Descreva rapidamente o domínio escolhido, por que foi selecionado e o que o CRUD faz. -->

## 2. Arquitetura

![Diagrama](docs/arquitetura.png)

| Camada | Serviço | Descrição |
|--------|---------|-----------|
| Backend | ECS Fargate (ou EC2 + Docker) | API REST Node/Spring/… |
| Banco   | Amazon RDS              | PostgreSQL / MySQL em subnet privada |
| Gateway | Amazon API Gateway      | Rotas CRUD → ECS · `/report` → Lambda |
| Função  | AWS Lambda              | Consome a API, gera estatísticas JSON |
| CI/CD   | CodePipeline + GitHub   | push → build → ECR → deploy |

## 3. Como rodar localmente

```bash
cp .env.example .env         # configure variáveis
docker compose up --build
# API em http://localhost:3000
