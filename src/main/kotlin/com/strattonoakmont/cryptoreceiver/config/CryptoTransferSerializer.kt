package com.strattonoakmont.cryptoreceiver.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.strattonoakmont.cryptoreceiver.service.CryptoTransferDTO
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory


class CryptoTransferSerializer: Serializer<CryptoTransferDTO> {

    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private val log = LoggerFactory.getLogger(javaClass)

    override fun serialize(topic: String?, transfer: CryptoTransferDTO?): ByteArray {
        log.info("Serializing...")
        return objectMapper.writeValueAsBytes(
            transfer ?: throw SerializationException("Error when serializing Product to ByteArray[]")
        )
    }
}