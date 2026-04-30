workspace "Book Management API" "API para gerenciamento de biblioteca" {

    model {
        user = person "Usuário" "Usuário do sistema"

        api = softwareSystem "ms-book-api" "Microsserviço de reserva de salas" {
            tags "Java", "Quarkus", "REST API"

            webapp = container "Web Application" "Aplicação REST" "Quarkus" {
                tags "Container"

                bookResource = component "BookResource" "Gerencia livros" "Java"
                authorResource = component "AuthorResource" "Gerencia autores" "Java"
                categoryResource = component "CategoryResource" "Gerencia categorias" "Java"
                publisherResource = component "PublisherResource" "Gerencia editoras" "Java"
                userResource = component "UserResource" "Gerencia usuários" "Java"
                loanResource = component "LoanResource" "Gerencia empréstimos" "Java"
                reviewResource = component "ReviewResource" "Gerencia avaliações" "Java"
                zeebeDeployer = component "ZeebeDeployer" "Deploy do BPMN" "Java"
            }
        }

        database = softwareSystem "MySQL Database" "Persistência de dados" {
            tags "Database"
        }

        zeebe = softwareSystem "Camunda Zeebe" "Workflow Engine" {
            tags "Workflow"
        }

        workerValidate = softwareSystem "LoanValidationWorker" "Valida empréstimo" {
            tags "Worker"
        }

        workerCreate = softwareSystem "CreateLoanWorker" "Cria empréstimo" {
            tags "Worker"
        }

        workerNotify = softwareSystem "NotifyUserWorker" "Notifica usuário" {
            tags "Worker"
        }

        user -> webapp "Faz requisições HTTP"
        webapp -> database "Leitura/Escrita"
        webapp -> zeebe "Inicia processo"
        zeebe -> workerValidate "Executa tarefa"
        zeebe -> workerCreate "Executa tarefa"
        zeebe -> workerNotify "Executa tarefa"
        workerCreate -> database "Persiste empréstimo"
    }

    views {
        systemContext api "SystemContext" {
            title "System Context Diagram"
            include *
            autoLayout
        }

        container api "Containers" {
            title "Container Diagram"
            include *
            autoLayout
        }

        component webapp "Components" {
            title "Component Diagram"
            include *
            autoLayout
        }

        dynamic api "DynamicView" {
            title "Dynamic Diagram - Loan Process"
            description "Fluxo de solicitação de empréstimo"

            user -> webapp "POST /loans"
            webapp -> zeebe "startLoanProcess"
            zeebe -> workerValidate "validate-loan"
            workerValidate -> zeebe "loanValid = true/false"
            zeebe -> workerCreate "create-loan"
            workerCreate -> database "save loan"
            zeebe -> workerNotify "notify-user"
        }
    }
}