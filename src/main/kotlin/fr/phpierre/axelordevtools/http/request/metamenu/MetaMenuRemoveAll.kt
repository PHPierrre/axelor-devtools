package fr.phpierre.axelordevtools.http.request.metamenu

import com.fasterxml.jackson.core.type.TypeReference
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorRequestBody
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.AxelorModel
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils

class MetaMenuRemoveAll(val views: List<AxelorModel>) : AxelorHttpClient<List<AxelorModel>>() {

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
        val url = AxelorSettingsState.getInstance().getUrlConfig() + "ws/rest/com.axelor.meta.db.MetaMenu/removeAll"
        val post = HttpPost(url)
        post.setHeaders(getRequestHeaders())
        val body = mapper.writeValueAsString(axelorRequestBody)
        post.entity = StringEntity(body, ContentType.APPLICATION_JSON)

        return post
    }

    //http://localhost:8080/orion-agrikolis/ws/rest/com.axelor.meta.db.MetaMenu/removeAll
    //{"records":[{"id":342,"version":1},{"id":340,"version":0},{"id":412,"version":0},{"id":470,"version":0},{"id":413,"version":0},{"id":414,"version":0},{"id":416,"version":0},{"id":347,"version":1},{"id":259,"version":0},{"id":376,"version":0},{"id":366,"version":0},{"id":241,"version":20},{"id":315,"version":1},{"id":331,"version":0},{"id":257,"version":0},{"id":263,"version":0},{"id":255,"version":0},{"id":321,"version":0},{"id":270,"version":1},{"id":256,"version":0},{"id":253,"version":0},{"id":252,"version":0},{"id":302,"version":1},{"id":293,"version":0},{"id":297,"version":0},{"id":311,"version":1},{"id":264,"version":0},{"id":258,"version":0},{"id":282,"version":0},{"id":260,"version":0},{"id":254,"version":0},{"id":374,"version":0},{"id":268,"version":0},{"id":304,"version":1},{"id":433,"version":0},{"id":434,"version":0},{"id":417,"version":0},{"id":345,"version":1},{"id":370,"version":0},{"id":477,"version":1},{"id":398,"version":0},{"id":371,"version":0},{"id":436,"version":0},{"id":301,"version":1},{"id":365,"version":0},{"id":361,"version":0},{"id":289,"version":1},{"id":369,"version":0},{"id":377,"version":0},{"id":272,"version":1},{"id":359,"version":0},{"id":456,"version":0},{"id":442,"version":0},{"id":452,"version":0},{"id":440,"version":0},{"id":423,"version":0},{"id":334,"version":1},{"id":362,"version":0},{"id":344,"version":1},{"id":285,"version":0},{"id":284,"version":0},{"id":378,"version":0},{"id":326,"version":1},{"id":299,"version":1},{"id":317,"version":1},{"id":322,"version":0},{"id":320,"version":0},{"id":448,"version":0},{"id":355,"version":1},{"id":364,"version":0},{"id":379,"version":0},{"id":474,"version":0},{"id":269,"version":0},{"id":287,"version":0},{"id":367,"version":0},{"id":349,"version":1},{"id":429,"version":0},{"id":428,"version":0},{"id":426,"version":0},{"id":424,"version":0},{"id":438,"version":0},{"id":458,"version":0},{"id":459,"version":0},{"id":360,"version":0},{"id":454,"version":0},{"id":249,"version":0},{"id":245,"version":0},{"id":325,"version":0},{"id":400,"version":0},{"id":469,"version":0},{"id":471,"version":0},{"id":348,"version":0},{"id":420,"version":0},{"id":373,"version":0},{"id":267,"version":0},{"id":266,"version":0},{"id":409,"version":0},{"id":332,"version":0},{"id":336,"version":1},{"id":406,"version":0},{"id":351,"version":1},{"id":294,"version":1},{"id":316,"version":0},{"id":468,"version":0},{"id":318,"version":0},{"id":408,"version":0},{"id":247,"version":0},{"id":439,"version":0},{"id":432,"version":0},{"id":265,"version":2},{"id":346,"version":1},{"id":335,"version":1},{"id":411,"version":0},{"id":305,"version":1},{"id":262,"version":0},{"id":405,"version":0},{"id":404,"version":0},{"id":401,"version":0},{"id":397,"version":0},{"id":402,"version":0},{"id":403,"version":0},{"id":243,"version":0},{"id":246,"version":0},{"id":341,"version":1},{"id":290,"version":0},{"id":271,"version":1},{"id":292,"version":0},{"id":296,"version":0},{"id":279,"version":0},{"id":291,"version":0},{"id":295,"version":0},{"id":283,"version":0},{"id":444,"version":0},{"id":313,"version":1},{"id":314,"version":1},{"id":441,"version":0},{"id":453,"version":0},{"id":450,"version":0},{"id":451,"version":0},{"id":375,"version":0},{"id":410,"version":0},{"id":457,"version":0},{"id":380,"version":0},{"id":381,"version":0},{"id":298,"version":0},{"id":437,"version":0},{"id":464,"version":0},{"id":382,"version":0},{"id":337,"version":1},{"id":415,"version":0},{"id":328,"version":0},{"id":251,"version":0},{"id":333,"version":1},{"id":475,"version":0},{"id":389,"version":0},{"id":306,"version":1},{"id":383,"version":0},{"id":387,"version":0},{"id":386,"version":0},{"id":392,"version":0},{"id":391,"version":0},{"id":390,"version":0},{"id":384,"version":0},{"id":388,"version":0},{"id":393,"version":0},{"id":385,"version":0},{"id":352,"version":1},{"id":303,"version":0},{"id":465,"version":0},{"id":427,"version":0},{"id":312,"version":0},{"id":467,"version":0},{"id":476,"version":1},{"id":308,"version":1},{"id":309,"version":1},{"id":356,"version":1},{"id":447,"version":0},{"id":319,"version":0},{"id":363,"version":0},{"id":419,"version":0},{"id":445,"version":0},{"id":330,"version":0},{"id":462,"version":0},{"id":250,"version":0},{"id":310,"version":1},{"id":307,"version":0},{"id":466,"version":0},{"id":368,"version":0},{"id":421,"version":0},{"id":443,"version":0},{"id":407,"version":0},{"id":350,"version":0},{"id":430,"version":0},{"id":472,"version":0},{"id":353,"version":1},{"id":354,"version":1},{"id":338,"version":0},{"id":300,"version":1},{"id":323,"version":1},{"id":324,"version":0},{"id":460,"version":0},{"id":461,"version":0},{"id":273,"version":0},{"id":275,"version":1},{"id":280,"version":0},{"id":281,"version":0},{"id":277,"version":0},{"id":278,"version":0},{"id":276,"version":1},{"id":274,"version":0},{"id":286,"version":0},{"id":372,"version":0},{"id":435,"version":0},{"id":327,"version":0},{"id":329,"version":0},{"id":288,"version":0},{"id":418,"version":0},{"id":463,"version":0},{"id":446,"version":0},{"id":449,"version":0},{"id":455,"version":0},{"id":399,"version":0},{"id":394,"version":0},{"id":395,"version":0},{"id":242,"version":0},{"id":248,"version":0},{"id":396,"version":0},{"id":261,"version":0},{"id":244,"version":0},{"id":431,"version":0},{"id":357,"version":0},{"id":473,"version":0},{"id":358,"version":1},{"id":339,"version":0},{"id":425,"version":0},{"id":422,"version":0},{"id":343,"version":1}]}
}