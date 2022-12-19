package fr.phpierre.axelordevtools.http.request.metaview

import com.fasterxml.jackson.core.type.TypeReference
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorRequestBody
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.AxelorModel
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils

class MetaViewRemoveAll(val views: List<AxelorModel>) : AxelorHttpClient<List<AxelorModel>>() {

    override fun request(): AxelorResponseBody<List<AxelorModel>> {
        val axelorRequestBody = AxelorRequestBody("", "")
        axelorRequestBody.records.addAll(views)
        val request = createRequest(axelorRequestBody)
        val response: CloseableHttpResponse = HttpClient.builder.build().execute(request)
        val responseBody = EntityUtils.toString(response.entity)
        EntityUtils.consume(response.entity)

        val responseEntity =  mapper.readValue(responseBody, object : TypeReference<AxelorResponseBody<List<AxelorModel>>>() {})
        responseEntity.status = response.statusLine.statusCode
        return responseEntity
    }

    private fun createRequest(axelorRequestBody: AxelorRequestBody): HttpPost {
        val url = AxelorSettingsState.getInstance().getUrlConfig() + "ws/rest/com.axelor.meta.db.MetaView/removeAll"
        val post = HttpPost(url)
        post.setHeaders(getRequestHeaders())
        val body = mapper.writeValueAsString(axelorRequestBody)
        post.entity = StringEntity(body, ContentType.APPLICATION_JSON)

        return post
    }

    //http://localhost:8080/orion-agrikolis/ws/rest/com.axelor.meta.db.MetaView/removeAll
    //{"records":[{"id":541,"version":0},{"id":530,"version":0},{"id":529,"version":0},{"id":542,"version":0}]}
}