package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LyricistStrings
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.portfolio.MyArticleEn
import dev.tonholo.portfolio.MyArticlePtBr
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.workExperience.Axxiom
import dev.tonholo.portfolio.resources.workExperience.BairesDev
import dev.tonholo.portfolio.resources.workExperience.Planear
import dev.tonholo.portfolio.resources.workExperience.QuestradePrincipal
import dev.tonholo.portfolio.resources.workExperience.QuestradeSenior
import dev.tonholo.portfolio.resources.workExperience.REDSpace
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

data class NavBar(
    val home: String,
    val articles: String,
    val resume: String,
)

data class Screen(
    val home: HomePage,
    val articles: List<MarktdownDocument>,
)

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
            val presentTag: String,
            val technologiesUsed: String,
            override val experiences: List<Experience>,
        ) : History

        data class EducationalHistory(
            override val title: String,
            override val experiences: List<Experience>,
        ) : History

        data class Experience(
            val key: Long = Clock.System.now().epochSeconds,
            val name: String,
            val title: String,
            val description: Description,
            val starting: LocalDate? = null,
            val ending: LocalDate? = null,
            val technologiesUsed: List<String> = emptyList(),
        )
    }
}

data class Description(
    val summary: String,
    val bulletPoints: List<String> = emptyList(),
) {
    companion object {
        operator fun invoke(
            summary: String,
            vararg bulletPoints: String,
        ): Description = Description(
            summary = summary,
            bulletPoints = bulletPoints.toList(),
        )
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
                jobTitle = "A Android Mage. Kotlin/Compose 💜",
                about = listOf(
                    "Experienced Android engineer with 10+ years in software development, 5+ years specializing in Kotlin for native Android apps.",
                    "Deep grasp of Android best practices, keen on user-centric design, adept at translating complex ideas into scalable solutions.",
                    "Skilled at fostering growth in collaborative, fast-paced settings.",
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
                    presentTag = "Present",
                    technologiesUsed = "Technologies used:",
                    experiences = listOf(
                        REDSpace,
                        BairesDev,
                        QuestradePrincipal,
                        QuestradeSenior,
                        Axxiom,
                        Planear,
                    ),
                ),
                educational = HomePage.HistorySection.EducationalHistory(
                    title = "Education",
                    experiences = listOf(
                        HomePage.HistorySection.Experience(
                            name = "University center UNA",
                            title = "Post-graduate, Development of Application for Mobile Devices",
                            description = Description(
                                "- Development of applications to iOS, Android, Windows Phone.\n- Development of hybrid applications focused on Cordova (ionic) and Xamarin.",
                            ),
                            starting = LocalDate.parse("2015-03-20"),
                            ending = LocalDate.parse("2016-01-09"),
                        ),
                        HomePage.HistorySection.Experience(
                            name = "PUC-MG",
                            title = "Technologist, Digital games",
                            description = Description(
                                "Development of Digital Games using Unity3D engine, DirectX and OpenGL.",
                            ),
                            starting = LocalDate.parse("2012-02-01"),
                            ending = LocalDate.parse("2014-09-10"),
                        ),
                        HomePage.HistorySection.Experience(
                            name = "COTEMIG",
                            title = "Computer Technician",
                            description = Description(
                                "",
                            ),
                            starting = LocalDate.parse("2010-01-01"),
                            ending = LocalDate.parse("2012-01-01"),
                        ),
                    ),
                ),
            ),
        ),
        articles = listOf(
            MyArticleEn,
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
                jobTitle = "Um Mago do Android. Kotlin/Compose 💜",
                about = listOf(
                    "Engenheiro Android experiente com mais de 10 anos em desenvolvimento de software, e mais de 5 anos desenvolvendo em Kotlin para aplicações Android nativas.",
                    "Adepto das melhores práticas do Android, sempre busco um desenvolvimento centrado à experiência de usuário traduzindo ideias complexas em soluções escaláveis.",
                    "Busco promover o crescimento do time em ambientes colaborativos e de ritmo acelerado.",
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
                    presentTag = "Atual",
                    technologiesUsed = "Tecnologias utilizadas:",
                    experiences = listOf(
                        REDSpace.copy(
                            description = Description(
                                "Projetando e desenvolvendo aplicações mobile robustas, fácil manutenção e em conformidade com os padrões de aplicações e bibliotecas para a plataforma Android.",
                                "Projetando, arquitetando e mantendo código Kotlin/Java de alto desempenho, reutilizável e confiável.",
                                "Usando técnicas e softwares de profiling para identificar e corrigir problemas de gerenciamento de memória e gargalos de desempenho.",
                                "Entregando apps robustos, de alta qualidade dentro do cronograma.",
                            ),
                            title = "Android Engineer",
                        ),
                        BairesDev.copy(
                            description = Description(
                                "Desenvolvendo soluções, optimizações e mantendo frameworks que ajudam o time de engenheiros de Software a entregar a melhor experiência possível para quem utiliza o Pinterest",
                                "Trabalhando com dynamic feature module, melhorando a velocidade de compilação do app.",
                                "Trabalhando em conjunto com o time de engenheiros de produto criando facilitadores para desenvolvimento de novos produtos.",
                                "Manutenção de unit e integration tests garantindo a qualidade de código",
                                "Iniciei o processo de migração de views que utilizavão XML para o Jetpack Compose",
                            ),
                            title = "Android Engineer",
                        ),
                        QuestradePrincipal.copy(
                            description = Description(
                                "Liderança técnica, guiando o novo time que trabalha nos novos apps para Questrade, usando React Native. Participação direta no desenho e riação =  soluções baseadas em eventos com alta disponibilidade para suportar os novos aplicativos e a enorme demanda dos clientes",
                                "Implementação de React Native module para suportar autenticação utilizando biometria e OIDC Auth code + PCKE flow.",
                                "Projetado e desenvolvido microsserviços baseados em eventos usando Apache Kafkae Google Pub-Sub.",
                                "Projetado e um serviço mock com validação de payload, baseado no contrato de API Open API, a fim de permitir o desenvolvimento móvel durante a criação dos endpoints.",
                            ),
                            title = "Principal Software Engineer",
                        ),
                        QuestradeSenior.copy(
                            description = Description(
                                "Implementadas novas funcionalidades e melhorou o desempenho e a qualidade do app híbrido móvel Questrade. Implementação de um novo método de utenticação =tilizando autenticações biométricos (Fingerprint para Android, TouchID e FaceID para iOS). Desenvolvimento de todo um novo processo de utenticação = ra mobile, desktop e web, baseado em Identity Server",
                                "Aumento da satisfação do cliente no aplicativo móvel híbrido de 1,5 para 3,3 estrelas para um aplicativo com mais de 100.000 uso diário.",
                                "Criado um método de autenticação segura para o app usando código nativo (Kotlin para Android e Swift para iOS), com todos os dados criptografados usando RSA.",
                                "Execução de um novo projeto de autenticação desde o conceito até a conclusão, incluindo integração móvel, implementação de front e back-end, registro, métricas e entrega.",
                            ),
                            title = "Senior Software Engineer",
                        ),
                        Axxiom.copy(
                            description = Description(
                                "Projetei e implementei um novo aplicativo do zero para a CEMIG, empresa mineira de energia, usando Android nativo. Integração com Google Maps mostrando objetos que precisam de reparos ou instalação",
                                "Projetado um aplicativo usando o padrão de design de materiais.",
                                "Aplicação distribuída que se comunica com os outros usando AIDL.",
                                "Concebido e desenvolvido um centro de notificação que fornece mensagens entre a base e os aplicativos usando a melhor conexão disponível (Wi-Fi, rede móvel ou conexão de satélite).",
                            ),
                            title = "Analista Desenvolvedor Mobile",
                        ),
                        Planear.copy(
                            description = Description(
                                "Soluções para a área de gestão de saúde e para a área de marketing. Desenvolvido um aplicativo móvel para gerenciar agendamento médico, rescrições =  tratamento e recebimentos financeiros. Também foi desenvolvido um aplicativo para a área de marketing criando pesquisas dinâmicas no aplicativo mobile",
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
                            description = Description(
                                "- Desenolvimento de aplicativos para iOS, Android e Windows Phone.\n- Desenvolvimento de aplicações híbridas com foco em Cordova (ionic) e Xamarin.",
                            ),
                        ),
                        HomePage.HistorySection.Experience(
                            name = "PUC-MG",
                            title = "Tecnólogo, Jogos digitais",
                            description = Description(
                                "Desenvolvimento de jogos digitais utilizando Unity3D engine, DirectX e OpenGL."
                            ),
                        ),
                        HomePage.HistorySection.Experience(
                            name = "COTEMIG",
                            title = "Técnico em informática",
                            description = Description(
                                "",
                            ),
                        ),
                    ),
                ),
            ),
        ),
        articles = listOf(
            MyArticlePtBr,
        ),
    ),
    navBar = NavBar(
        home = "Início",
        articles = "Artigos",
        resume = "Currículo",
    )
)
