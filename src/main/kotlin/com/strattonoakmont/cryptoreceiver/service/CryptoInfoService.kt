package com.strattonoakmont.cryptoreceiver.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpPost
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.strattonoakmont.cryptoreceiver.controller.AmountForPeriod
import com.strattonoakmont.cryptoreceiver.controller.AmountList
import org.springframework.stereotype.Service
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.*

@Service
class CryptoInfoService : ICryptoInfoService {
    override fun getAmountListForPeriod(amountRequestDto: AmountRequestDto): MutableList<AmountForPeriod> {
        val objectMapper: ObjectMapper = ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(JavaTimeModule())
            .registerModule(ParameterNamesModule())
            .registerModule(Jdk8Module())
        val value = objectMapper.writeValueAsString(amountRequestDto)
        println(value)
        val bodyJson = """
                          { "startDatetime" : "2019-10-05T14:45:05+07:00",
                            "endDatetime" : "2019-10-05T16:45:05+07:00"
                          }
                        """
        val (request, response, result) = Fuel.post("http://localhost:8081/get_crypto_for_period")
            .body(value)
            .header("Content-Type", "application/json")
            .responseObject(AmountList.Deserializer())
        print(response)
        var mutableListOf = mutableListOf<AmountForPeriod>()
        "http://localhost:8081/get_crypto_for_period".httpPost().body(value)
            .header("Content-Type", "application/json")
            .responseObject(AmountList.Deserializer()){ request, response, result ->
                val (people, err) = result
                print(11)
//                mutableListOf = people!!
            }
        println(mutableListOf)

        return mutableListOf
    }

}


internal class LocalDateTimeDeserializer : JsonDeserializer<ZonedDateTime?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ZonedDateTime {
        return ZonedDateTime.parse(json.asString, ISO_OFFSET_DATE_TIME.withLocale(Locale.ENGLISH))
//        return ZonedDateTime.parse(json.asString, ISO_OFFSET_DATE_TIME)
    }
}

//    override fun getAmountListForPeriod(amountRequestDto: AmountRequestDto): MutableList<AmountForPeriod> {
//        val gson = GsonBuilder().registerTypeAdapter(ZonedDateTime::class.java, LocalDateTimeDeserializer())
//            .setPrettyPrinting().create()
//
//        val client: ICryptoStorageClient =
//            Feign.builder().client(OkHttpClient()).encoder(GsonEncoder(gson)).decoder(GsonDecoder(gson))
//                .logger(Slf4jLogger(ICryptoStorageClient::class.java)).logLevel(Logger.Level.FULL)
//                .target(ICryptoStorageClient::class.java, "http://localhost:8081/")
////        val amountListForPeriod = client.getAmountListForPeriod(amountRequestDto)
//        new()
////        return amountListForPeriod.amountList.map { AmountForPeriod(it.datetime, it.amount) }.toMutableList()
//        return mutableListOf()