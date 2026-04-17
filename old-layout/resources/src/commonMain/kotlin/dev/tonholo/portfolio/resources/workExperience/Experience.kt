package dev.tonholo.portfolio.resources.workExperience

import cafe.adriel.lyricist.LanguageTag
import dev.tonholo.portfolio.core.collections.ImmutableList
import dev.tonholo.portfolio.core.collections.immutableListOf
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.Description
import kotlinx.datetime.LocalDate

data class Experience(
    val position: String,
    val company: String,
    val description: Description,
    val starting: LocalDate,
    val ending: LocalDate? = null,
    val technologiesUsed: List<String> = emptyList(),
) {
    companion object {
        val Experiences: Map<LanguageTag, ImmutableList<Experience>> = mapOf(
            Locales.EN to immutableListOf(
                REDSpace,
                Pinterest,
                QuestradePrincipal,
                QuestradeSenior,
                Axxiom,
                Planear,
            ),
            Locales.PT_BR to immutableListOf(
                REDSpace.copy(
                    description = Description(
                        "Projetando e desenvolvendo aplicações mobile robustas, fácil manutenção e em conformidade com os padrões de aplicações e bibliotecas para a plataforma Android.",
                        "Projetando, arquitetando e mantendo código Kotlin/Java de alto desempenho, reutilizável e confiável.",
                        "Usando técnicas e softwares de profiling para identificar e corrigir problemas de gerenciamento de memória e gargalos de desempenho.",
                        "Entregando apps robustos, de alta qualidade dentro do cronograma.",
                    ),
                    position = "Senior Android Engineer",
                ),
                Pinterest.copy(
                    description = Description(
                        "Desenvolvendo soluções, optimizações e mantendo frameworks que ajudam o time de engenheiros de Software a entregar a melhor experiência possível para quem utiliza o Pinterest",
                        "Trabalhando com dynamic feature module, melhorando a velocidade de compilação do app.",
                        "Trabalhando em conjunto com o time de engenheiros de produto criando facilitadores para desenvolvimento de novos produtos.",
                        "Manutenção de unit e integration tests garantindo a qualidade de código",
                        "Iniciei o processo de migração de views que utilizavão XML para o Jetpack Compose",
                    ),
                    position = "Android Engineer",
                ),
                QuestradePrincipal.copy(
                    description = Description(
                        "Liderança técnica, guiando o novo time que trabalha nos novos apps para Questrade, usando React Native. Participação direta no desenho e riação =  soluções baseadas em eventos com alta disponibilidade para suportar os novos aplicativos e a enorme demanda dos clientes",
                        "Implementação de React Native module para suportar autenticação utilizando biometria e OIDC Auth code + PCKE flow.",
                        "Projetado e desenvolvido microsserviços baseados em eventos usando Apache Kafkae Google Pub-Sub.",
                        "Projetado e um serviço mock com validação de payload, baseado no contrato de API Open API, a fim de permitir o desenvolvimento móvel durante a criação dos endpoints.",
                    ),
                    position = "Principal Software Engineer",
                ),
                QuestradeSenior.copy(
                    description = Description(
                        "Implementadas novas funcionalidades e melhorou o desempenho e a qualidade do app híbrido móvel Questrade. Implementação de um novo método de utenticação =tilizando autenticações biométricos (Fingerprint para Android, TouchID e FaceID para iOS). Desenvolvimento de todo um novo processo de utenticação = ra mobile, desktop e web, baseado em Identity Server",
                        "Aumento da satisfação do cliente no aplicativo móvel híbrido de 1,5 para 3,3 estrelas para um aplicativo com mais de 100.000 uso diário.",
                        "Criado um método de autenticação segura para o app usando código nativo (Kotlin para Android e Swift para iOS), com todos os dados criptografados usando RSA.",
                        "Execução de um novo projeto de autenticação desde o conceito até a conclusão, incluindo integração móvel, implementação de front e back-end, registro, métricas e entrega.",
                    ),
                    position = "Senior Software Engineer",
                ),
                Axxiom.copy(
                    description = Description(
                        "Projetei e implementei um novo aplicativo do zero para a CEMIG, empresa mineira de energia, usando Android nativo. Integração com Google Maps mostrando objetos que precisam de reparos ou instalação",
                        "Projetado um aplicativo usando o padrão de design de materiais.",
                        "Aplicação distribuída que se comunica com os outros usando AIDL.",
                        "Concebido e desenvolvido um centro de notificação que fornece mensagens entre a base e os aplicativos usando a melhor conexão disponível (Wi-Fi, rede móvel ou conexão de satélite).",
                    ),
                    position = "Analista Desenvolvedor Mobile",
                ),
                Planear.copy(
                    description = Description(
                        "Soluções para a área de gestão de saúde e para a área de marketing. Desenvolvido um aplicativo móvel para gerenciar agendamento médico, rescrições =  tratamento e recebimentos financeiros. Também foi desenvolvido um aplicativo para a área de marketing criando pesquisas dinâmicas no aplicativo mobile",
                        "Implementado ambas as soluções móveis e da Web para gerenciar clínicas de saúde e policlínicas públicas.",
                        "Criado um aplicativo dinâmico que recebe dados do serviço RESTful e constrói uma forma dinâmica com base nisso.",
                        "Criado uma rede social para a política de ajuda e seus partidários em sua campanha usando gamification.",
                    ),
                    position = "Analista Desenvolvedor .NET",
                ),
            )
        )
    }
}

private val REDSpace = Experience(
    company = "REDspace",
    description = Description(
        "Designing and developing robust, maintainable and standards-compliant mobile applications and libraries for the Android Platform.",
        "Designing, architecting, and maintaining high-performance, reusable, and reliable Kotlin/Java code.",
        "Using profiling techniques and software to identify and correct memory management problems and performance bottlenecks.",
        "Delivering high-quality, robust, feature-specific software on schedule.",
    ),
    position = "Senior Android Engineer",
    starting = LocalDate.parse("2022-03-28"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "Hilt",
        "Jetpack Compose",
        "git",
        "Java",
    )
)

private val Pinterest = Experience(
    company = "Pinterest",
    description = Description(
        "Working designing solutions, optimizing and maintaining frameworks that empower the engineering team to deliver the best possible experience for people who use Pinterest.",
        "Working with dynamic feature module, improving build speed.",
        "Working closely with product engineering team creating enablers to new products development.",
        "Maintaining unit and integration tests ensuring code quality.",
        "Started migration of XML views to Jetpack Compose.",
    ),
    position = "Senior Android Engineer",
    starting = LocalDate.parse("2021-07-23"),
    ending = LocalDate.parse("2022-03-18"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "Play Feature Delivery",
        "Dagger2",
        "Hilt",
        "Jetpack Compose",
        "Java",
        "Phabricator",
        "git",
        "Jira",
    )
)

private val QuestradePrincipal = Experience(
    company = "Questrade Financial Group",
    description = Description(
        "Technical leadership, coaching the new team working on the new apps for Questrade, using React Native. Designed solutions event-based with high availability to support the new apps and the huge customer demand.",
        "Implemented React Native module to support biometric authentication and OIDC Auth code + PCKE flow.",
        "Designed and developed event-based microservices using Apache Kafka and Google Pub-Sub.",
        "Designed and developed a mock service with payload private validation, based on the open API contract, in order to enable mobile development during microservice creation.",
    ),
    position = "Principal Software Engineer",
    starting = LocalDate.parse("2019-10-01"),
    ending = LocalDate.parse("2021-07-23"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "iOS",
        "Swift",
        "Bitrise",
        "C#",
        ".NET Core",
        "GCP",
        "TypeScript",
        "NodeJS",
        "git",
        "Jenkins",
        "Datadog",
        "Jira",
        "GitLab",
    )
)

private val QuestradeSenior = Experience(
    company = "Questrade Financial Group",
    description = Description(
        "Implemented new features and improved the performance and quality of the Questrade mobile hybrid app. Implemented a new authentication method using biometric authentications (Fingerprint for Android, TouchID and FaceID for iOS). Development of a whole new authentication process for mobile, desktop and web, based on Identity Server.",
        "Increased customer satisfaction in the hybrid mobile app from 1.5 to 3.3 stars for an application with 100.000+ daily usage.",
        "Created a secure authentication method for the hybrid app using native code (Kotlin for Android and Swift for iOS), with all data encrypted using the RSA method.",
        "Executed new authentication project from concept through to completion, including mobile integration, front and back-end implementation, logging, metrics and delivery.",
    ),
    position = "Senior Software Engineer",
    starting = LocalDate.parse("2017-08-28"),
    ending = LocalDate.parse("2019-10-01"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "Java",
        "iOS",
        "Swift",
        "Objective-C",
        "Cordova",
        "C#",
        ".NET Core",
        "git",
        "SVN",
        "Jenkins",
        "Jira",
        "gitLab",
    )
)

private val Axxiom = Experience(
    company = "Axxiom",
    description = Description(
        "Designed and implemented a new app from scratch for CEMIG, the energy company from Minas Gerais, using Android native. Integration with Google Maps showing objects that need repairs or installation.",
        "Designed an application using the Material Design pattern.",
        "Distributed application that communicates with others using AIDL.",
        "Designed and developed a notification center that provides messages between the base and the apps using the best available connection (Wi-Fi, Mobile Network or Satellite connection)."
    ),
    position = "Mobile Analyst Developer",
    starting = LocalDate.parse("2015-06-08"),
    ending = LocalDate.parse("2017-08-18"),
    technologiesUsed = listOf(
        "Android",
        "Java",
        "SQLite",
        "Symmetric DS",
        "Google Maps API",
        "C#",
        "git",
        "Jira",
        "Stash",
        "Bamboo",
        "Informix",
    )
)

private val Planear = Experience(
    company = "Planear Sistemas",
    description = Description(
        "Designed solutions to Health care management and marketing area. Developed a mobile application to manage medical scheduling, treatment prescriptions, and financial receipts. Developed an application to the marketing area creating dynamic surveys in the mobile app.",
        "Implemented both mobile and web solutions to manage health clinics and public polyclinics managing themselves.",
        "Created a dynamic application that receives data from RESTful service and builds a dynamic form based on that.",
        "Created a social network to help politics and his supporters in his campaign using gamification.",
    ),
    position = "Analyst Developer .NET",
    starting = LocalDate.parse("2013-02-01"),
    ending = LocalDate.parse("2015-06-08"),
    technologiesUsed = listOf(
        "C#",
        "Android",
        "Java",
        "RESTful",
        "SVN",
        "SQL Server",
        "Unity 3D",
        "JavaScript",
        "ASP.NET WebForms",
        "ASP.NET MVC 5",
    )
)
