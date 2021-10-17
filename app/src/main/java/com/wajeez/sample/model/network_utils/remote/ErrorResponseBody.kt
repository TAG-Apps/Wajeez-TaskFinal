package  com.wajeez.sample.model.network_utils.remote


data class ErrorResponseBody(
    val success: Boolean?,
    val status: Int?,
    val message: String?
)