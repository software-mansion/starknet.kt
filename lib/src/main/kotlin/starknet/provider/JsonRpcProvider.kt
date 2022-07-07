package starknet.provider

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import starknet.data.types.*
import java.util.concurrent.atomic.AtomicLong

class JsonRpcProvider(
    private val url: String,
    override val chainId: StarknetChainId
) : Provider {
    companion object {
        private val nextId = AtomicLong(0)
    }

    private fun buildRequestJson(id: Long, method: String, paramsJson: JsonElement): Map<String, JsonElement> {
        val map = mapOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive(method),
            "id" to JsonPrimitive(id),
            "params" to paramsJson
        )

        return JsonObject(map)
    }

    private fun <T : Response> buildRequest(
        method: JsonRpcMethod,
        paramsJson: JsonElement,
        deserializer: DeserializationStrategy<T>
    ): Request<T> {
        val id = nextId.getAndIncrement()

        val requestJson = buildRequestJson(id, method.methodName, paramsJson)

        return Request(url, "POST", emptyList(), requestJson.toString(), deserializer)
    }

    override fun callContract(payload: CallContractPayload): Request<CallContractResponse> {
        val params = Json.encodeToJsonElement(payload)

        return buildRequest(JsonRpcMethod.CALL, params, CallContractResponse.serializer())
    }

    override fun getStorageAt(payload: GetStorageAtPayload): Request<GetStorageAtResponse> {
        val params = Json.encodeToJsonElement(payload)

        return buildRequest(JsonRpcMethod.GET_STORAGE_AT, params, GetStorageAtResponse.serializer())
    }

    override fun invokeFunction(
        payload: InvokeFunctionPayload
    ): Request<InvokeFunctionResponse> {
        val params = Json.encodeToJsonElement(payload)

        return buildRequest(JsonRpcMethod.INVOKE_TRANSACTION, params, InvokeFunctionResponse.serializer())
    }
}