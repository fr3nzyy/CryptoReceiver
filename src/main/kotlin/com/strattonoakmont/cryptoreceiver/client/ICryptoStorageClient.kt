package com.strattonoakmont.cryptoreceiver.client

import com.strattonoakmont.cryptoreceiver.controller.AmountForPeriod
import com.strattonoakmont.cryptoreceiver.service.AmountRequestDto
import feign.Headers
import feign.RequestLine

interface ICryptoStorageClient {
    @RequestLine("POST /get_crypto_for_period")
    @Headers("Content-Type: application/json")
    fun getAmountListForPeriod(amountRequestDto: AmountRequestDto): AmountListForPeriod
//    fun getAmountListForPeriod(amountRequest: AmountRequest): Response
}

data class AmountListForPeriod(val amountList: MutableList<AmountForPeriod>)
