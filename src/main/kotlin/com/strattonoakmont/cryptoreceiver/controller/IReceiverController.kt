package com.strattonoakmont.cryptoreceiver.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.strattonoakmont.cryptoreceiver.service.AmountRequestDto
import org.springframework.web.bind.annotation.RequestBody
import java.time.ZonedDateTime

interface IReceiverController {
    fun receive(@RequestBody receiveCryptoRequest: ReceiveCryptoRequest): ReceiveCryptoResponse
    fun getAmount(@RequestBody amountRequest: AmountRequest): AmountResponse
}

data class ReceiveCryptoRequest(val datetime: String, val amount: String)

data class ReceiveCryptoResponse(var status: ReceiveStatus, var errors: MutableList<String>)

enum class ReceiveStatus {
    SUCCESS,
    FAILURE
}

data class AmountRequest(val startDatetime: String, val endDatetime: String)

data class AmountForPeriod(
    @JsonProperty("datetime") var datetime: ZonedDateTime,
    @JsonProperty("amount") var amount: Double
) {
    class Deserializer : ResponseDeserializable<Array<AmountForPeriod>> {
        override fun deserialize(content: String): Array<AmountForPeriod>
                = Gson().fromJson(content, Array<AmountForPeriod>::class.java)
    }
}

data class AmountList(var amountForPeriodList: MutableList<AmountForPeriod>) {
    class Deserializer : ResponseDeserializable<AmountList> {
        override fun deserialize(content: String): AmountList
                = Gson().fromJson(content, AmountList::class.java)
    }
}

data class AmountResponse(
    @JsonProperty("amountList") var amountList: MutableList<AmountForPeriod> = mutableListOf(),
    var status: ReceiveStatus = ReceiveStatus.SUCCESS, var errors: MutableList<String> = mutableListOf()
)
