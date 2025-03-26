package com.trevorwiebe.timesheet.core.data

import com.trevorwiebe.timesheet.core.domain.HttpInterface
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.HolidayDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException

class HttpInterfaceImpl(
    private val httpClient: HttpClient,
) : HttpInterface {

    override suspend fun getHolidays(year: String, countryCode: String): TSResult {
        val response = try {
            httpClient.get(
                urlString = "https://date.nager.at/api/v3/PublicHolidays/$year/$countryCode"
            )
        } catch (e: UnresolvedAddressException) {
            return TSResult(error = "No network connection")
        } catch (e: ServerResponseException) {
            return TSResult(error = "Data parsing error")
        }
        return when (response.status.value) {
            in 200..299 -> TSResult(data = response.body<List<HolidayDto>>())
            401 -> TSResult(error = "Unauthorized")
            403 -> TSResult(error = "Forbidden")
            404 -> TSResult(error = "Not found")
            409 -> TSResult(error = "Conflict")
            413 -> TSResult(error = "Request entity too large")
            in 500..599 -> TSResult(error = "Server error")
            else -> TSResult(error = "Unknown error")
        }
    }
}