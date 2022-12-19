package fr.phpierre.axelordevtools.http.request.metaview.hotreload

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorRequestBody
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.AxelorModel
import fr.phpierre.axelordevtools.http.model.IdeHotReloadView
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils

class ViewHotReload(val views: Set<String>) : AxelorHttpClient<List<NotifyModel>>() {

    override fun request(): AxelorResponseBody<List<NotifyModel>> {
        val axelorRequestBody = AxelorRequestBody("fr.phpierre.axelor.intellij.hotreload.IdeHotReload", "fr.phpierre.axelor.intellij.hotreload.ViewHotReloadController:reloadFiles")

        val viewsList = views.map { IdeHotReloadView(it) }.toSet()
        axelorRequestBody.addInContext("views", viewsList)

        val request = createRequest(axelorRequestBody)
        val response: CloseableHttpResponse = HttpClient.builder.build().execute(request)
        val responseBody = EntityUtils.toString(response.entity)
        EntityUtils.consume(response.entity)

        return try {
            val responseEntity = mapper.readValue(responseBody, object : TypeReference<AxelorResponseBody<List<NotifyModel>>>() {})
            responseEntity.status = response.statusLine.statusCode
            responseEntity
        } catch (e: MismatchedInputException) {
            AxelorResponseBody(401, null)
        }
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