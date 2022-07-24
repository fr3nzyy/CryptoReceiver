package com.strattonoakmont.cryptoreceiver.controller

import com.strattonoakmont.cryptoreceiver.service.AmountRequestDto
import com.strattonoakmont.cryptoreceiver.service.CryptoTransferDTO
import com.strattonoakmont.cryptoreceiver.service.ICryptoInfoService
import com.strattonoakmont.cryptoreceiver.service.KafkaProducer
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.NumberFormatException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
class ReceiverController(
    val kafkaProducer: KafkaProducer,
    val iCryptoInfoService: ICryptoInfoService
): IReceiverController {

    private val DATE_PARSE_ERROR = "Date parsing error. Wrong format" +
            "Available formats: '2011-12-03T10:15:30' or '2011-12-03T10:15:30+01:00' or '2011-12-03T10:15:30+01:00[Europe/Paris]'."

    @PostMapping(value = ["send_crypto"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun receive(@RequestBody receiveCryptoRequest: ReceiveCryptoRequest): ReceiveCryptoResponse {
        val response = ReceiveCryptoResponse(ReceiveStatus.SUCCESS, mutableListOf())
        var datetime: ZonedDateTime? = null
        var amount: Double? = null

        try {
            datetime = ZonedDateTime.parse(receiveCryptoRequest.datetime, DateTimeFormatter.ISO_DATE_TIME)
        } catch (ex: DateTimeParseException) {
            response.errors.add(
                DATE_PARSE_ERROR
            )
        }
        try {
            amount = receiveCryptoRequest.amount.toDouble()
        } catch (ex: NumberFormatException) {
            response.errors.add("Amount parsing error. Wrong format. Must be Double")
        }

        if (response.errors.isNotEmpty()) {
            response.status = ReceiveStatus.FAILURE
            return response
        }

        kafkaProducer.sendToKafka(CryptoTransferDTO(datetime!!, amount!!))
        return response
    }

    @PostMapping(value = ["get_crypto_for_period"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun getAmount(@RequestBody amountRequest: AmountRequest): AmountResponse {
        val response = AmountResponse()
        var startDatetime: ZonedDateTime? = null
        var endDatetime: ZonedDateTime? = null

        try {
            startDatetime = ZonedDateTime.parse(amountRequest.startDatetime, DateTimeFormatter.ISO_DATE_TIME)
        } catch (ex: DateTimeParseException) {
            response.errors.add(
                DATE_PARSE_ERROR
            )
        }
        try {
            endDatetime = ZonedDateTime.parse(amountRequest.endDatetime, DateTimeFormatter.ISO_DATE_TIME)
        } catch (ex: DateTimeParseException) {
            response.errors.add(
                DATE_PARSE_ERROR
            )
        }
        if (response.errors.isNotEmpty()) {
            response.status = ReceiveStatus.FAILURE
            return response
        }
        response.amountList = iCryptoInfoService.getAmountListForPeriod(
            AmountRequestDto(startDatetime!!, endDatetime!!))
        return response
    }
}