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

class MetaViewRestoreAll : AxelorHttpClient<List<NotifyModel>>() {

    override fun request(): AxelorResponseBody<List<NotifyModel>> {
        val axelorRequestBody = AxelorRequestBody("com.axelor.meta.db.MetaView", "action-meta-restore-all")
        val request = createRequest(axelorRequestBody)
        val response: CloseableHttpResponse = HttpClient.builder.build().execute(request)
        val responseBody = EntityUtils.toString(response.entity)
        EntityUtils.consume(response.entity)

        val responseEntity = mapper.readValue(responseBody, object : TypeReference<AxelorResponseBody<List<NotifyModel>>>() {})
        responseEntity.status = response.statusLine.statusCode
        return responseEntity
    }

    private fun createRequest(axelorRequestBody: AxelorRequestBody): HttpPost {
        val url = AxelorSettingsState.getInstance().getActionUrlConfig()
        val post = HttpPost(url)
        post.setHeaders(getRequestHeaders())
        val body = mapper.writeValueAsString(axelorRequestBody)
        post.entity = StringEntity(body, ContentType.APPLICATION_JSON)

        return post
    }
}