package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LyricistStrings
import dev.tonholo.portfolio.locale.Locales

data class NavBar(
    val home: String,
    val articles: String,
    val resume: String,
)

data class Screen(val home: HomePage)

sealed interface Page
data class HomePage(
    val info: InfoSection,
    val skills: SkillSection,
    val historySection: HistorySection,
) : Page {
    data class InfoSection(
        val jobTitle: String,
        val about: List<String>,
    )

    data class SkillSection(
        val languages: Languages,
        val programingLanguages: ProgrammingLanguages
    ) {
        data class Languages(
            val title: String,
            val en: String,
            val pt: String,
        )

        data class ProgrammingLanguages(
            val title: String,
            val ctaShowMore: String,
            val ctaShowLess: String,
        )
    }

    data class HistorySection(
        val work: WorkHistory,
        val educational: EducationalHistory,
    ) {
        interface History {
            val title: String
            val experiences: List<Experience>
        }

        data class WorkHistory(
            override val title: String,
            val technologiesUsed: String,
            override val experiences: List<Experience>,
        ) : History

        data class EducationalHistory(
            override val title: String,
            override val experiences: List<Experience>,
        ) : History

        data class Experience(
            val name: String,
            val title: String,
            val description: ExperienceParagraph,
            val starting: String? = null,
            val ending: String? = null,
            val technologiesUsed: List<String>? = null
        )
    }
}

value class ExperienceParagraph private constructor(
    val value: String,
) {
    companion object {
        operator fun invoke(highlight: String, vararg paragraphs: String): ExperienceParagraph {
            var parsed = "$highlight ${if (paragraphs.isNotEmpty()) "<0>" else ""}"
            paragraphs.forEachIndexed { index, paragraph ->
                parsed += "<${index + 1}>"
                parsed += paragraph
                parsed += "</${index + 1}>"
            }

            return ExperienceParagraph(
                value = parsed,
            )
        }
    }
}

data class Strings(
    val screens: Screen,
    val navBar: NavBar,
)

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = Strings(
    screens = Screen(
        home = HomePage(
            info = HomePage.InfoSection(
                jobTitle = "Software Engineer",
                about = listOf(
                    "Software engineer with 10 years of experience delivering tech solutions. An avid programmer and fast learner.",
                    "Always advocating for optimal user experience while ensuring technological feasibility. Ability to work with several languages, frameworks, and paradigms at the same time.",
                    "A team-oriented developer which understands helping my colleagues we'll grow together. I love simple and elegant solutions for every problem, but understanding that simplicity and elegance must not degrade performance.",
                ),
            ),
            skills = HomePage.SkillSection(
                languages = HomePage.SkillSection.Languages(
                    title = "Languages",
                    en = "English",
                    pt = "Portuguese",
                ),
                programingLanguages = HomePage.SkillSection.ProgrammingLanguages(
                    title = "Skills",
                    ctaShowMore = "Show more",
                    ctaShowLess = "Show less",
                ),
            ),
            historySection = HomePage.HistorySection(
                work = HomePage.HistorySection.WorkHistory(
                    title = "Experience",
                    technologiesUsed = "Technologies used:",
                    experiences = listOf(
                        HomePage.HistorySection.Experience(
                            name = "REDspace",
                            description = ExperienceParagraph(
                                highlight = "Designing and developing robust, maintainable and standards-compliant mobile applications and libraries for the Android Platform.",
                                "Designing, architecting, and maintaining high-performance, reusable, and reliable Kotlin/Java code.",
                                "Using profiling techniques and software to identify and correct memory management problems and performance bottlenecks.",
                                "Delivering high-quality, robust, feature-specific software on schedule.",
                            ),
                            title = "Android Engineer",
                            starting = "2022/03/28",
                            technologiesUsed = listOf(
                                "Android",
                                "Kotlin",
                                "Hilt",
                                "Jetpack Compose",
                                "git",
                                "Java",
                            )
                        ),
                        HomePage.HistorySection.Experience(
                            name = "BairesDev / Pinterest",
                            description = ExperienceParagraph(
                                highlight = "Working designing solutions, optimizing and maintaining frameworks that empower the engineering team to deliver the best possible experience for people who use Pinterest.",
                                "Working with dynamic feature module, improving build speed.",
                                "Working closely with product engineering team creating enablers to new products development.",
                                "Maintaining unit and integration tests ensuring code quality.",
                                "Started migration of XML views to Jetpack Compose.",
                            ),
                            title = "Android Engineer",
                            starting = "2021/07/23",
                            ending = "2022/03/18",
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
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Questrade Financial Group",
                            description = ExperienceParagraph(
                                highlight = "Technical leadership, coaching the new team working on the new apps for Questrade, using React Native. Designed solutions event-based with high availability to support the new apps and the huge customer demand.",
                                "Implemented React Native module to support biometric authentication and OIDC Auth code + PCKE flow.",
                                "Designed and developed event-based microservices using Apache Kafka and Google Pub-Sub.",
                                "Designed and developed a mock service with payload validation, based on the open API contract, in order to enable mobile development during microservice creation.",
                            ),
                            title = "Principal Software Engineer",
                            starting = "2019/10/01",
                            ending = "2021/07/23",
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
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Questrade Financial Group",
                            description = ExperienceParagraph(
                                highlight = "Implemented new features and improved the performance and quality of the Questrade mobile hybrid app. Implemented a new authentication method using biometric authentications (Fingerprint for Android, TouchID and FaceID for iOS). Development of a whole new authentication process for mobile, desktop and web, based on Identity Server.",
                                "Increased customer satisfaction in the hybrid mobile app from 1.5 to 3.3 stars for an application with 100.000+ daily usage.",
                                "Created a secure authentication method for the hybrid app using native code (Kotlin for Android and Swift for iOS), with all data encrypted using the RSA method.",
                                "Executed new authentication project from concept through to completion, including mobile integration, front and back-end implementation, logging, metrics and delivery.",
                            ),
                            title = "Senior Software Engineer",
                            starting = "2017/08/28",
                            ending = "2019/10/01",
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
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Axxiom",
                            description = ExperienceParagraph(
                                highlight = "Designed and implemented a new app from scratch for CEMIG, the energy company from Minas Gerais, using Android native. Integration with Google Maps showing objects that need repairs or installation.",
                                "Designed an application using the Material Design pattern.",
                                "Distributed application that communicates with others using AIDL.",
                                "Designed and developed a notification center that provides messages between the base and the apps using the best available connection (Wi-Fi, Mobile Network or Satellite connection)."
                            ),
                            title = "Mobile Analyst Developer",
                            starting = "2015/06/08",
                            ending = "2017/08/18",
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
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Planear Sistemas",
                            description = ExperienceParagraph(
                                highlight = "Designed solutions to Health care management and marketing area. Developed a mobile application to manage medical scheduling, treatment prescriptions, and financial receipts. Developed an application to the marketing area creating dynamic surveys in the mobile app.",
                                "Implemented both mobile and web solutions to manage health clinics and public polyclinics managing themselves.",
                                "Created a dynamic application that receives data from RESTful service and builds a dynamic form based on that.",
                                "Created a social network to help politics and his supporters in his campaign using gamification.",
                            ),
                            title = "Analyst Developer .NET",
                            starting = "2013/02/01",
                            ending = "2015/06/08",
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
                        ),
                    ),
                ),
                educational = HomePage.HistorySection.EducationalHistory(
                    title = "Education",
                    experiences = listOf(
                        HomePage.HistorySection.Experience(
                            name = "University center UNA",
                            title = "Post-graduate, Development of Application for Mobile Devices",
                            description = ExperienceParagraph(
                                highlight = "- Development of applications to iOS, Android, Windows Phone.\n- Development of hybrid applications focused on Cordova (ionic) and Xamarin.",
                            ),
                            starting = "2015/03/20",
                            ending = "2016/01/09",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "PUC-MG",
                            title = "Technologist, Digital games",
                            description = ExperienceParagraph(
                                highlight = "Development of Digital Games using Unity3D engine, DirectX and OpenGL.",
                            ),
                            starting = "2012/02/01",
                            ending = "2014/09/10",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "COTEMIG",
                            title = "Computer Technician",
                            description = ExperienceParagraph(
                                highlight = "",
                            ),
                            starting = "2010/01/01",
                            ending = "2012/01/01",
                        ),
                    ),
                ),
            ),
        ),
    ),
    navBar = NavBar(
        home = "Home",
        articles = "Articles",
        resume = "Resume",
    )
)

@LyricistStrings(languageTag = Locales.PT_BR)
val PtStrings = Strings(
    screens = Screen(
        home = HomePage(
            info = HomePage.InfoSection(
                jobTitle = "Engenheiro de Software",
                about = listOf(
                    "Engenheiro de software com 10 anos de experiência entregando soluções tecnológicas. Um programador ávido e com habilidade de aprendizado rápida.",
                    "Sempre advogo para a melhor experiência de usuário garantindo viabilidade tecnológica. Tenho facilidade em trabalhar com\\ndiversas linguagens, frameworks e paradigmas ao mesmo tempo.",
                    "Um desenvolvedor orientado a time que entende que juntos, todos crescemos. Amo soluções elegantes e simples para todos os problemas, mas entendendo que esta elegância e simplicidade não podem sobrepor a performance do software.",
                ),
            ),
            skills = HomePage.SkillSection(
                languages = HomePage.SkillSection.Languages(
                    title = "Languages",
                    en = "Inglês",
                    pt = "Português",
                ),
                programingLanguages = HomePage.SkillSection.ProgrammingLanguages(
                    title = "Habilidades",
                    ctaShowMore = "Mostrar mais",
                    ctaShowLess = "Mostrar menos",
                ),
            ),
            historySection = HomePage.HistorySection(
                work = HomePage.HistorySection.WorkHistory(
                    title = "Experiência",
                    technologiesUsed = "Tecnologias utilizadas:",
                    experiences = listOf(
                        HomePage.HistorySection.Experience(
                            name = "REDspace",
                            description = ExperienceParagraph(
                                highlight = "Projetando e desenvolvendo aplicações mobile robustas, fácil manutenção e em conformidade com os padrões de aplicações e bibliotecas para a plataforma Android.",
                                "Projetando, arquitetando e mantendo código Kotlin/Java de alto desempenho, reutilizável e confiável.",
                                "Usando técnicas e softwares de profiling para identificar e corrigir problemas de gerenciamento de memória e gargalos de desempenho.",
                                "Entregando apps robustos, de alta qualidade dentro do cronograma.",
                            ),
                            title = "Android Engineer",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "BairesDev / Pinterest",
                            description = ExperienceParagraph(
                                highlight = "Desenvolvendo soluções, optimizações e mantendo frameworks que ajudam o time de engenheiros de Software a entregar a melhor experiência possível para quem utiliza o Pinterest",
                                "Trabalhando com dynamic feature module, melhorando a velocidade de compilação do app.",
                                "Trabalhando em conjunto com o time de engenheiros de produto criando facilitadores para desenvolvimento de novos produtos.",
                                "Manutenção de unit e integration tests garantindo a qualidade de código",
                                "Iniciei o processo de migração de views que utilizavão XML para o Jetpack Compose",
                            ),
                            title = "Android Engineer",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Questrade Financial Group",
                            description = ExperienceParagraph(
                                highlight = "Liderança técnica, guiando o novo time que trabalha nos novos apps para Questrade, usando React Native. Participação direta no desenho e riação =  soluções baseadas em eventos com alta disponibilidade para suportar os novos aplicativos e a enorme demanda dos clientes",
                                "Implementação de React Native module para suportar autenticação utilizando biometria e OIDC Auth code + PCKE flow.",
                                "Projetado e desenvolvido microsserviços baseados em eventos usando Apache Kafkae Google Pub-Sub.",
                                "Projetado e um serviço mock com validação de payload, baseado no contrato de API Open API, a fim de permitir o desenvolvimento móvel durante a criação dos endpoints.",
                            ),
                            title = "Principal Software Engineer",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Questrade Financial Group",
                            description = ExperienceParagraph(
                                highlight = "Implementadas novas funcionalidades e melhorou o desempenho e a qualidade do app híbrido móvel Questrade. Implementação de um novo método de utenticação =tilizando autenticações biométricos (Fingerprint para Android, TouchID e FaceID para iOS). Desenvolvimento de todo um novo processo de utenticação = ra mobile, desktop e web, baseado em Identity Server",
                                "Aumento da satisfação do cliente no aplicativo móvel híbrido de 1,5 para 3,3 estrelas para um aplicativo com mais de 100.000 uso diário.",
                                "Criado um método de autenticação segura para o app usando código nativo (Kotlin para Android e Swift para iOS), com todos os dados criptografados usando RSA.",
                                "Execução de um novo projeto de autenticação desde o conceito até a conclusão, incluindo integração móvel, implementação de front e back-end, registro, métricas e entrega.",
                            ),
                            title = "Senior Software Engineer",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Axxiom",
                            description = ExperienceParagraph(
                                highlight = "Projetei e implementei um novo aplicativo do zero para a CEMIG, empresa mineira de energia, usando Android nativo. Integração com Google Maps mostrando objetos que precisam de reparos ou instalação",
                                "Projetado um aplicativo usando o padrão de design de materiais.",
                                "Aplicação distribuída que se comunica com os outros usando AIDL.",
                                "Concebido e desenvolvido um centro de notificação que fornece mensagens entre a base e os aplicativos usando a melhor conexão disponível (Wi-Fi, rede móvel ou conexão de satélite).",
                            ),
                            title = "Analista Desenvolvedor Mobile",
                        ),
                        HomePage.HistorySection.Experience(
                            name = "Planear Sistemas",
                            description = ExperienceParagraph(
                                highlight = "Soluções para a área de gestão de saúde e para a área de marketing. Desenvolvido um aplicativo móvel para gerenciar agendamento médico, rescrições =  tratamento e recebimentos financeiros. Também foi desenvolvido um aplicativo para a área de marketing criando pesquisas dinâmicas no aplicativo mobile",
                                "Implementado ambas as soluções móveis e da Web para gerenciar clínicas de saúde e policlínicas públicas.",
                                "Criado um aplicativo dinâmico que recebe dados do serviço RESTful e constrói uma forma dinâmica com base nisso.",
                                "Criado uma rede social para a política de ajuda e seus partidários em sua campanha usando gamification.",
                            ),
                            title = "Analista Desenvolvedor .NET",
                        ),
                    ),
                ),
                educational = HomePage.HistorySection.EducationalHistory(
                    title = "Formação acadêmica",
                    experiences = listOf(
                        HomePage.HistorySection.Experience(
                            name = "Centro universitário UNA",
                            title = "Pós graduação, Desenvolvimento de aplicativos móveis",
                            description = ExperienceParagraph(
                                "- Desenolvimento de aplicativos para iOS, Android e Windows Phone.\n- Desenvolvimento de aplicações híbridas com foco em Cordova (ionic) e Xamarin.",
                            ),
                        ),
                        HomePage.HistorySection.Experience(
                            name = "PUC-MG",
                            title = "Tecnólogo, Jogos digitais",
                            description = ExperienceParagraph(
                                "Desenvolvimento de jogos digitais utilizando Unity3D engine, DirectX e OpenGL."
                            ),
                        ),
                        HomePage.HistorySection.Experience(
                            name = "COTEMIG",
                            title = "Técnico em informática",
                            description = ExperienceParagraph(
                                "",
                            ),
                        ),
                    ),
                ),
            ),
        ),
    ),
    navBar = NavBar(
        home = "Início",
        articles = "Artigos",
        resume = "Currículo",
    )
)
