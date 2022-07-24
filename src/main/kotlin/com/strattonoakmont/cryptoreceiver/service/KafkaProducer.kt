package com.strattonoakmont.cryptoreceiver.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class KafkaProducer(
    @Value("\${kafka.topics.crypto}")
    val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Any>
): IKafkaProducer {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun sendToKafka(kafkaModel: CryptoTransferDTO) {
        log.info("Receiving kafkaModel request")
        log.info("Sending message to Kafka {}", kafkaModel)
        val message: Message<CryptoTransferDTO> = MessageBuilder
            .withPayload(kafkaModel)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build()
        kafkaTemplate.send(message)
    }

}