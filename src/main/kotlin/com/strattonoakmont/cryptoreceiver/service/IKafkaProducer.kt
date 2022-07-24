package com.strattonoakmont.cryptoreceiver.service

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

interface IKafkaProducer {
    fun sendToKafka(kafkaModel: CryptoTransferDTO)
}

data class CryptoTransferDTO(
    @JsonProperty("datetime") val datetime: ZonedDateTime,
    @JsonProperty("amount") var amount: Double
)