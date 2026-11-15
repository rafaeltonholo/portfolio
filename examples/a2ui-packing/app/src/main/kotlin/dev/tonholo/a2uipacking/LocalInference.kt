package dev.tonholo.a2uipacking

import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.SamplerConfig
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

private const val MAX_OUTPUT_TOKENS = 512
private const val MAX_RESPONSE_CHARACTERS = 65_536

suspend fun generateResponse(modelPath: String, prompt: String): String {
    return Executors.newSingleThreadExecutor().asCoroutineDispatcher().use { dispatcher ->
        withContext(dispatcher) {
            val engine = Engine(EngineConfig(modelPath = modelPath))
            engine.initialize()
            engine.use {
                engine.createConversation(
                    ConversationConfig(
                        samplerConfig = SamplerConfig(topK = 10, topP = 0.95, temperature = 0.2),
                        maxOutputToken = MAX_OUTPUT_TOKENS,
                    ),
                ).use { conversation ->
                    val response = StringBuilder()
                    conversation.sendMessageAsync(prompt).collect { message ->
                        response.append(message)
                        require(response.length <= MAX_RESPONSE_CHARACTERS) { "Model response is too long" }
                    }
                    response.toString()
                }
            }
        }
    }
}
