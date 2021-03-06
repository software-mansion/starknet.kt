@file:JvmName("Responses")

package com.swmansion.starknet.data.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
sealed class Response

@Serializable
data class CallContractResponse(
    val result: List<Felt>,
) : Response()

@Serializable
data class InvokeFunctionResponse(
    @SerialName("transaction_hash") val transactionHash: Felt,
) : Response()

@OptIn(ExperimentalSerializationApi::class)
@Serializable
// OptIn needed because @JsonNames is part of the experimental serialization api
data class DeployResponse(
    @JsonNames("transaction_hash")
    val transactionHash: Felt,

    @JsonNames("contract_address", "address")
    val contractAddress: Felt,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class DeclareResponse(
    @JsonNames("transaction_hash")
    val transactionHash: Felt,

    @JsonNames("class_hash")
    val classHash: Felt,
)

@Serializable
data class GetStorageAtResponse(
    val result: Felt,
) : Response()

data class TransactionFailureReason(
    val code: String,
    val errorMessage: String,
)
