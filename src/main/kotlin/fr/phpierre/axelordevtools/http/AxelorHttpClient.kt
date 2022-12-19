package fr.phpierre.axelordevtools.http

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.phpierre.axelordevtools.http.model.AxelorModel
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import org.apache.http.Header
import org.apache.http.NameValuePair
import org.apache.http.client.CookieStore
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.cookie.Cookie
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicNameValuePair

abstract class AxelorHttpClient<T> {

    object HttpClient {
        val builder: HttpClientBuilder
        private val httpCookieStore: CookieStore

        init {
            // https://stackoverflow.com/questions/8733758/how-can-i-get-the-cookies-from-httpclient
            httpCookieStore = BasicCookieStore()
            builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore)
        }

        fun getAxelorCookie(): Cookie? {
            for (cookie in httpCookieStore.getCookies()) {
                if(cookie.name == "JSESSIONID") {
                    return cookie
                }
            }

            return null
        }
    }

    companion object {
        val mapper = jacksonObjectMapper()

        fun <T> request(clientRequest: AxelorHttpClient<T>): AxelorResponseBody<T> {
            if(HttpClient.getAxelorCookie() == null) {
                println("Auth")
                auth()
            }

            var response = clientRequest.request()

            // Ask a new token
            if(response.status != 200) {
                println("Token invalide, generation d'un nouveau token")
                auth()
                response = clientRequest.request()
            }

            return response
        }

        private fun auth() : AxelorResponseBody<Any> {
            val response: CloseableHttpResponse = HttpClient.builder.build().execute(authRequest())
            return AxelorResponseBody(response.statusLine.statusCode, Any())
        }


        private fun authRequest() : HttpEntityEnclosingRequestBase {
            val settings: AxelorSettingsState = AxelorSettingsState.getInstance()

            val url = settings.getUrlConfig()
            val post = HttpPost(url + "login.jsp")
            val parameters = mutableListOf<NameValuePair>()
            parameters.add(BasicNameValuePair("username", settings.erpUsername))
            parameters.add(BasicNameValuePair("password", settings.erpPassword))

            post.entity = UrlEncodedFormEntity(parameters)
            return post
        }


        fun getRequestHeaders() : Array<Header> {
            return arrayOf(
                    BasicHeader("Accept", "application/json"),
                    BasicHeader("Content-Type", "application/json"),
                    BasicHeader("Access-Control-Allow-Origin", "*")
            )
        }

        fun isOnline(): Boolean {
            val settings: AxelorSettingsState = AxelorSettingsState.getInstance()

            val url = settings.getUrlConfig()
            val get = HttpGet(url)
            try {
                val response: CloseableHttpResponse = HttpClient.builder.build().execute(get)
            } catch (e: Exception) {
                return false
            }

            return true
        }
    }

    abstract fun request() : AxelorResponseBody<T>

    fun toEntity(responseBody: String): AxelorResponseBody<T> {
        return try {
            //https://stackoverflow.com/questions/6846244/jackson-and-generic-type-reference
            mapper.readValue(responseBody, object : TypeReference<AxelorResponseBody<T>>() {})
        } catch (e: MismatchedInputException) {
            AxelorResponseBody(401, null)
        }
    }
}