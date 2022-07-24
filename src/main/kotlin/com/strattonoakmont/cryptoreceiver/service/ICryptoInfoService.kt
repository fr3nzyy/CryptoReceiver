package com.strattonoakmont.cryptoreceiver.service

import com.fasterxml.jackson.annotation.JsonFormat
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.strattonoakmont.cryptoreceiver.controller.AmountForPeriod
import java.time.ZonedDateTime

interface ICryptoInfoService {
    fun getAmountListForPeriod(amountRequestDto: AmountRequestDto): MutableList<AmountForPeriod>
}

data class AmountRequestDto(
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    val startDatetime: ZonedDateTime,
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    val endDatetime: ZonedDateTime)

